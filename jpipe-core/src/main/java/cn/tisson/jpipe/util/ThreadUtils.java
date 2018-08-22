// package cn.tisson.bigpipe.core.util;
//
// import java.util.Map;
// import java.util.concurrent.CompletionService;
// import java.util.concurrent.ExecutorCompletionService;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.ThreadPoolExecutor;
// import java.util.concurrent.TimeUnit;
//
// /**
//  * Thread util
//  *
//  * @author Created by YL on 2018/8/12
//  */
// public class ThreadUtils {
//     private static final int THREAD_MULTIPLER = 2;
//
//     private static ExecutorService scheduler;
//
//     // private BlockingQueue<Future<Map<String, Object>>> queue = new LinkedBlockingDeque<>(1000);
//     private CompletionService<Map<String, Object>> completionService = new ExecutorCompletionService<>(scheduler);
//     // new ExecutorCompletionService<>(scheduler, queue);
//
//     static {
//         int corePoolSize = getSuitableThreadCount();
//         int maxinumPoolSize = 1024;
//         long keepAliveTime = 20;
//         scheduler = new ThreadPoolExecutor(corePoolSize, maxinumPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
//     }
//
//     /**
//      * 通过内核数，算出合适的线程数；1.5-2倍cpu内核数
//      *
//      * @return thread count
//      */
//     private static int getSuitableThreadCount() {
//         final int coreCount = Runtime.getRuntime().availableProcessors();
//         System.out.println("核心数：" + coreCount);
//         int workerCount = 1;
//         while (workerCount < coreCount * THREAD_MULTIPLER) {
//             workerCount <<= 1;
//         }
//         System.out.println("工作线程数：" + workerCount);
//         return workerCount;
//     }
//
//     private static void execute(Runnable runnable) {
//         if (runnable == null) {
//             return;
//         }
//         if (scheduler != null) {
//             scheduler.execute(runnable);
//         }
//     }
// }
