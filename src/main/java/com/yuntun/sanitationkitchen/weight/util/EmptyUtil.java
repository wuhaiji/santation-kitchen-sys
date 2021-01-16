package com.yuntun.sanitationkitchen.weight.util;

/**
 * @author wujihong
 */
public class EmptyUtil {

    /**
     * 判断对象和内容是否为空
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(T t) {
        if (t == null) {
            return true;
        }
        T instance;
        try {
            instance = (T) t.getClass().newInstance();
        } catch (Exception e) {
            // 对象不存在无参构造函数,所以通过反射获取象失败！（因此，不存在对象内容为空）
            return false;
        }
        return t.equals(instance);
    }
}
