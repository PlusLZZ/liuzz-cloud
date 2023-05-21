package com.liuzz.cloud.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * 异常信息国际化
 * https://springdoc.cn/spring-security/index.html
 * @author liuzz
 */
@ConditionalOnWebApplication(type = SERVLET)
public class I18nConfig implements WebMvcConfigurer {

    @Bean
    @Primary
    public MessageSource securityMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.addBasenames("classpath:i18n/errors/messages");
        messageSource.addBasenames("classpath:i18n/messages");
        messageSource.setDefaultLocale(Locale.CHINA);
        return messageSource;
    }

}
