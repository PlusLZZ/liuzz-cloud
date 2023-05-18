package com.liuzz.cloud.common.mybatis.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;

/**
 * sql执行时长打印
 *
 * @author liuzz
 */
@Intercepts(
        {@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
                @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
                @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})}
)
@Slf4j
public class ExecuteTimeInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!log.isDebugEnabled()) {
            return invocation.proceed();
        }
        long start = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            log.debug("<== Sql Execution Time : [{}] MS", System.currentTimeMillis() - start);
        }
    }
}
