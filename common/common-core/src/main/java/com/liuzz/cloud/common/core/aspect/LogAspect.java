package com.liuzz.cloud.common.core.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.liuzz.cloud.common.core.annotation.Log;
import com.liuzz.cloud.common.core.domain.SysLog;
import com.liuzz.cloud.common.core.event.LogEvent;
import com.liuzz.cloud.common.core.utils.SpringContextHolder;
import com.liuzz.cloud.common.core.utils.json.JsonUtil;
import com.liuzz.cloud.common.core.utils.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 操作日志使用spring event异步入库
 *
 * @author liuzz
 */
@Aspect
@Slf4j
public class LogAspect {

    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 前置记录操作耗时
     */
    @Before("@annotation(syslog)")
    public void doBefore(JoinPoint joinPoint, Log syslog) {
        THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 请求处理完毕后执行
     */
    @AfterReturning(pointcut = "@annotation(syslog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log syslog, Object jsonResult) {
        handleLog(joinPoint, syslog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(syslog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log syslog, Exception e) {
        handleLog(joinPoint, syslog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log syslog, final Exception e, Object jsonResult) {
        try {
            SysLog logInfo = new SysLog();
            // 方法耗时
            logInfo.setOperatorTime(Math.abs(System.currentTimeMillis() - THREAD_LOCAL.get()));
            // 服务名称
            logInfo.setServiceName(SpringUtil.getApplicationName());
            // 日志标题
            logInfo.setTitle(syslog.value());
            // 方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            logInfo.setMethod(className + "." + methodName + "()");
            ServletUtil.getRequest().ifPresent(request -> {
                // 请求方式
                logInfo.setRequestMethod(request.getMethod());
                // 操作ip地址
                logInfo.setIp(cn.hutool.extra.servlet.ServletUtil.getClientIP(request));
                // 请求uri
                logInfo.setUri(request.getRequestURI());
                // 获取操作地点
                logInfo.setLocation(ServletUtil.getIpLocation(request));
            });
            if (syslog.saveRequest()){
                // 获取参数的信息，传入到数据库中。
                setRequestValue(joinPoint, logInfo);
            }
            if (syslog.saveResponse() && ObjectUtil.isNotNull(jsonResult)) {
                logInfo.setResult(JsonUtil.toJsonStr(jsonResult));
            }
            // 处理异常信息
            if (e != null) {
                logInfo.setErrorMsg(e.getMessage());
            }
            // 处理操作人
            Optional.ofNullable(SecurityContextHolder.getContext())
                    .map(SecurityContext::getAuthentication)
                    .ifPresent(auth -> logInfo.setCreateBy(auth.getName()));
            SpringContextHolder.publishEvent(new LogEvent(logInfo));
        } catch (Exception ex) {
            log.error("请求日志处理异常:{}", ex.getMessage());
            ex.printStackTrace();
        } finally {
            THREAD_LOCAL.remove();
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param logInfo 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysLog logInfo) throws Exception {
        String requestMethod = logInfo.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            logInfo.setParams(params);
        } else {
            ServletUtil.getRequest().ifPresent(request -> {
                Map<?, ?> paramsMap = (Map<?, ?>) request.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
                logInfo.setParams(JsonUtil.toJsonStr(paramsMap));
            });
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null) {
            for (Object o : paramsArray) {
                if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        params.append(JsonUtil.toJsonStr(o)).append(" ");
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }

}
