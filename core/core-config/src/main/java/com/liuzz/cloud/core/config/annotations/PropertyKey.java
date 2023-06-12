package com.liuzz.cloud.core.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置键标记
 * @author liuzz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyKey {

	/**
	 * 键名
	 */
	String value();

	/**
	 * 配置描述
	 */
	String desc() default "";

}
