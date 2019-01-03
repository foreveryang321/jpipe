package top.ylonline.jpipe.threadpool.eager;

import top.ylonline.jpipe.threadpool.JPipeThreadPoolExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Created by YL on 2018/9/7
 */
public class EagerThreadPoolExecutor extends JPipeThreadPoolExecutor {
    /**
     * The number of tasks submitted but not yet finished.
     * This number is always greater or equal to {@link #getActiveCount()}.
     */
    private final AtomicInteger submittedCount = new AtomicInteger(0);

    public EagerThreadPoolExecutor(int coreSize, int maxPoolSize, long keepAliveTime, TimeUnit unit,
                                   BlockingQueue<Runnable> queue, ThreadFactory factory,
                                   RejectedExecutionHandler handler) {
        super(coreSize, maxPoolSize, keepAliveTime, unit, queue, factory, handler);
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }
        submittedCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException rx) {
            if (super.getQueue() instanceof EagerTaskQueue) {
                // retry to offer the task into queue.
                final EagerTaskQueue queue = (EagerTaskQueue) super.getQueue();
                try {
                    if (!queue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                        submittedCount.decrementAndGet();
                        throw new RejectedExecutionException("Queue capacity is full.", rx);
                    }
                } catch (InterruptedException x) {
                    // decrease any way
                    submittedCount.decrementAndGet();
                    throw new RejectedExecutionException(x);
                }
            } else {
                // decrease any way
                submittedCount.decrementAndGet();
                throw rx;
            }
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedCount.decrementAndGet();
    }

    public int getSubmittedCount() {
        return submittedCount.get();
    }
}
