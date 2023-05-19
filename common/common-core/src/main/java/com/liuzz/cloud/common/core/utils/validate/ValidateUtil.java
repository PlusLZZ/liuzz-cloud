package com.liuzz.cloud.common.core.utils.validate;

import com.liuzz.cloud.common.core.exception.SystemException;
import lombok.SneakyThrows;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 校验工具类
 *
 * @author lzz
 */

public class ValidateUtil {

    /**
     * 快速失败校验器,有一个失败就返回
     */
    private static final Validator VALIDATOR_FAST = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

    /**
     * 校验返回错误信息
     *
     * @param domain 需要校验的对象
     * @param <T>    泛型
     * @return 第一条校验失败的信息
     */
    @SneakyThrows(Exception.class)
    public static <T> String validate(T domain) {
        Set<ConstraintViolation<T>> validateResult = VALIDATOR_FAST.validate(domain);
        if (CollectionUtils.isEmpty(validateResult)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<T> field : validateResult) {
            sb.append(field.getPropertyPath()).append(field.getMessage()).append("。");
        }
        return sb.toString();
    }

    /**
     * 校验自动抛出异常
     *
     * @param domain 需要校验的对象
     * @param <T>    泛型
     */
    public static <T> void validateThrowErr(T domain) {
        Set<ConstraintViolation<T>> validateResult = VALIDATOR_FAST.validate(domain);
        if (!CollectionUtils.isEmpty(validateResult)) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<T> field : validateResult) {
                sb.append(field.getPropertyPath()).append(field.getMessage()).append("。");
            }
            throw new SystemException(sb.toString());
        }

    }

}
