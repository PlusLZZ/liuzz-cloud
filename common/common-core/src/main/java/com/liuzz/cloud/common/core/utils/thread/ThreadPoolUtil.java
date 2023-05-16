package com.liuzz.cloud.common.core.utils.thread;

import cn.hutool.core.util.StrUtil;
import com.liuzz.cloud.common.core.utils.runtime.RuntimeUtil;
import lombok.experimental.UtilityClass;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类
 *
 * @author liuzz
 */
@UtilityClass
public class ThreadPoolUtil {

    /**
     * 默认线程前缀
     */
    public final String THREAD_PREFIX = "liuzz";

    /**
     * 默认队列长度
     */
    public final Integer QUEUE_SIZE = 1000;

    /**
     * 创建一个简单的自定义线程池
     */
    public Executor createSimpleExecutor() {
        return createSimpleExecutor(THREAD_PREFIX);
    }

    /**
     * 创建一个简单的自定义线程池
     *
     * @param namePrefix 线程前缀名
     */
    public Executor createSimpleExecutor(String namePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int cpuCore = RuntimeUtil.getCpuCore();
        executor.setCorePoolSize(cpuCore);
        executor.setMaxPoolSize(cpuCore * 2);
        executor.setQueueCapacity(QUEUE_SIZE);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 关闭程序时会等待线程池中的任务运行完毕
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 关闭程序时会等待线程池中的任务运行完毕的超时时间
        executor.setAwaitTerminationSeconds(60);
        executor.setThreadNamePrefix(StrUtil.format("{}-", namePrefix));
        executor.initialize();
        return executor;
    }

}
