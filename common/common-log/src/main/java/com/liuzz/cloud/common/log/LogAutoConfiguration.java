package com.liuzz.cloud.common.log;

import com.liuzz.cloud.common.log.aspect.LogAspect;
import com.liuzz.cloud.common.log.event.LogListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * @author liuzz
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = SERVLET)
@AutoConfiguration
public class LogAutoConfiguration {

    @Bean
    public LogListener sysLogListener() {
        return new LogListener();
    }

    @Bean
    public LogAspect sysLogAspect() {
        return new LogAspect();
    }

}
