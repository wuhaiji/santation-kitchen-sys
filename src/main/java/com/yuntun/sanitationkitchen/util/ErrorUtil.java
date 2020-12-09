package com.yuntun.sanitationkitchen.util;


import com.yuntun.sanitationkitchen.constant.CommonConstant;
import com.yuntun.sanitationkitchen.exception.ServiceException;

import java.util.Collection;

/**
 * <p>
 * 参数检查工具类
 * </p>
 *
 * @author whj
 * @since 2020/11/5
 */
public class ErrorUtil {

    /**
     * 参数检查， 目标不能为null
     *
     * @param object 目标对象
     * @param msg    异常信息
     */
    public static void isObjectNull(Object object, String msg) {
        if (object == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
    }

    /**
     * 参数检查， 目标对象内容不能为空
     *
     * @author wujihong
     * @param t
     * @param msg
     * @since 2020-12-09 16:08
     */
    public static <T> void isObjectNullContent(T t, String msg) {
        T instance;
        if (t == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        } else {
            try {
               instance = (T)t.getClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("参数错误");
            }
            if (t.equals(instance)) {
                throw new ServiceException("PARAM_ERROR", msg + "内容不能为空");
            }
        }
    }

    /**
     * 参数检查， 目标不能为null
     *
     * @param collection 目标集合
     * @param msg        异常信息
     */
    public static void isListEmpty(Collection collection, String msg) {
        if (collection == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (collection.size() <= 0) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
    }

    /**
     * 参数检查，字符串不能是空串
     *
     * @param string 目标字符串
     * @param msg    异常信息
     */
    public static void isStringEmpty(String string, String msg) {
        if (string == null || string.trim().equals("")) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
    }

    /**
     * 参数检查，字符串长度不能小于等于目标值
     *
     * @param string 目标字符串
     * @param i      目标值
     * @param msg    异常信息
     */
    public static void isStringGt(String string, int i, String msg) {
        if (string == null || string.trim().equals("")) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (string.length() < i) {
            throw new ServiceException("PARAM_ERROR", msg + "不能大于" + i);
        }
    }

    /**
     * 参数检查，字符串长度不能小于等于目标值
     *
     * @param string 目标字符串
     * @param i      目标值
     * @param msg    异常信息
     */
    public static void isStringLt(String string, int i, String msg) {
        if (string == null || string.trim().equals("")) {
            throw new ServiceException("PARAM_ERROR", msg + "长度不能为空");
        }
        if (string.length() < i) {
            throw new ServiceException("PARAM_ERROR", msg + "长度不能小于" + i);
        }
    }

    /**
     * 参数检查，字符串长度不能小于等于目标值
     *
     * @param string 目标字符串
     * @param i      目标值
     * @param msg    异常信息
     */
    public static void isStringLe(String string, int i, String msg) {
        if (string == null || string.trim().equals("")) {
            throw new ServiceException("PARAM_ERROR", msg + "长度不能为空");
        }
        if (string.length() <= i) {
            throw new ServiceException("PARAM_ERROR", msg + "长度不能小于等于" + i);
        }
    }

    /**
     * 参数检查，字符串长度不能小于等于目标值
     *
     * @param string 目标字符串
     * @param i      目标下限值
     * @param k      目标上限值
     * @param msg    异常信息
     */
    public static void isStringLengthOutOfRange(String string, int i, int k, String msg) {
        if (string == null || string.trim().equals("")) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (string.length() < i || string.length() > k) {
            throw new ServiceException("PARAM_ERROR", msg + "长度不能小于" + i + "或者大于" + k);
        }
    }


    /**
     * 异常检查，目标数值不能小于等于给定值
     *
     * @param integer 目标数值
     * @param i       给定值
     * @param msg     异常信息
     */
    public static void isNumberValueLe(Integer integer, int i, String msg) {
        if (integer == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (integer <= i) {
            throw new ServiceException("PARAM_ERROR", msg + "不能小于等于" + i);
        }
    }

    /**
     * 异常检查，目标数值不能小于等于给定值
     *
     * @param integer 目标数值
     * @param i       给定值
     * @param msg     异常信息
     */
    public static void isNumberValue(Integer integer, int i, String msg) {
        if (integer == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (integer <= i) {
            throw new ServiceException("PARAM_ERROR", msg + "不能小于等于" + i);
        }
    }

    /**
     * 异常检查，目标数值不能小于给定值
     *
     * @param integer 目标数值
     * @param i       给定值
     * @param msg     异常信息
     */
    public static void isNumberValueLt(Integer integer, int i, String msg) {
        if (integer == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (integer < i) {
            throw new ServiceException("PARAM_ERROR", msg + "不能小于" + i);
        }
    }

    /**
     * 参数检查，字符串长度不能小于等于目标值
     *
     * @param integer 目标值
     * @param i       目标下限值
     * @param k       目标上限值
     * @param msg     异常信息
     */
    public static void isNumberOutOfRange(Integer integer, int i, int k, String msg) {
        if (integer == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (integer < i || integer > k) {
            throw new ServiceException("PARAM_ERROR", msg + "长度不能小于" + i + "或者大于" + k);
        }
    }

    /**
     * 检查参数快捷方法
     * @param pageSize
     * @param pageNo
     */
    public static void PageParamError(Integer pageSize,Integer pageNo){
        ErrorUtil.isNumberOutOfRange(pageSize, 0, CommonConstant.PageSizeMax, "pageSize");
        ErrorUtil.isNumberValueLe(pageNo, 0, "pageNo");
    }
}
