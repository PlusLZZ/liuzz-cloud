package com.liuzz.cloud.gateway.config;

import com.liuzz.cloud.gateway.properties.GatewayProperties;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuzz
 */
@Configuration(proxyBeanMethods = false)
public class SpringDocConfig {

    @Bean
    @Lazy(false)
    @ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
    public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters,
                                     GatewayProperties gatewayProperties) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        if (!CollectionUtils.isEmpty(gatewayProperties.getSwagger().getServices())){
            gatewayProperties.getSwagger().getServices().forEach((key,value)->{
                swaggerUiConfigParameters.addGroup(value);
            });
        }
        return groups;
    }

}
