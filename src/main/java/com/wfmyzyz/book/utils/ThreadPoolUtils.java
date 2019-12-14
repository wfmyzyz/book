package com.wfmyzyz.book.utils;


import java.time.Instant;
import java.util.concurrent.*;

/**
 * @author xiong
 */
public class ThreadPoolUtils {

    /**
     * 获取定长的线程池
     * @param num
     * @return
     */
    public static ExecutorService newFixedThreadPool(int num){
        return new ThreadPoolExecutor(num,num,0L,TimeUnit.MINUTES,new LinkedBlockingQueue());
    }

    /**
     * 获取无界的线程池
     * @return
     */
    public static ExecutorService newCachedThreadPool(){
        return new ThreadPoolExecutor(0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,new SynchronousQueue<>());
    }

    /**
     * 获取单列的线程池
     * @return
     */
    public static ExecutorService newSingleThreadExecutor(){
        return new ThreadPoolExecutor(1,1,0L,TimeUnit.MICROSECONDS,new SynchronousQueue<>());
    }

    /**
     * 获取定时器的线程池
     * @param num
     * @return
     */
    public static ScheduledExecutorService newScheduledThreadPool(int num){
        return new ScheduledThreadPoolExecutor(num);
    }
}
