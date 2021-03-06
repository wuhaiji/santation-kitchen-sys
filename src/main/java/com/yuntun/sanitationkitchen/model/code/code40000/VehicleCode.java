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
    DELETE_BIND_ERROR("40107", "不能删除,该车绑定了小票机"),
    ID_NOT_EXIST("40108", "数据不存在"),
    NUMBER_PLATE_ALREADY_EXISTS("40109", "车牌号已存在"),
    RFID_PLATE_ALREADY_EXISTS("40110", "RFID号已存在"),
    IDS_TOO_MUCH("40111", "一次删除车辆不能大于100"),
    UNREGISTERED_IN_LYY("40111", "车辆应该在来源云平台注册"),

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
