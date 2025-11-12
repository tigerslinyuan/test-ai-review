package com.example.validation;

import com.example.constants.AppConstants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 用户ID验证器
 */
public class UserIdValidator implements ConstraintValidator<ValidUserId, String> {

    private static final String USER_ID_PATTERN = "^[a-zA-Z0-9_]+$";

    @Override
    public void initialize(ValidUserId constraintAnnotation) {
        // 初始化方法，可以在这里获取注解参数
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        if (id == null) {
            return false;
        }

        // 验证格式
        if (!id.matches(USER_ID_PATTERN)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("用户ID只能包含字母、数字和下划线")
                    .addConstraintViolation();
            return false;
        }

        // 验证长度
        if (id.length() > AppConstants.MAX_ID_LENGTH) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("用户ID长度不能超过" + AppConstants.MAX_ID_LENGTH)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}

