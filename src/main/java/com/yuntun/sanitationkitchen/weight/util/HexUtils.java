package com.yuntun.sanitationkitchen.weight.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wujihong
 */
public class HexUtils {

    public static Logger logger = LoggerFactory.getLogger(HexUtils.class);

    // 将16进制的字符串==》字节数组
    public static byte[] hexStringToByteArray(String s) {
        s = s.replaceAll(" ", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * 将字节数组 转为 ascii字符串
     * @author wujihong
     * @param data
     * @since 18:22 2020-11-12
     */
    public static String getTenBytes(byte[] data) throws Exception {
        int i = 0;
        char[] chars = new char[data.length];
        for (Byte b:data) {
            // 将字节转为10进制的整形并判断
            if (BitOperator.oneByteToInteger(b) > 128) {
                logger.error("字节转ascii错误！");
                throw new Exception("字节转ascii错误！");
            }else {
                chars[i] = (char) BitOperator.oneByteToInteger(b);
            }
            i++;
        }
        return new String(chars);
    }

}
