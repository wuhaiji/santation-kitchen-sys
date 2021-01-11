package com.yuntun.sanitationkitchen.weight.util;

/**
 * @author wujihong
 */
public class XOR {

    public static byte[] getVerify(byte[] noVerify) {
        byte[] result = new byte[2];
        if (noVerify.length != 10) {
            return result;
        }
        result[0] = (byte)(noVerify[1] ^ noVerify[2] ^ noVerify[3] ^ noVerify[4]);
        result[1] = (byte)(noVerify[5] ^ noVerify[6] ^ noVerify[7] ^ noVerify[8]);

        return result;
    }
}
