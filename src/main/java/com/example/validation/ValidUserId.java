package com.example.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 用户ID验证注解
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
@Documented
public @interface ValidUserId {
    String message() default "用户ID格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

