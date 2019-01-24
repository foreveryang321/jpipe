package top.ylonline.jpipe.threadpool.common;

import top.ylonline.jpipe.threadpool.JpipeThreadPoolExecutor;

/**
 * @author YL
 */
public interface ThreadPool {
    /**
     * Thread pool
     *
     * @param pool pool parameter
     *
     * @return thread pool
     */
    JpipeThreadPoolExecutor getExecutor(Pool pool);
}
