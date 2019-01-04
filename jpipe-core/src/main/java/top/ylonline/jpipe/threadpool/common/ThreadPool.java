package top.ylonline.jpipe.threadpool.common;

import top.ylonline.jpipe.threadpool.JpipeThreadPoolExecutor;

/**
 * @author Created by YL on 2018/9/10
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
