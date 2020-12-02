package com.yuntun.sanitationkitchen.model.code.code30000;

import com.yuntun.sanitationkitchen.model.code.ResultCode;

/**
 * <p>
 *  文件
 * </p>
 *
 * @author whj
 * @since 2020/11/10
 */
public enum FileCode implements ResultCode {
    FILE_NOT_EXISTS_ERROR("30101", "文件错误!"),
    FILE_DELETE_ERROR("30102", "文件删除失败!"),


    ;

    FileCode(String resultCode, String resultMsg) {
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
