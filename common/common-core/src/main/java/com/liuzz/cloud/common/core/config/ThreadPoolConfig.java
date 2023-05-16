package com.liuzz.cloud.common.core.config;

import com.liuzz.cloud.common.core.utils.thread.ThreadPoolUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;

/**
 * 系统线程池配置
 * @author liuzz
 */
@AutoConfiguration
public class ThreadPoolConfig implements AsyncConfigurer {

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        return ThreadPoolUtil.createSimpleExecutor();
    }

}
