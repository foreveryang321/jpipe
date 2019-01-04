package top.ylonline.jpipe.threadpool.eager;

import top.ylonline.jpipe.common.Cts;
import top.ylonline.jpipe.threadpool.JpipeThreadPoolExecutor;
import top.ylonline.jpipe.threadpool.common.AbortPolicyWithReport;
import top.ylonline.jpipe.threadpool.common.JpipeThreadFactory;
import top.ylonline.jpipe.threadpool.common.Pool;
import top.ylonline.jpipe.threadpool.common.ThreadPool;
import top.ylonline.jpipe.threadpool.util.ThreadUtils;

import java.util.concurrent.TimeUnit;

/**
 * When the core threads are all in busy,
 * create new thread instead of putting task into blocking queue.
 *
 * @author Created by YL on 2018/9/10
 */
public class EagerThreadPool implements ThreadPool {

    @Override
    public JpipeThreadPoolExecutor getExecutor(Pool pool) {
        int coreSize = pool.getCoreSize();
        if (coreSize == -1) {
            coreSize = ThreadUtils.getSuitableThreadCount();
        }
        int maxSize = pool.getMaxSize();
        long keepAlive = pool.getKeepAlive();
        int queueSize = pool.getQueueSize();

        EagerTaskQueue queue = new EagerTaskQueue(queueSize <= 0 ? 1 : queueSize);
        EagerThreadPoolExecutor executor = new EagerThreadPoolExecutor(
                coreSize,
                maxSize,
                keepAlive,
                TimeUnit.MILLISECONDS,
                queue,
                new JpipeThreadFactory(Cts.JPIPE_THREAD_NAME, true),
                new AbortPolicyWithReport(Cts.JPIPE_THREAD_NAME)
        );
        if (pool.isPreStartAllCoreThreads()) {
            executor.prestartAllCoreThreads();
        }
        queue.setExecutor(executor);
        return executor;
    }
}
