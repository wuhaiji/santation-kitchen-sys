package com.yuntun.sanitationkitchen.auth;

/**
 * 用于保存管理系统用户的Id,每个请求对应一个
 *
 * @author whj
 */
public class UserIdHolder {
    private static final ThreadLocal<Long> userTl = new ThreadLocal<>();

    public static void set(Long userId) {
        userTl.set(userId);
    }

    public static Long get() {
        return userTl.get();
    }

    public static void clear() {
        userTl.remove();
    }
}
