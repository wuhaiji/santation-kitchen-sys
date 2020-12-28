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
public enum DriverCode implements ResultCode {
    SAVE_ERROR("40801", "保存异常"),
    GET_ERROR("40802", "查询详情异常"),
    LIST_ERROR("40803", "查询列表异常"),
    OPTIONS_ERROR("40804", "查询选项列表"),
    UPDATE_ERROR("40805", "修改异常"),
    DELETE_ERROR("40806", "删除异常"),
    DELETE_BIND_ERROR("40807", "不能删除,该车绑定了小票机"),
    ID_NOT_EXIST("40808", "数据不存在"),
    PHONE_ALREADY_EXISTS("40809", "电话已被使用"),
    RFID_ALREADY_EXISTS("40810", "RFID已被使用"),


    ;


    DriverCode(String resultCode, String resultMsg) {
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
