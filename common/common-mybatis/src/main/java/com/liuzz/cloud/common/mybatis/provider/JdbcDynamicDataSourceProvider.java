package com.liuzz.cloud.common.mybatis.provider;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.liuzz.cloud.common.mybatis.constants.DsConstant;
import com.liuzz.cloud.common.mybatis.properties.DataSourceProperties;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * 从数据源中获取配置信息
 * 默认只取master数据源,其它数据源通过后期获取
 *
 * @author liuzz
 */
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

    private final DataSourceProperties properties;

    public JdbcDynamicDataSourceProvider(DataSourceProperties properties) {
        super(properties.getDriverClassName(), properties.getUrl(), properties.getUsername(), properties.getPassword());
        this.properties = properties;
    }

    /**
     * 执行语句获得数据源参数
     *
     * @param statement 语句
     * @return 数据源参数
     * @throws SQLException sql异常
     */
    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery(properties.getQueryDsSql());

        Map<String, DataSourceProperty> map = new HashMap<>(8);
        while (rs.next()) {
            String name = rs.getString(DsConstant.DS_NAME);
            // 如果没有包含主数据源就暂时先不添加
            if (!StrUtil.equals(name,properties.getPrimary())){
                continue;
            }
            String username = rs.getString(DsConstant.DS_USER_NAME);
            String password = rs.getString(DsConstant.DS_USER_PWD);
            String url = rs.getString(DsConstant.DS_JDBC_URL);
            DataSourceProperty property = new DataSourceProperty();
            property.setUsername(username);
            property.setPassword(password);
            property.setUrl(url);
            // 必须要懒加载
            property.setLazy(true);
            Optional.ofNullable(rs.getString(DsConstant.DS_DRIVER_CLASS_NAME))
                    .ifPresent(property::setDriverClassName);

            map.put(name, property);
        }
        // 判断是否加载主数据源
        if (!map.containsKey(properties.getPrimary())){
            DataSourceProperty property = new DataSourceProperty();
            property.setUsername(properties.getUsername());
            property.setPassword(properties.getPassword());
            property.setUrl(properties.getUrl());
            property.setDriverClassName(properties.getDriverClassName());
            property.setLazy(true);
            map.put(properties.getPrimary(), property);
        }
        return map;
    }

}
