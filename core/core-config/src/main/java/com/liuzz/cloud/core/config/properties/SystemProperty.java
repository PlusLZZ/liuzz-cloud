package com.liuzz.cloud.core.config.properties;

import com.liuzz.cloud.core.config.annotations.PropertyKey;
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
@PropertyKey("system")
public class SystemProperty {
}
