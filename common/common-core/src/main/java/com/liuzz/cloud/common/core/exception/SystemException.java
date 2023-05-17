package com.liuzz.cloud.common.core.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 系统异常,常见于工具类等特殊异常
 * @author liuzz
 */
public class SystemException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String format,Object... args){
        super(StrUtil.format(format, args),null,false,false);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
