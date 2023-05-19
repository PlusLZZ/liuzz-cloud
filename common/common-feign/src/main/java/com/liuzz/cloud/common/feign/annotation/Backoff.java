package com.liuzz.cloud.common.feign.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试策略
 * @author liuzz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Backoff {

	/**
	 * 初始休眠时间
	 */
	long delay() default 100L;;

	/**
	 * 最大休眠时间
	 */
	long maxDelay() default 1000L;

	/**
	 * 休眠时间逐级乘数 例如 delay=1000 multiplier=2 那么第二次的休眠时间就是1000*2
	 */
	double multiplier() default 1.0D;;

}
