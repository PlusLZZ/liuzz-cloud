package com.liuzz.cloud.common.core.exception;

import cn.hutool.core.util.StrUtil;

/**
 * 服务抛出的异常
 * @author liuzz
 */
public class ServerException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String format,Object... args){
        super(StrUtil.format(format, args),null,false,false);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
