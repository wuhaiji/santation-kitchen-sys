package com.yuntun.sanitationkitchen.weight.config;

/**
 * UDC数据头类型
 *
 * @author wujihong
 */
public class UDCDataHeaderType {

    // 标识位
    public static final int PACKAGE_SYMBOL = 0X7B;

    // 登录 数据包类型
    public static final int LOGIN_PACKAGE = 0X03;

    // 登录响应 数据包类型
    public static final int LOGIN_RESPONSE_PACKAGE = 0X83;

    // 心跳 数据包类型
    public static final int HEART_PACKAGE = 0X01;

    // 心跳响应 数据包类型
    public static final int HEART_RESPONSE_PACKAGE = 0X81;

    // 数据上报 数据包类型
    public static final int UPLOAD_PACKAGE = 0X09;

    // 上报响应 数据包类型
    public static final int UPLOAD_RESPONSE_PACKAGE = 0X85;

    // 数据下发 数据包类型
    public static final int SEND_PACKAGE = 0X89;

    // 主动下线 数据包类型
    public static final int OFFLINE_PACKAGE = 0X82;

    // 下线响应 数据包类型
    public static final int OFFLINE_RESPONSE_PACKAGE = 0X02;
}
