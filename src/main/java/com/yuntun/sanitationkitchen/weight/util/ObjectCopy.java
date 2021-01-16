package com.yuntun.sanitationkitchen.weight.util;

import com.yuntun.sanitationkitchen.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static Logger logger = LoggerFactory.getLogger(ObjectCopy.class);

    // 用于拷贝对象赋值（将source的内容拷贝到target中，source中的空值不处理）
    public static <T> void copyNotNullObject(T source, T target) {
        logger.info("source:{}", source);
        logger.info("target:{}", target);
        Objects.requireNonNull(source,"source null");
        Objects.requireNonNull(target,"target null");
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        // 判断类型是否一致
        if (sourceClass != targetClass) {
            throw new ServiceException("要求：两个参数类型一致！");
        }

        // 判断两者内容是否相等
        if (source.equals(target)) {
            logger.warn("source content equals target content！");
            return;
        }

        // 判断前者内容是否为空
        if (EmptyUtil.isEmpty(source)) {
            logger.warn("source isEmpty！");
            return;
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
