package top.ylonline.jpipe.threadpool.eager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Created by YL on 2018/9/7
 */
public class EagerTaskQueue extends LinkedBlockingQueue<Runnable> {
    private static final long serialVersionUID = 1L;

    private transient volatile EagerThreadPoolExecutor executor = null;

    public EagerTaskQueue(int capacity) {
        super(capacity);
    }

    public void setExecutor(EagerThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean offer(Runnable o) {
        // we can't do any checks
        if (executor == null) {
            throw new RejectedExecutionException("The task queue does not have executor!");
        }

        // current pool thread size
        int poolSize = executor.getPoolSize();
        int maxSize = executor.getMaximumPoolSize();
        int submittedCount = executor.getSubmittedCount();

        // if submittedCount <= poolSize, add it to the queue
        if (submittedCount <= poolSize) {
            return super.offer(o);
        }
        // if poolSize < maxSize, return false let executor create new thread.
        if (poolSize < maxSize) {
            return false;
        }
        // if poolSize >= maxSize, add it to the queue
        return super.offer(o);
    }

    @Override
    public Runnable take() throws InterruptedException {
        if (executor != null) {
            long keepAliveTime = executor.getKeepAliveTime(TimeUnit.MILLISECONDS);
            // NullPointException check?
            return super.poll(keepAliveTime, TimeUnit.MILLISECONDS);
        }
        return super.take();
    }

    /**
     * retry offer task
     *
     * @param o task
     *
     * @return offer success or not
     *
     * @throws RejectedExecutionException if executor is terminated.
     */
    public boolean retryOffer(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown! can't offer a command into the queue.");
        }
        return super.offer(o, timeout, unit);
    }
}
