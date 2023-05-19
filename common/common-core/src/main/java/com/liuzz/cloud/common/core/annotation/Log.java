package com.liuzz.cloud.common.core.annotation;


import java.lang.annotation.*;

/**
 * 日志注解
 * @author liuzz
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /**
     * 日志标题
     */
    String value();

    /**
     * 是否保存请求的参数
     */
    boolean saveRequest() default true;

    /**
     * 是否保存响应的结果
     */
    boolean saveResponse() default true;

}
