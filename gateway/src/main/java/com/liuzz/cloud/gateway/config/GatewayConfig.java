package com.liuzz.cloud.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuzz.cloud.gateway.filter.RequestGlobalFilter;
import com.liuzz.cloud.gateway.filter.SwaggerGatewayFilter;
import com.liuzz.cloud.gateway.handler.GlobalExceptionHandler;
import com.liuzz.cloud.gateway.properties.GatewayProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuzz
 */
@Configuration
@EnableConfigurationProperties({GatewayProperties.class})
public class GatewayConfig {

    /**
     * 全局请求处理
     */
    @Bean
    public RequestGlobalFilter requestGlobalFilter() {
        return new RequestGlobalFilter();
    }

    /**
     * swagger过滤
     */
    @Bean
    @ConditionalOnProperty(name = "gateway.swagger.basic.enabled")
    public SwaggerGatewayFilter swaggerBasicGatewayFilter(GatewayProperties gatewayProperties) {
        return new SwaggerGatewayFilter(gatewayProperties);
    }

    /**
     * 网关异常处理器
     */
    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }

}
