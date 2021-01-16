package com.yuntun.sanitationkitchen.weight.util;

import com.yuntun.sanitationkitchen.exception.ServiceException;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

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
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        if (sourceClass != targetClass) {
            throw new ServiceException("要求：两个参数为同一类型！");
        }
        Field[] fields = sourceClass.getDeclaredFields();
        String getMethodName;
        String setMethodName;

        for (Field field:fields) {
            getMethodName = getMethodName(field);
            Object targetObject = invokeGetMethod(getMethodName, targetClass, target);
            if (targetObject == null) {
                setMethodName = setMethodName(field);
                // 将source中的属性放入到target中
                Object sourceObject = invokeGetMethod(getMethodName, sourceClass, source);
                invokeSetMethod(setMethodName, targetClass, target, sourceObject);
            }

        }


    }

    public static Class<?>[] getMethodParameterTypes(Class objectClass, String methodName) {
        Class<?>[]  parameterTypes = null;
        Method[] declaredMethods = objectClass.getDeclaredMethods();
        for (Method method:declaredMethods) {
            if (method.getName().equals(methodName)) {
                parameterTypes = method.getParameterTypes();
            }
        }
        return parameterTypes;
    }

    public static List<String> FieldNames(Class objectClass) {
        Field[] fields = objectClass.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        String fieldName;
        for(int i=0;i<fields.length;i++){
            fieldName = fields[i].getName();
            System.out.println("fields["+i+"]:"+fields[i]+"--"+"fieldName:"+fieldName);
            fieldNames.add(fieldName);
        }
        return fieldNames;
    }

    public static String getMethodName(Field field) {
        String fieldName = field.getName();
        String firstLetter;
        String getMethodName;

        firstLetter = fieldName.substring(0,1).toUpperCase();
        getMethodName = "get"+firstLetter+fieldName.substring(1);
        System.out.println("getMethodName:"+getMethodName);
        return getMethodName;
    }

    public static String setMethodName(Field field) {
        String fieldName = field.getName();
        String firstLetter;
        String setMethodName;

        firstLetter = fieldName.substring(0,1).toUpperCase();
        setMethodName = "set"+firstLetter+fieldName.substring(1);
        System.out.println("setMethodName:"+setMethodName);
        return setMethodName;
    }

    public static <T> Object invokeMethod(String methodName, Class objectClass, T object) {
        Object returnValue = null;
        try {
            Method method = objectClass.getDeclaredMethod(methodName);
            returnValue = method.invoke(object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public static <T> Object invokeGetMethod(String getMethodName, Class objectClass, T object) {
        Object returnValue = null;
        try {
            Method method = objectClass.getDeclaredMethod(getMethodName);
            returnValue = method.invoke(object);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public static <T> void invokeSetMethod(String setMethodName, Class objectClass, T target, Object value) {
        Class<?>[] methodParameterTypes = getMethodParameterTypes(objectClass, setMethodName);
        try {
            Method method = objectClass.getDeclaredMethod(setMethodName, methodParameterTypes);
            method.invoke(target, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static List<String> getterMethodNames(Class sourceClass) {
        Field[] fields = sourceClass.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        String fieldName;
        String firstLetter;
        String memberPropertyMethodName;
        for(int i=0;i<fields.length;i++){
            fieldName = fields[i].getName();
            firstLetter = fieldName.substring(0,1).toUpperCase();
            System.out.println("fields["+i+"]:"+fields[i]+"--"+"fieldName:"+fieldName);
            memberPropertyMethodName = "get"+firstLetter+fieldName.substring(1);
            System.out.println("methodName:"+memberPropertyMethodName);
            fieldNames.add(memberPropertyMethodName);
        }
        return fieldNames;
    }

    public static List<String> setterMethodNames(Class sourceClass) {
        Field[] fields = sourceClass.getDeclaredFields();
        List<String> fieldNames = new ArrayList<>();
        String fieldName;
        String firstLetter;
        String memberPropertyMethodName;
        for(int i=0;i<fields.length;i++){
            fieldName = fields[i].getName();
            firstLetter = fieldName.substring(0,1).toUpperCase();
            System.out.println("fields["+i+"]:"+fields[i]+"--"+"fieldName:"+fieldName);
            memberPropertyMethodName = "set"+firstLetter+fieldName.substring(1);
            System.out.println("methodName:"+memberPropertyMethodName);
            fieldNames.add(memberPropertyMethodName);
        }
        return fieldNames;
    }

}
