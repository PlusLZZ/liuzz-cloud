package com.liuzz.cloud.common.mybatis.utils;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.liuzz.cloud.common.mybatis.constants.DsConstant;
import com.liuzz.cloud.common.mybatis.properties.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * 动态数据源工具类
 * @author liuzz
 */
@Component
@Slf4j
public class DsUtil implements ApplicationContextAware {

    /**
     * 核心动态数据源实现
     */
    private static DynamicRoutingDataSource DS;

    /**
     * 数据源构建器
     */
    private static DefaultDataSourceCreator DS_CREATOR;

    private static DataSourceProperties PROPERTIES;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DS = applicationContext.getBean(DynamicRoutingDataSource.class);
        DS_CREATOR = applicationContext.getBean(DefaultDataSourceCreator.class);
        PROPERTIES = applicationContext.getBean(DataSourceProperties.class);
        DS.setPrimary(PROPERTIES.getPrimary());
    }

    /**
     * 获取动态数据源对象
     */
    public static DynamicRoutingDataSource getDynamicDataSource() {
        return DS;
    }

    /**
     * 移除数据源
     */
    public static void removeDs(String dsName) {
        try {
            DataSource dsDataSource = DS.getDataSources().get(dsName);
            if (dsDataSource != null) {
                DS.removeDataSource(dsName);
            }
        } catch (Exception e) {
            log.error("移除数据源失败", e);
        }
    }


    /**
     * 添加数据源
     */
    public static void addDataSource(String dsName, String username, String password, String url, String driverClassName) {
        DataSourceProperty property = new DataSourceProperty();
        property.setUsername(username);
        property.setPassword(password);
        property.setUrl(url);
        property.setDriverClassName(driverClassName);
        property.setLazy(true);
        property.setPoolName(dsName);
        DataSource dataSource = DS_CREATOR.createDataSource(property);
        if (dataSource != null) {
            removeDs(dsName);
            DS.addDataSource(dsName, dataSource);
        }
    }

    /**
     * 添加数据源
     */
    public static void addDataSource(String dsName, DataSourceProperty property) {
        if (StrUtil.isBlank(property.getPoolName())) {
            property.setPoolName(dsName);
        }
        property.setPassword(property.getPassword());
        DataSource dataSource = DS_CREATOR.createDataSource(property);
        if (dataSource != null) {
            removeDs(dsName);
            DS.addDataSource(dsName, dataSource);
        }
    }

    /**
     * 从数据库中查询指定name的数据源配置
     */
    public static DataSourceProperty queryDataSource(String dsName) {
        Connection conn = null;
        Statement stmt = null;
        try {
            if (StringUtils.hasText(PROPERTIES.getDriverClassName())) {
                Class.forName(PROPERTIES.getDriverClassName());
            }
            conn = DriverManager.getConnection(PROPERTIES.getUrl(), PROPERTIES.getUsername(), PROPERTIES.getPassword());
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(StrUtil.format("{} and {} = '{}'", PROPERTIES.getQueryDsSql(), DsConstant.DS_NAME, dsName));
            if (rs.next()) {
                String username = rs.getString(DsConstant.DS_USER_NAME);
                String password = rs.getString(DsConstant.DS_USER_PWD);
                String url = rs.getString(DsConstant.DS_JDBC_URL);
                String driverName = rs.getString(DsConstant.DS_DRIVER_CLASS_NAME);
                DataSourceProperty property = new DataSourceProperty();
                property.setUsername(username);
                property.setPassword(password);
                property.setUrl(url);
                property.setDriverClassName(driverName);
                property.setLazy(true);
                log.info("数据源[{}]初始化查询成功", dsName);
                return property;
            } else {
                log.error("数据源[{}]初始化查询失败", dsName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeConnection(conn);
            JdbcUtils.closeStatement(stmt);
        }
        return null;
    }

    /**
     * 获取主数据源的名称
     */
    public static String getPrimaryDsName() {
        return PROPERTIES.getPrimary();
    }

    /**
     * 动态检查数据源,如果当前系统没有此数据源,就去数据库查询一次并进行初始化
     *
     * @param dsName 数据源名称
     */
    public static void dynamicCheckDataSource(String dsName) {
        if (!DS.getDataSources().containsKey(dsName)) {
            synchronized (dsName.intern()) {
                if (!DS.getDataSources().containsKey(dsName)) {
                    Optional.ofNullable(queryDataSource(dsName))
                            .ifPresent(property -> {
                                log.debug("数据源动态加载:{}", dsName);
                                addDataSource(dsName, property);
                            });
                }
            }
        }
    }

    /**
     * 切换数据源
     */
    public static void switchDs(String dsName) {
        dynamicCheckDataSource(dsName);
        DynamicDataSourceContextHolder.push(dsName);
    }

    /**
     * 退出此次数据源切换
     */
    public static void exitDs() {
        DynamicDataSourceContextHolder.poll();
    }

    /**
     * 退出此次数据源切换
     * 提供安全退出,检测当前数据源是否是想要退出的那个数据源,如果不是,默认不操作
     *
     * @param dsName 数据源名称
     */
    public static void exitDs(String dsName) {
        String peek = DynamicDataSourceContextHolder.peek();
        if (StrUtil.equals(dsName, peek)) {
            DynamicDataSourceContextHolder.poll();
        }
    }

    /**
     * 清空当前数据源栈
     */
    public static void clearDs() {
        DynamicDataSourceContextHolder.clear();
    }

    /**
     * 执行SQL操作
     * 示例:
     * <p>
     *     List<Map<String, Object>> data = execute(() -> {
     *             return jdbcTemplate.queryForList("select * from sys_user");
     *         }, "hip_base");
     * </p>
     *
     * @param supplier 提供者
     * @param dsName   数据源名称
     * @param <R>      泛型
     * @return R
     */
    public static <R> R execute(Supplier<R> supplier, String dsName) {
        try {
            switchDs(dsName);
            return supplier.get();
        } finally {
            exitDs(dsName);
        }
    }

    /**
     * 执行SQL操作
     * <p>
     *         String userName = "admin";
     *         SysUser user = execute(str -> {
     *             return userMapper.selectByName(str);
     *         }, userName,"hip_base");
     * </p>
     * 便于封装一些常用操作
     * @param function 函数接口
     * @param t        操作对象
     * @param dsName   数据源名称
     * @return         R
     * @param <T>      泛型操作对象
     * @param <R>      泛型返回对象
     */
    public static <T, R> R execute(Function<T, R> function, T t, String dsName) {
        try {
            switchDs(dsName);
            return function.apply(t);
        } finally {
            exitDs(dsName);
        }
    }
}
