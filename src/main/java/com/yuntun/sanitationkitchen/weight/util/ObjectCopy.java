package com.yuntun.sanitationkitchen.weight.util;

import com.yuntun.sanitationkitchen.exception.ServiceException;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 注意：是两个相同类型的对象
 *
 * @author wujihong
 */
public class ObjectCopy {

    // 用于拷贝对象赋值（将source的内容拷贝到target中，source中的空值不处理）
    public static <T> void copyNotNullObject(T source, T target) {
        System.out.println("source:"+source);
        System.out.println("target:"+target);
        Objects.requireNonNull(source,"source null");
        Objects.requireNonNull(target,"target null");

        if (EmptyUtil.isEmpty(source)) {
            throw new ServiceException("source的内容为空！");
        }
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        if (sourceClass != targetClass) {
            throw new ServiceException("要求：两个参数为同一类型！");
        }
        Field[] fields = sourceClass.getDeclaredFields();
        String getMethodName;
        String setMethodName;

        for (Field field:fields) {
            getMethodName = ReflexUtil.getMethodName(field);
            Object targetObject = ReflexUtil.invokeGetMethod(getMethodName, targetClass, target);
            if (targetObject == null) {
                setMethodName = ReflexUtil.setMethodName(field);
                // 将source中的属性放入到target中
                Object sourceObject = ReflexUtil.invokeGetMethod(getMethodName, sourceClass, source);
                ReflexUtil.invokeSetMethod(setMethodName, targetClass, target, sourceObject);
            }
        }
    }


}
