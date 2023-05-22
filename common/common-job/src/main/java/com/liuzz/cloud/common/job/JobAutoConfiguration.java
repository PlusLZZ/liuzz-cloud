package com.liuzz.cloud.common.job;

import com.liuzz.cloud.common.job.properties.JobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/**
 * xxl-job配置
 *
 * @author liuzz
 */
@AutoConfiguration
@EnableConfigurationProperties(JobProperties.class)
public class JobAutoConfiguration {

    /**
     * 服务名称 包含 XXL_JOB_ADMIN 则说明是 Admin
     */
    private static final String JOB_ADMIN = "hip-xxl-job-admin";

    /**
     * 配置xxl-job执行器，提供自动发现admin的能力
     *
     * @param jobProperties   任务配置
     * @param environment     环境变量
     * @param discoveryClient 注册发现客户端
     * @return
     */
    @Bean
    public XxlJobSpringExecutor xxlJobSpringExecutor(JobProperties jobProperties,
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
                    .filter(s -> s.contains(JOB_ADMIN))
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
