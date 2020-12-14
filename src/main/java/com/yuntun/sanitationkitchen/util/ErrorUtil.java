package com.yuntun.sanitationkitchen.util;


import com.yuntun.sanitationkitchen.constant.CommonConstant;
import com.yuntun.sanitationkitchen.exception.ServiceException;

import java.util.Collection;
import java.util.regex.Pattern;

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
     * 手机号格式
     */
    private static final String PHONE_REGEX="^1[0-9]{10}$";

    /**
     * 验证手机号是否合法
     * 长度在2-16之间
     * @param obj   目标字符串
     */
    public static void notIllegalPhone(String obj) {
        if (!obj.matches(PHONE_REGEX) ){
            throw new ServiceException("PARAM_ERROR", "输入的手机号不合法!");
        }
    }

    /**
     *
     * Latitude:纬度
     * Longitude:经度
     */
    public static void verifyLatitudeAndLongitude(Double longitude, Double latitude) {
        if (longitude != null && latitude != null) {
            String longitudePattern = "^-?(([1-9]\\d?)|(1[1-7]\\d)|180)(\\.\\d{1,6})?$";
            String latitudePattern = "^-?(([1-8]\\d?)|([1-8]\\d)|90)(\\.\\d{1,6})?$";
            boolean isLongitudeMatches = Pattern.matches(longitudePattern, longitude.toString());
            boolean isLatitudeMatches = Pattern.matches(latitudePattern, latitude.toString());
            System.out.println("经度："+longitude.toString()+" 纬度："+latitude.toString());
            System.out.println("经度Matches："+isLongitudeMatches+" 纬度Matches："+isLatitudeMatches);
            if (!isLongitudeMatches) {
                throw new ServiceException("PARAM_ERROR", "经度整数部分为0-180,小数部分为0到6位!");
            }
            if (!isLatitudeMatches) {
                throw new ServiceException("PARAM_ERROR", "纬度整数部分为0-90,小数部分为0到6位!");
            }
        } else {
            throw new ServiceException("PARAM_ERROR", "经纬度不能为空");
        }

    }

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
                // 对象不存在无参构造函数,所以通过反射获取象失败！（因此，不存在对象内容为空）
                return;
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
    public static void isCollectionEmpty(Collection collection, String msg) {
        if (collection == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (collection.size() <= 0) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
    }

    /**
     * 参数检查， 目标不能为null
     *
     * @param array 目标集合
     * @param msg        异常信息
     */
    public static void isArrayEmpty(Object[] array, String msg) {
        if (array == null) {
            throw new ServiceException("PARAM_ERROR", msg + "不能为空");
        }
        if (array.length <= 0) {
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
     *
     * @param pageSize
     * @param pageNo
     */
    public static void PageParamError(Integer pageSize, Integer pageNo) {
        ErrorUtil.isNumberOutOfRange(pageSize, 0, CommonConstant.PageSizeMax, "pageSize");
        ErrorUtil.isNumberValueLe(pageNo, 0, "pageNo");
    }
}
