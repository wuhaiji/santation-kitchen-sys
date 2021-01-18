package com.yuntun.sanitationkitchen.weight.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射攻击类
 *
 * @author wujihong
 */
public class ReflexUtil {

    /**
     * 根据方法名，获取方法中的所有参数类型
     *
     * @param objectClass 类对象
     * @param methodName  方法名
     * @return
     */
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

    /**
     * 获取类对象的全部属性名
     *
     * @param objectClass 类对象
     * @return
     */
    public static List<String> getFieldNames(Class objectClass) {
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

    /**
     * 根据属性名，获取该属性的get方法
     *
     * @param fieldName 属性名
     * @return
     */
    public static String getterMethodName(String fieldName) {
        String firstLetter;
        String getMethodName;

        firstLetter = fieldName.substring(0,1).toUpperCase();
        getMethodName = "get"+firstLetter+fieldName.substring(1);
        System.out.println("getMethodName:"+getMethodName);
        return getMethodName;
    }

    /**
     * 根据属性名，获取该属性的set方法
     *
     * @param fieldName 属性名
     * @return
     */
    public static String setterMethodName(String fieldName) {
        String firstLetter;
        String setMethodName;

        firstLetter = fieldName.substring(0,1).toUpperCase();
        setMethodName = "set"+firstLetter+fieldName.substring(1);
        System.out.println("setMethodName:"+setMethodName);
        return setMethodName;
    }

    /**
     * 根据传入的参数，调用指定方法
     *
     * @param methodName  方法名
     * @param objectClass 类对象
     * @param object      源对象
     * @param value       传入的参数值（可选）
     * @param <T>         泛型
     * @return
     */
    public static <T> Object invokeMethod(String methodName, Class objectClass, T object, Object... value) {
        Object returnValue = null;
        Class<?>[] methodParameterTypes = getMethodParameterTypes(objectClass, methodName);
        try {
            Method method = objectClass.getDeclaredMethod(methodName, methodParameterTypes);
            returnValue = method.invoke(object, value);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    /**
     * 根据传入参数，调用指定的get方法
     *
     * @param getMethodName get方法名
     * @param objectClass   类对象
     * @param object        源对象
     * @param <T>           泛型
     * @return
     */
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

    /**
     * 根据传入参数，调用指定的set方法
     *
     * @param setMethodName set方法名
     * @param objectClass   类对象
     * @param target        目标对象
     * @param value         传入的参数值（可选）
     * @param <T>
     */
    public static <T> void invokeSetMethod(String setMethodName, Class objectClass, T target, Object... value) {
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

    /**
     * 根据指定的class对象，获取其所有的get方法
     *
     * @param objectClass 类对象
     * @return
     */
    public static List<String> getterMethodNames(Class objectClass) {
        Field[] fields = objectClass.getDeclaredFields();
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

    /**
     * 根据指定的class对象，获取其所有的set方法
     *
     * @param objectClass 类对象
     * @return
     */
    public static List<String> setterMethodNames(Class objectClass) {
        Field[] fields = objectClass.getDeclaredFields();
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
