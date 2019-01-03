package top.ylonline.jpipe.threadpool.common;

import lombok.extern.slf4j.Slf4j;
import top.ylonline.jpipe.util.JVMUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Created by YL on 2018/9/10
 */
@Slf4j
public class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy {
    private final String threadName;

    private static volatile long lastPrintTime = 0;

    private static Semaphore guard = new Semaphore(1);

    public AbortPolicyWithReport(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        String msg = String.format("Thread pool is exhausted!" +
                        " Thread Name: %s, Pool Size: %d (active: %d, core: %d, max: %d, largest: %d), Task: %d " +
                        "(completed: %d)," +
                        " Executor status:(isShutdown:%s, isTerminated:%s, isTerminating:%s)",
                threadName, e.getPoolSize(), e.getActiveCount(), e.getCorePoolSize(), e.getMaximumPoolSize(),
                e.getLargestPoolSize(),
                e.getTaskCount(), e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
        log.warn(msg);
        dumpJStack();
        throw new RejectedExecutionException(msg);
    }

    private void dumpJStack() {
        long now = System.currentTimeMillis();

        // dump every 10 minutes
        if (now - lastPrintTime < 10 * 60 * 1000) {
            return;
        }

        if (!guard.tryAcquire()) {
            return;
        }
        // 启动一条线程 dump 内存快照
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String dumpPath = System.getProperty("user.home");
                // window system don't support ":" in file name
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                String dateStr = sdf.format(new Date());
                FileOutputStream jstackStream = null;
                try {
                    jstackStream = new FileOutputStream(new File(dumpPath, "JPipe_JStack.log" + "." + dateStr));
                    JVMUtils.jstack(jstackStream);
                } catch (Throwable t) {
                    log.error("dump jstack error", t);
                } finally {
                    guard.release();
                    if (jstackStream != null) {
                        try {
                            jstackStream.flush();
                            jstackStream.close();
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
                lastPrintTime = System.currentTimeMillis();
            }
        });
    }
}
