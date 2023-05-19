package com.liuzz.cloud.common.feign.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.MyFeignClientsRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author liuzz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableFeignClients
@Import({MyFeignClientsRegistrar.class})
public @interface EnableMyFeignClients {

	String[] value() default {};

	String[] basePackages() default { "com.liuzz.cloud.api" };

	Class<?>[] basePackageClasses() default {};

	Class<?>[] defaultConfiguration() default {};

	/**
	 * 用@FeignClient注释的类列表。如果不为空，则禁用类路径扫描。
	 */
	Class<?>[] clients() default {};

}
