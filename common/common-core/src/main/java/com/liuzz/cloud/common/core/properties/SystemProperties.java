package com.liuzz.cloud.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 通用系统配置
 *
 * @author liuzz
 */
@ConfigurationProperties(prefix = "system", ignoreInvalidFields = false)
@Data
@RefreshScope
public class SystemProperties {
}
