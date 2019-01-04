package top.ylonline.jpipe.threadpool.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Wrapped thread factory for better use.
 *
 * @author Created by YL on 2018/9/7
 */
public class JpipeThreadFactory implements ThreadFactory {
    private final AtomicInteger count = new AtomicInteger(1);

    private final ThreadGroup group;
    private final String name;
    private final boolean daemon;

    public JpipeThreadFactory(String name, boolean daemon) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.name = name;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(group, runnable, name + "-thread-" + count.getAndIncrement(), 0);
        thread.setDaemon(daemon);
        return thread;
    }
}
