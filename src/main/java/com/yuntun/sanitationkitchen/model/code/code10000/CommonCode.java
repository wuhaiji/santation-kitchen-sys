package com.yuntun.sanitationkitchen.model.code.code10000;

import com.yuntun.sanitationkitchen.model.code.ResultCode;

/**
 * 通用
 * 10000常用错误信息枚举
 * @author whj
 */
public enum CommonCode implements ResultCode {

    SUCCESS("10000", "成功!"),
    METHOD_NOT_SUPPORT("10001", "请求方法不支持!"),
    NETWORK_ANOMALY("10004", "网络异常!"),
    FREQUENT_OPERATION("10005", "操作频繁,请稍后再试"),
    PERMISSION_OPERATION("10006", "权限不足"),
    SERVER_ERROR("99999", "系统异常");


    CommonCode(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;


    @Override
    public String getCode() {
        return this.resultCode;
    }

    @Override
    public String getMsg() {
        return this.resultMsg;
    }
}
