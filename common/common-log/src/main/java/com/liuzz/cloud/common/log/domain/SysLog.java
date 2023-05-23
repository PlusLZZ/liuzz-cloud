package com.liuzz.cloud.common.log.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author liuzz
 */
@Data
@Accessors(chain = true)
@TableName(value = "sys_log", autoResultMap = true)
@Schema(description = "日志对象")
public class SysLog {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "日志ID")
    private Long id;

    @TableField(value = "operator_time")
    @Schema(description = "方法操作耗时")
    private Long operatorTime;

    @TableField(value = "service_name")
    @Schema(description = "操作服务名称")
    private String serviceName;

    @Schema(description = "日志标题")
    @TableField(value = "title")
    private String title;

    @TableField(value = "method")
    @Schema(description = "方法名称")
    private String method;

    @TableField(value = "request_method")
    @Schema(description = "请求方式(GET,POST)")
    private String requestMethod;

    @TableField(value = "ip")
    @Schema(description = "操作ip地址")
    private String ip;

    @TableField(value = "uri")
    @Schema(description = "请求uri")
    private String uri;

    @TableField(value = "location")
    @Schema(description = "操作地点")
    private String location;

    @TableField(value = "params")
    @Schema(description = "请求参数")
    private String params;

    @TableField(value = "result")
    @Schema(description = "操作返回结果")
    private String result;

    @TableField(value = "error_msg")
    @Schema(description = "异常信息")
    private String errorMsg;

    @TableField(value = "remark")
    @Schema(description = "日志备注")
    private String remark;

    @Schema(description = "操作人")
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @Schema(description = "操作时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
