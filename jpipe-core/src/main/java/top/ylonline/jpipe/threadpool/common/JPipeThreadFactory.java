package top.ylonline.jpipe.threadpool.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Created by YL on 2018/9/7
 */
public class JPipeThreadFactory implements ThreadFactory {
    private final AtomicInteger count = new AtomicInteger(1);

    private final ThreadGroup group;
    private final String name;
    private final boolean daemon;

    public JPipeThreadFactory(String name, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.name = name;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(group, runnable, name + "-" + count.getAndIncrement(), 0);
        thread.setDaemon(daemon);
        return thread;
    }
}
