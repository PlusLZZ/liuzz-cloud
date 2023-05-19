package com.liuzz.cloud.common.feign;

import com.liuzz.cloud.common.feign.annotation.EnableMyFeignClients;
import com.liuzz.cloud.common.feign.aspect.FeignRetryAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * feign
 * @author liuzz
 */
@AutoConfiguration
@EnableMyFeignClients
public class FeignAutoConfiguration {

    @Bean
    public FeignRetryAspect feignRetryAspect() {
        return new FeignRetryAspect();
    }

}
