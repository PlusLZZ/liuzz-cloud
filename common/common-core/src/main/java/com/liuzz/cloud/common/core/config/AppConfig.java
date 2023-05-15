package com.liuzz.cloud.common.core.config;

import com.liuzz.cloud.common.core.properties.SystemProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 公共配置大全
 *
 * @author liuzz
 */
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@EnableAsync
@EnableConfigurationProperties({SystemProperties.class})
@AutoConfiguration
public class AppConfig {
}
