package top.ylonline.jpipe.threadpool.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * Thread util
 *
 * @author YL
 */
public class ThreadUtils {
    private static final int MULTIPLE_OF_THREAD = 2;

    /**
     * 通过内核数，算出合适的线程数；1.5-2倍cpu内核数
     *
     * @return thread count
     */
    public static int getSuitableThreadCount() {
        final int coreCount = Runtime.getRuntime().availableProcessors();
        int workerCount = 1;
        while (workerCount < coreCount * MULTIPLE_OF_THREAD) {
            workerCount <<= 1;
        }
        return workerCount;
    }

    public static BlockingQueue<Runnable> createQueue(int capacity) {
        if (capacity > 0) {
            return new LinkedBlockingQueue<>(capacity);
        } else {
            return new SynchronousQueue<>();
        }
    }
}
