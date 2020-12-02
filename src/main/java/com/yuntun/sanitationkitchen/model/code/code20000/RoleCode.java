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
public enum RoleCode implements ResultCode {

    GET_ERROR("20301", "查询详情异常"),
    ADD_ERROR("20302", "添加异常"),
    UPDATE_ERROR("20303", "修改异常"),
    DELETE_ERROR("20304", "删除异常"),
    LIST_ERROR("20305", "分页查询列表异常"),
    ROLE_NOT_EXIST("20306", "角色不存在"),
    OPTIONS_ERROR("20307", "查询选项列表异常"),
    NAME_ALREADY_EXISTS_ERROR("20308", "角色名称已存在"),

    ;
    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    RoleCode(String resultCode, String resultMsg) {
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
