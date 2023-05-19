package com.liuzz.cloud.common.core.config;

import com.liuzz.cloud.common.core.aspect.LogAspect;
import com.liuzz.cloud.common.core.listener.LogListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 日志配置
 *
 * @author liuzz
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication
@AutoConfiguration
public class LogConfig {

    @Bean
    public LogListener sysLogListener() {
        return new LogListener();
    }

    @Bean
    public LogAspect sysLogAspect() {
        return new LogAspect();
    }

}
