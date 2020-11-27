package com.yuntun.sanitationkitchen.model.code;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/10
 */
public enum FileCode implements ResultCode {
    FILE_NOT_EXISTS_ERROR("20601", "文件错误!"),
    FILE_DELETE_ERROR("20602", "文件删除失败!"),


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
