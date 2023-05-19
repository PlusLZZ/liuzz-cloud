package com.liuzz.cloud.common.feign.handler;


import cn.hutool.core.util.StrUtil;
import com.liuzz.cloud.common.core.constants.HttpConstant;
import com.liuzz.cloud.common.core.domain.Result;
import com.liuzz.cloud.common.core.exception.BizException;
import com.liuzz.cloud.common.core.exception.ServerException;
import com.liuzz.cloud.common.core.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理器结合sentinel 全局异常处理器不能作用在 oauth server https://gitee.com/log4j/pig/issues/I1M2TJ
 * @author liuzz
 */
@Slf4j
@Order(10000)
@RestControllerAdvice
@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class GlobalBizExceptionHandler {

	/**
	 * 全局异常
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public Result handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		return Result.error(e.getMessage());
	}

	/**
	 * 处理业务校验过程中碰到的非法参数异常
	 * @see Assert#hasLength(String, String)
	 * @see Assert#hasText(String, String)
	 * @see Assert#isTrue(boolean, String)
	 * @see Assert#isNull(Object, String)
	 * @see Assert#notNull(Object, String)
	 * @param exception 参数校验异常
	 * @return API返回结果对象包装后的错误输出结果
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result handleIllegalArgumentException(IllegalArgumentException exception) {
		log.error("参数校验异常,ex = {}", exception.getMessage(), exception);
		return Result.error(exception.getMessage());
	}

	/**
	 * AccessDeniedException
	 * @return Result
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.OK)
	public Result handleAccessDeniedException(AccessDeniedException e) {
		log.error(StrUtil.format("权限异常 ex={}", e.getMessage()),e);
		return Result.error("权限不足,不允许访问").setCode(String.valueOf(HttpConstant.FORBIDDEN));
	}

	/**
	 * validation参数校验异常
	 * @param exception
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result handleBodyValidException(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return Result.error(String.format("%s %s", fieldErrors.get(0).getField(), fieldErrors.get(0).getDefaultMessage()));
	}

	/**
	 * validation Exception (以form-data形式传参)
	 * @param exception
	 */
	@ExceptionHandler({ BindException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result bindExceptionHandler(BindException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return Result.error(fieldErrors.get(0).getDefaultMessage());
	}

	@ExceptionHandler({ BizException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result bizExceptionHandler(BizException exception) {
		log.error("业务异常,ex = {}", exception.getMessage());
		return Result.error(exception.getMessage()).setCode(exception.getCode());
	}

	@ExceptionHandler({ ServerException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result serverExceptionHandler(ServerException exception) {
		log.error("服务异常,ex = {}", exception.getMessage());
		return Result.error(exception.getMessage());
	}

	@ExceptionHandler({ SystemException.class })
	@ResponseStatus(HttpStatus.OK)
	public Result systemExceptionHandler(SystemException exception) {
		log.error("系统异常,ex = {}", exception.getMessage());
		return Result.error(exception.getMessage());
	}

}
