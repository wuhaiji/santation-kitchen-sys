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
public enum PoundBillCode implements ResultCode {
    EXPORT_EXCEL("40900", "excel文件导出失败")
    ;

    PoundBillCode(String resultCode, String resultMsg) {
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
