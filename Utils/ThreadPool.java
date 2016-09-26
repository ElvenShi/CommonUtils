package com.syz.example.threadpool;

import android.os.Process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SYZ on 16/9/26.
 * 线程池
 */
public class ThreadPool {
    /**
     * 当前可获得CPU内核数量
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    /**
     * 核心线程数目，即使线程池没有任务，核心线程也不会终止，
     * 除非设置了allowCoreThreadTimeOut参数）可以理解为“常驻线程”
     */
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    /**
     * 线程池最大线程数
     * 设置为CPU内核数的两倍加1
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    /**
     * 线程存活时间，当线程池中的线程数目比核心线程多的时候，
     * 如果超过这个keepAliveTime的时间，多余的线程会被回收；
     * 这些与核心线程相对的线程通常被称为缓存线程
     */
    private static final int KEEP_ALIVE_SECONDS = 30;

    private static final Executor mExecutor;

    /**
     * 初始化线程池
     */
    static {
        // 创建线程池工厂
        ThreadFactory factory = new PriorityThreadFactory("thread-pool", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        // 创建工作队列
        BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<Runnable>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS, workQueue, factory);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        mExecutor = threadPoolExecutor;
    }

    private ThreadPool(){

    }

    private static final ThreadPool instance = new ThreadPool();

    public static ThreadPool getInstance(){
        return instance;
    }

    /**
     * 执行任务
     * @param runnable
     */
    public void execute(Runnable runnable){
        mExecutor.execute(runnable);
    }

    /**
     * 线程工厂，给定优先级
     */
    private static class PriorityThreadFactory implements ThreadFactory {
        /**
         * 线程池名称
         */
        private String threadName;
        /**
         * 线程池优先级
         */
        private int threadPriority;
        /**
         * 线程池中线程数量
         */
        private AtomicInteger threadNumber = new AtomicInteger();

        public PriorityThreadFactory(String threadName,int threadPriority){
            this.threadName = threadName;
            this.threadPriority = threadPriority;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,threadName+"-"+threadNumber.getAndIncrement()){
                @Override
                public void run() {
                    Process.setThreadPriority(threadPriority);
                    super.run();
                }
            };
        }
    }
}
