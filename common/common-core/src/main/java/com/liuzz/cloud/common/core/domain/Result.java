package com.liuzz.cloud.common.core.domain;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.liuzz.cloud.common.core.constants.SystemConstant;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

/**
 * 统一响应主体
 *
 * @author liuzz
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CODE = "code";

    public static final String MSG = "msg";

    public static final String DATA = "data";

    public static final String TOTAL = "total";

    public static final String SUCCESS_TEXT = "请求成功";

    public static final String ERROR_TEXT = "请求失败";

    public Result(int initialCapacity) {
        super(initialCapacity);
    }

    public static Result buildResult(String code, String msg, Object data, Long total) {
        Result result = new Result(8);
        result.put(CODE, code);
        result.put(MSG, msg);
        result.put(DATA, data);
        result.put(TOTAL, total);
        return result;
    }

    public static Result success() {
        return buildResult(SystemConstant.SUCCESS, SUCCESS_TEXT, null, null);
    }

    public static Result success(Object data) {
        return buildResult(SystemConstant.SUCCESS, SUCCESS_TEXT, data, null);
    }

    public static Result success(Object data, String message, Object[] args) {
        return buildResult(SystemConstant.SUCCESS, StrUtil.format(message, args), data, null);
    }

    public static Result toResult(int result) {
        if (result > 0) {
            return success();
        }
        return error();
    }

    public static Result error() {
        return buildResult(SystemConstant.FAIL, ERROR_TEXT, null, null);
    }

    public static Result error(String message, Object[] args) {
        return buildResult(SystemConstant.FAIL, StrUtil.format(message, args), null, null);
    }

    public static Result error(Throwable cause, String message, Object[] args) {
        return buildResult(SystemConstant.FAIL, StrUtil.format(message, args) + StrUtil.format("({})", cause.getMessage()), null, null);
    }

    public static Result page(Collection<Object> list, Long total) {
        return buildResult(SystemConstant.SUCCESS, SUCCESS_TEXT, list, total);
    }

    public static Result page(Page<?> page) {
        return buildResult(SystemConstant.SUCCESS, SUCCESS_TEXT, page.getRecords(), page.getTotal());
    }

    /**
     * 是否是分页对象,通过TOTAL值是否为空判断
     */
    public boolean isPage() {
        return get(TOTAL) != null;
    }

}
