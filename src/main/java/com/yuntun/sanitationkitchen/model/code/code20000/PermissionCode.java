package com.yuntun.sanitationkitchen.model.code.code20000;

import com.yuntun.sanitationkitchen.model.code.ResultCode;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/9
 */
public enum PermissionCode implements ResultCode {
    LIST_PAGE_PERMISSION_BY_USERID_ERROR("20501", "分页查询权限列表异常"),
    GET_PERMISSION_ERROR("20502", "分页查询权限列表异常"),
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
