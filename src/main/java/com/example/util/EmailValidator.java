package com.example.util;

import java.util.regex.Pattern;

/**
 * 邮箱验证工具类
 * 使用RFC 5322标准的简化正则表达式
 */
public final class EmailValidator {

    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    private EmailValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 验证邮箱格式
     *
     * @param email 待验证的邮箱
     * @return 如果邮箱格式正确返回true，否则返回false
     */
    public static boolean isValid(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return pattern.matcher(email.trim()).matches();
    }
}

