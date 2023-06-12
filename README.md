# 学习项目

## 一 项目分包构思

模块分类型应该为:

starter 依赖尽可能少的独立整合组件

sdk 特殊服务的开放接入工具,比如单点登录

common 需要独立部署的公共项目

core 包含项目运行底层依赖的核心组件

api  feign包接口

service 业务服务

基于以上定义进行构建

```
starter
  xxl-job-starter      提供定时任务接入
  log-starter          提供日志采集埋点接入
  mongo-starter        mongodb接入
  mybatis-plus-starter mp接入
  dynamic-datasource   动态数据源接入
  redis-starter        redis接入
  swagger-starter      swagger接入
common
  common-gateway       业务网关项目
  common-nacos         独立部署的nacos服务
  common-xxl-job       独立部署的xxl-job服务
  common-oauth2        独立的权限管理服务
sdk
  oauth2-sdk           权限服务接入包,可提供给第三方使用
core
  core-config          最细粒度的配置类集合与注解定义,无任何业务依赖
  core-util            基础工具类集合,整合谷歌工具类以及如梦工具包,禁用hutool与apache
  core-web             web服务核心依赖包,提供各种配置整合
  core-webflux         webflux核心依赖包,目前只需要给gateway使用
api
  ...
service
  platform-service     平台服务(包含各种基础能力,集成oauth2,用户管理等核心功能在平台提供)
```

