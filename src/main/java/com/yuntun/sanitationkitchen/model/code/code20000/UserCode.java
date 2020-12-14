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
public enum UserCode implements ResultCode {

    DETAIL_SYSUSER_FAILURE("20101", "查询用户详情异常"),
    ADD_SYSUSER_FAILURE("20102", "添加用户异常"),
    UPDATE_SYSUSER_FAILURE("20103", "修改用户异常"),
    DELETE_SYSUSER_FAILURE("20104", "删除用户异常"),
    LIST_SYSUSER_FAILURE("20105", "分页查询用户列表异常"),
    LOGIN_CAPTCHA_ERROR("20106", "图形验证码不正确,请重新获取"),
    LOGIN_FAILED_PUBLICKEY_INCORRECT("20107", "加密密钥不正确"),
    LOGIN_FAILED_USERNAME_INCORRECT("20108", "账号不存在"),
    LOGIN_FAILED_PASSWORD_INCORRECT("20109", "密码不正确"),
    LOGIN_EXCEPTION("20110", "验证账户时异常"),
    NOT_LOGGED_IN("20111", "未登录"),
    TOKEN_TIME_OUT("20112", "token过期"),
    REFRESH_TOKEN_EXPIRED("20113", "刷新token过期，请重新登录"),
    USERNAME_ALREADY_EXISTS("20114", "用户名已存在"),
    PHONE_NUMBER_ALREADY_EXISTS("20115", "手机号已存在"),
    LOGIN_OUT_ERROR("20116", "退出异常"),
    LOGIN_FAILED_ERROR_ACCOUNT_IS_DISABLED("20117", "账号被禁用"),
    PASSWORD_UPDATE_ERROR("20118", "密码修改异常"),
    DISABLED_FAILED("20119", "禁用用户失败"),
    USER_DOES_NOT_EXIST("20120", "用户不存在"),
    LIST_USER_PERMISSION_ERROR("20121", "查询用户权限列表异常"),
    LIST_USER_ROLE_ERROR("20122", "查询用户角色列表异常"),
    USER_OFFICE_ERROR("20123", "该用户还是环卫所管理员,不能更改其所属单位"),
    USER_NOT_NULL_ERROR("20124", "还有用户属于该单位,不能删除"),
    WIGHT_BRIGHT_NOT_NULL_ERROR("20125", "还有地磅属于该单位,不能删除"),
    VEHICLE_NOT_NULL_ERROR("20126", "还有车辆属于该单位,不能删除"),
    TICKET_MACHINE_NOT_NULL_ERROR("20127", "还有小票机属于该单位,不能删除")
    ;
    /**
     * 错误码
     */
    private final String resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    UserCode(String resultCode, String resultMsg) {
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
