package top.ylonline.jpipe.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author YL
 */
public class JpipeThreadPoolExecutor extends ThreadPoolExecutor {
    public JpipeThreadPoolExecutor(int coreSize, int maxPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> queue, ThreadFactory factory,
                                   RejectedExecutionHandler handler) {
        super(coreSize, maxPoolSize, keepAliveTime, unit, queue, factory, handler);
    }
}
