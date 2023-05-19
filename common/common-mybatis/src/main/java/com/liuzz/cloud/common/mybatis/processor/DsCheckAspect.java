package com.liuzz.cloud.common.mybatis.processor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.liuzz.cloud.common.mybatis.utils.DsUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 使用DS检测,如果没有对应的数据源就去加载
 *
 * @author liuzz
 */
@Aspect
@Slf4j
public class DsCheckAspect {

    @Before(value = "@annotation(com.baomidou.dynamic.datasource.annotation.DS)||@within(com.baomidou.dynamic.datasource.annotation.DS)")
    public void doBefore(JoinPoint joinPoint) {
        DS ds = getDataSource(joinPoint);
        if (ds != null){
            if (!ds.value().startsWith("#")) {
                DsUtil.dynamicCheckDataSource(ds.value());
            }
        }
    }

    /**
     * 获取需要切换的数据源
     */
    public DS getDataSource(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DS ds = method.getAnnotation(DS.class);
        if (ds != null){
            return ds;
        }
        Class<?> targetClass = point.getTarget().getClass();
        return targetClass.getAnnotation(DS.class);
    }

}
