package top.ylonline.jpipe.threadpool.common;

import lombok.Data;

/**
 * 连接池配置
 *
 * @author YL
 */
@Data
public class Pool {
    /**
     * Whether to start all core threads
     */
    private boolean preStartAllCoreThreads = false;

    /**
     * the number of threads to keep in the pool. 默认: -1, 相当于：2 * Runtime.getRuntime().availableProcessors()
     */
    private int coreSize = -1;

    /**
     * the maximum number of threads to allow in the pool
     */
    private int maxSize = 1024;

    /**
     * when the number of threads is greater than the max size, this is the maximum time that excess idle threads will
     * wait for new tasks before terminating. default: 60000(ms)
     */
    private long keepAlive = 60000;

    /**
     * the queue to use for holding tasks before they are executed.
     */
    private int queueSize = 1024;
}
