package com.yuntun.sanitationkitchen.weight.response;

import com.yuntun.sanitationkitchen.weight.config.G780DataPackage;
import com.yuntun.sanitationkitchen.weight.util.BitOperator;

/**
 * @author wujihong
 */
public class TerminalResponse {

    // 登录响应(有人网的G780设备)
    // 0X03--0X83
    public static byte[] g780LoginResponse(byte[] deviceNumber) {
        byte[] bytes = new byte[16];
        // 标识位
        bytes[0] = BitOperator.integerTo1Byte(G780DataPackage.PACKAGE_SYMBOL);
        // 数据包类型
        bytes[1] = BitOperator.integerTo1Byte(G780DataPackage.LOGIN_RESPONSE_PACKAGE);

        // 数据包长度
        bytes[2] = 0;
        bytes[3] = 0x10;

        // 设备号
        System.arraycopy(deviceNumber, 0 ,bytes, 4,11);

        // 标识位
        bytes[15] = G780DataPackage.PACKAGE_SYMBOL;

        return bytes;
    }

}
