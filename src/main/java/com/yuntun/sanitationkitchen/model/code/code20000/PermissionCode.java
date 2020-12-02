package com.yuntun.sanitationkitchen.model.code.code20000;

import com.yuntun.sanitationkitchen.model.code.ResultCode;

/**
 * <p>
 *  权限
 * </p>
 *
 * @author whj
 * @since 2020/11/9
 */
public enum PermissionCode implements ResultCode {
    LIST_PAGE_PERMISSION_BY_USERID_ERROR("20501", "分页查询权限列表异常"),
    GET_ERROR("20502", "分页查询权限列表异常"),
    SAVE_ERROR("20503", "保存权限异常"),
    OPTIONS_ERROR("20506", "查询选项列表异常"),
    NAME_ALREADY_EXISTS_ERROR("20507", "权限名称已存在"),
    TAG_ALREADY_EXISTS_ERROR("20508", "权限标识已存在"),
    ;
    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    PermissionCode(String resultCode, String resultMsg) {
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
