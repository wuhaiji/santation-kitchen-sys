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
