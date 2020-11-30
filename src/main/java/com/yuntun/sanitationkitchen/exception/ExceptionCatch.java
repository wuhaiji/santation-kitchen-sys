package com.yuntun.sanitationkitchen.exception;

import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.code.ResultCode;
import com.yuntun.sanitationkitchen.model.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;


/**
 * <p>
 * 统一异常捕获类
 * </p>
 *
 * @author whj
 * @since 2020-09-08
 **/
@ControllerAdvice
public class ExceptionCatch {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    /**
     * 已知异常类型map
     */
    private static final HashMap<Class<? extends Throwable>, ResultCode> exceptionMap;

    static {
        exceptionMap = new HashMap<>();
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result<Object> ServiceException(ServiceException e) {
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result<Object> HttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException:", e);
        return Result.error(CommonCode.METHOD_NOT_SUPPORT.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> exception(Exception e) {
        log.error("Exception:", e);
        //判断异常是否有对应resultCode
        ResultCode resultCode = exceptionMap.get(e.getClass());
        if (resultCode != null)
            return Result.error(resultCode);
        return Result.error(CommonCode.SERVER_ERROR);
    }
}
