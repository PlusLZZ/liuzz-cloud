package com.liuzz.cloud.common.mybatis.interceptor;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * 分页拦截器
 * 重构分页插件, 当 size 小于 0 时, 直接设置为 0, 防止错误查询全表
 *
 * @author liuzz
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MyPaginationInnerInterceptor extends PaginationInnerInterceptor {

    /**
     * 数据库类型
     * {@link #findIDialect(Executor)} 切换分页逻辑
     */
    private DbType dbType;

    /**
     * 方言实现类
     * {@link #findIDialect(Executor)} 切换分页逻辑
     */
    private IDialect dialect;

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
        // size 小于 0 直接设置为 0 , 即不查询任何数据
        if (null != page && page.getSize() < 0) {
            page.setSize(0);
        }
        super.beforeQuery(executor, ms, page, rowBounds, resultHandler, boundSql);
    }

}
