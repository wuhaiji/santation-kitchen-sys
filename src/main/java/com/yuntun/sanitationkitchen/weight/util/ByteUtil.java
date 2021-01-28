package com.yuntun.sanitationkitchen.weight.util;

/**
 * <p>
 * 字节工具类
 * </p>
 *
 * @author whj
 * @since 2021/1/20
 */
public class ByteUtil {
    /**
     * int 转 byte[]   低字节在前（低字节序）
     *
     * @param n 目标整型
     * @return 结果数组
     */
    public static byte[] intToLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }

    /**
     * int 转 byte[]   高字节在前（高字节序）
     *
     * @param n 目标整型
     * @return 结果数组
     */
    public static byte[] intToHH(int n) {
        byte[] b = new byte[4];
        b[3] = (byte) (n & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[0] = (byte) (n >> 24 & 0xff);
        return b;
    }


    /**
     * byte[] 转 int 低字节在前（低字节序）
     *
     * @param b 目标数组
     * @return 结果整型
     */
    public static int byteToIntLH(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << (i * 8);
        }
        return res;
    }

    /**
     * byte[] 转 int 高字节在前（高字节序）
     *
     * @param b
     * @return
     */
    public static int byteToIntHH(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res += (b[i] & 0xff) << ((3 - i) * 8);
        }
        return res;
    }

    /**
     * char 转 byte[]  低字节序
     *
     * @param c 目标char
     * @return 结果数组
     */
    public static byte[] charToLH(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) (c & 0xFF);
        b[1] = (byte) (c >> 8 & 0xff);
        return b;
    }


    /**
     * char 转 byte[]  高字节序
     *
     * @param c 目标char
     * @return 结果数组
     */
    public static byte[] charToHH(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) (c >> 8 & 0xff);
        b[1] = (byte) (c & 0xFF);
        return b;
    }


    /**
     * char 转 byte[]  低字节序
     * ‘& 0xff’ 取最低8位
     *
     * @param b 目标数组
     * @return 结果char
     */
    public static char byteToCharLH(byte[] b) {
        int res = 0;
        for (int i = 0; i < b.length; i++) {
            res= res|(b[i] & 0xff) << (i * 8);
        }
        return (char) res;
    }

    /**
     * char 转 byte[]  高字节序
     *
     * @param b 目标数组
     * @return 结果char
     */
    public static char byteToCharHH(byte[] b) {
        char c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        return c;
    }


    public static void main(String[] args) {
        char zi = '节';
        byte[] LHzi = charToLH(zi);
        byte[] HHzi = charToHH(zi);
        System.out.println(byteToCharLH(LHzi));
        System.out.println(byteToCharHH(HHzi));

    }

}
