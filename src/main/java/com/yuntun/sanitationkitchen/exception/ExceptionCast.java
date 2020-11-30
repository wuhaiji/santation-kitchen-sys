package com.yuntun.sanitationkitchen.exception;


import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.code.ResultCode;
import com.yuntun.sanitationkitchen.model.response.Result;

/**
 * @author whj
 * @since 2020-09-08
 **/
public class ExceptionCast {
    public static void cast(ResultCode resultCode) {
        throw new ServiceException(resultCode);
    }

    /**
     * 处理feign http请求异常
     *
     * @param result
     */
    public static void feignCast(Result result) {
        //网络异常
        if (result == null) {
            throw new ServiceException(CommonCode.NETWORK_ANOMALY);
        }
        //业务异常
        if (!CommonCode.SUCCESS.getCode().equals(result.getCode())) {
            throw new ServiceException(result.getCode(), result.getMessage());
        }

    }

}
