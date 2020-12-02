package com.yuntun.sanitationkitchen.model.code.code40000;

import com.yuntun.sanitationkitchen.model.code.ResultCode;

/**
 * <p>
 *  设备
 * </p>
 *
 * @author whj
 * @since 2020/11/10
 */
public enum VehicleTypeCode implements ResultCode {
    SAVE_ERROR("40200", "保存异常"),
    GET_ERROR("40202", "查询详情异常"),
    LIST_ERROR("40203", "查询列表异常"),
    OPTIONS_ERROR("40204", "查询选项列表"),
    UPDATE_ERROR("40205", "修改异常"),
    DELETE_ERROR("40206", "删除异常"),
    ID_NOT_EXIST("40206", "id不存在"),

    ;

    VehicleTypeCode(String resultCode, String resultMsg) {
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
