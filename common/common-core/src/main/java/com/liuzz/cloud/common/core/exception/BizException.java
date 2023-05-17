package com.liuzz.cloud.common.core.exception;

import cn.hutool.core.util.StrUtil;
import com.liuzz.cloud.common.core.constants.SystemConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常
 * 一般为业务手动抛出异常,需要带上错误码等信息
 *
 * @author liuzz
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String code;

    @Getter
    @Setter
    private String message;

    public BizException() {
    }

    public BizException(String message) {
        this.code = SystemConstant.FAIL;
        this.message = message;
    }

    public BizException(String message, Object[] args) {
        this.code = SystemConstant.FAIL;
        this.message = StrUtil.format(message, args);
    }

    public BizException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(String code, String message, Object[] args) {
        this.code = code;
        this.message = StrUtil.format(message, args);
    }

    public BizException(Throwable cause) {
        super(cause);
        this.code = SystemConstant.FAIL;
        this.message = cause.getMessage();
    }

    public BizException(Throwable cause, String message) {
        super(cause);
        this.code = SystemConstant.FAIL;
        this.message = message + StrUtil.format("({})", cause.getMessage());
    }

    public BizException(Throwable cause, String message, Object[] args) {
        super(cause);
        this.code = SystemConstant.FAIL;
        this.message = StrUtil.format(message, args) + StrUtil.format("({})", cause.getMessage());
    }

    public BizException(Throwable cause, String code, String message) {
        super(cause);
        this.code = code;
        this.message = message + StrUtil.format("({})", cause.getMessage());
    }

    public BizException(Throwable cause, String code, String message, Object[] args) {
        super(cause);
        this.code = code;
        this.message = StrUtil.format(message, args) + StrUtil.format("({})", cause.getMessage());
    }

    /**
     * 不写入堆栈信息，提高性能
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }


}
