package com.yuntun.sanitationkitchen.util;

import com.yuntun.sanitationkitchen.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/9/23
 */
@Slf4j
public class EptUtil {

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

    /**
     * 判断对象和内容是否不为空
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

}
