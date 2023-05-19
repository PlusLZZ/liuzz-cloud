package com.liuzz.cloud.common.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 基础数据源配置
 * @author liuzz
 */
@Data
@ConfigurationProperties("spring.datasource")
@RefreshScope
public class DataSourceProperties {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * jdbcUrl
     */
    private String url;

    /**
     * 驱动类
     */
    private String driverClassName;

    /**
     * 查询数据源的SQL
     */
    private String queryDsSql = "select * from sys_datasource where del_flag = 0";

    /**
     * 默认的主数据源,对于某些只需要使用同一数据源的项目,可以配置对应的数据源可以不用再进行切换
     */
    private String primary = "master";

}
