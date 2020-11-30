package com.yuntun.sanitationkitchen.model.code.code20000;

import com.yuntun.sanitationkitchen.model.code.ResultCode;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/5
 */
public enum UserRoleCode implements ResultCode {

    SAVE_ERROR("20201", "用户添加角色异常"),
    DELETE_ERROR("20202", "用户取消角色异常"),


    ;
    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    UserRoleCode(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


    @Override
    public String getCode() {
        return this.resultCode;
    }

    @Override
    public String getMsg() {
        return this.resultMsg;
    }
}
