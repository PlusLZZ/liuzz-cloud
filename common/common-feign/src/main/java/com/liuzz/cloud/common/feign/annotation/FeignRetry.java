package com.liuzz.cloud.common.feign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试注解，作用在 @FeignClient 注解之上
 * @author liuzz
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignRetry {

	/**
	 * 重试策略
	 */
	Backoff backoff() default @Backoff();

	/**
	 * 最大重试次数
	 */
	int maxAttempt() default 3;

	/**
	 * 指定需要进行重试的异常
	 */
	Class<? extends Throwable>[] include() default {};

}
