package com.yuntun.sanitationkitchen.model.code.code40000;

import com.yuntun.sanitationkitchen.model.code.ResultCode;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/10
 */
public enum VehicleCameraCode implements ResultCode {
    SAVE_ERROR("40400", "保存异常"),
    ID_NOT_EXIST("40402", "数据不存在"),
    LIST_ERROR("40403", "查询列表异常"),
    OPTIONS_ERROR("40404", "查询选项列表"),
    UPDATE_ERROR("40405", "修改异常"),
    DELETE_ERROR("40406", "删除异常"),
    DEVICE_CODE_ALREADY_EXISTS("40407", "设备编号已经存在"),

    ;

    VehicleCameraCode(String resultCode, String resultMsg) {
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
