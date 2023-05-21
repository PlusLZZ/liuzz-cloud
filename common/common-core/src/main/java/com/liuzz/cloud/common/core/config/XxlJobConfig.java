package com.liuzz.cloud.common.core.config;

import com.liuzz.cloud.common.core.constants.ServiceNameConstant;
import com.liuzz.cloud.common.core.properties.XxlJobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * xxl-job配置
 * @author liuzz
 */
@AutoConfiguration
@EnableConfigurationProperties(XxlJobProperties.class)
@ConditionalOnWebApplication(type = SERVLET)
public class XxlJobConfig {

    /**
     * 配置xxl-job执行器，提供自动发现admin的能力
     *
     * @param jobProperties   任务配置
     * @param environment     环境变量
     * @param discoveryClient 注册发现客户端
     * @return
     */
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(XxlJobProperties jobProperties,
                                                     Environment environment,
                                                     DiscoveryClient discoveryClient) {
        XxlJobSpringExecutor executor = new XxlJobSpringExecutor();
        // 如果没有配置就使用应用名称作为服务名
        String appName = jobProperties.getAppName();
        if (!StringUtils.hasText(appName)) {
            appName = environment.getProperty("spring.application.name");
        }
        String accessToken = jobProperties.getAccessToken();
        executor.setAppname(appName);
        executor.setAddress(jobProperties.getServiceAddress());
        executor.setIp(jobProperties.getIp());
        executor.setPort(jobProperties.getPort());
        executor.setAccessToken(accessToken);
        executor.setLogPath(jobProperties.getLogPath());
        executor.setLogRetentionDays(jobProperties.getLogRetentionDays());
        if (!StringUtils.hasText(jobProperties.getAdminAddress())) {
            String serverList = discoveryClient.getServices().stream()
                    .filter(s -> s.contains(ServiceNameConstant.XXL_JOB_ADMIN))
                    .flatMap(s -> discoveryClient.getInstances(s).stream())
                    .map(instance -> String.format("http://%s:%s", instance.getHost(), instance.getPort()))
                    .collect(Collectors.joining(","));
            executor.setAdminAddresses(serverList);
        } else {
            executor.setAdminAddresses(jobProperties.getAdminAddress());
        }
        return executor;
    }

}
