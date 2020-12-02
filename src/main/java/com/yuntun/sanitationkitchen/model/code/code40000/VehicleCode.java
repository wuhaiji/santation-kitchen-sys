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
public enum VehicleCode implements ResultCode {
    SAVE_ERROR("40100", "保存异常"),
    GET_ERROR("40102", "查询详情异常"),
    LIST_ERROR("40103", "查询列表异常"),
    OPTIONS_ERROR("40104", "查询选项列表"),
    UPDATE_ERROR("40105", "修改异常"),
    DELETE_ERROR("40106", "删除异常"),
    ID_NOT_EXIST("40106", "数据不存在"),
    NUMBER_PLATE_ALREADY_EXISTS("40106", "车牌号已存在"),
    RFID_PLATE_ALREADY_EXISTS("40106", "RFID号已存在"),

    ;


    VehicleCode(String resultCode, String resultMsg) {
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
