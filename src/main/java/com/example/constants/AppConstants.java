package com.example.constants;

/**
 * 应用常量
 */
public final class AppConstants {

    private AppConstants() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 用户ID前缀
     */
    public static final String USER_ID_PREFIX = "user_";

    /**
     * 最大ID长度
     */
    public static final int MAX_ID_LENGTH = 100;

    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;

    /**
     * 默认每页大小
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 最大每页大小
     */
    public static final int MAX_PAGE_SIZE = 100;

    /**
     * 一周的毫秒数
     */
    public static final long WEEK_IN_MILLIS = 7L * 24 * 60 * 60 * 1000;
}

