package com.yuntun.sanitationkitchen.weight.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

/**
 * <p>
 * 打印util
 * </p>
 *
 * @author whj
 * @since 2021/1/20
 */
public class PrintUtil {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public static final int PAYTYPE_VOICE_NULL = 2000;
    public static final int PAYTYPE_VOICE_ALI = 2001;
    public static final int PAYTYPE_VOICE_QQ = 2002;
    public static final int PAYTYPE_VOICE_WECHAT = 2003;
    public static final int PAYTYPE_VOICE_JD = 2004;
    public static final int PAYTYPE_VOICE_UNION = 2005;
    public static final int PAYTYPE_VOICE_DIY7570 = 7570;
    public static final int PAYTYPE_VOICE_DIY7571 = 7571;
    public static final int PAYTYPE_VOICE_DIY7572 = 7572;
    public static final int PAYTYPE_VOICE_DIY7573 = 7573;
    public static final int PAYTYPE_VOICE_DIY7574 = 7574;
    public static final int PAYTYPE_VOICE_DIY7575 = 7575;
    public static final int PAYTYPE_VOICE_DIY7576 = 7576;
    public static final int PAYTYPE_VOICE_DIY7577 = 7577;
    public static final int PAYTYPE_VOICE_DIY7578 = 7578;
    public static final int PAYTYPE_VOICE_DIY7579 = 7579;

    /**
     * 支付播报固定语音字节数组
     *
     * @param imeiStr  IMEI设备唯一标识
     * @param msgId    交易序列号，不大于32字节，保证唯一
     * @param moneyStr 播报金额，最多两位小数
     * @param payType  支付类型
     * @return
     */
    public static byte[] getStaticVoiceBytes(String imeiStr, String msgId, String moneyStr, int payType) {
        return getStaticVoiceStr(imeiStr, msgId, moneyStr, payType).getBytes();
    }

    /**
     * 支付播报固定语音字符串
     *
     * @param imeiStr  IMEI设备唯一标识
     * @param msgId    交易序列号，不大于32字节，保证唯一
     * @param moneyStr 播报金额，最多两位小数
     * @param payType  支付类型
     * @return
     */
    public static String getStaticVoiceStr(String imeiStr, String msgId, String moneyStr, int payType) {
        String str = imeiStr + "|1007|" + msgId + "|" + moneyStr + "|" + payType;
        return str;
    }

    /**
     * 获取打印内容，适用于云打印机
     *
     * @param printText   打印文本
     * @param pageCount   打印联数
     * @param encodingStr 编码方式，默认UTF-8
     * @return
     */
    public static byte[] getPrinterBytes(final String printText,
                                         final int pageCount, String encodingStr) {
        try {
            if (encodingStr.equals("")) {
                encodingStr = "UTF-8";
            }
            byte[] msgByte = printText.getBytes(encodingStr);
            // 消息数组
            final byte[] dataByte = new byte[msgByte.length + 9];
            dataByte[0] = 0x1E;
            dataByte[1] = 0x10;
            dataByte[2] = (byte) pageCount;// 打印多联
            // 有效数据长度
            final int len = dataByte.length - 5;
            dataByte[3] = (byte) (len >> 8);
            dataByte[4] = (byte) (len & 0xff);
            // 数据内容
            System.arraycopy(msgByte, 0, dataByte, 5, msgByte.length);
            // 标识字节
            dataByte[dataByte.length - 4] = 0x1b;
            dataByte[dataByte.length - 3] = 0x63;
            // 打印内容CRC校验
            final byte[] dtCRC = getCRC(msgByte);
            dataByte[dataByte.length - 2] = (byte) (dtCRC[0]);
            dataByte[dataByte.length - 1] = (byte) (dtCRC[1]);
            msgByte = dataByte;
            return msgByte;
        } catch (Exception ex) {
            log.error("Exception", ex);
        }
        return null;
    }

    public static byte[] getPrinterVoiceBytes(String printTxt, int pageCount, String encodingStr, String imeiStr, String msgId, String moneyStr, int payType) {
        byte[] voiceArray = getStaticVoiceBytes(imeiStr, msgId, moneyStr, payType);
        byte[] printerArray = getPrinterBytes(printTxt, pageCount, encodingStr);
        byte[] data = new byte[voiceArray.length + printerArray.length];
        System.arraycopy(printerArray, 0, data, 0, printerArray.length);
        System.arraycopy(voiceArray, 0, data, printerArray.length, voiceArray.length);
        return data;
    }

    private static int[] CRC16Table = {0x0000, 0x1189, 0x2312, 0x329b, 0x4624,
            0x57ad, 0x6536, 0x74bf, 0x8c48, 0x9dc1, 0xaf5a, 0xbed3, 0xca6c,
            0xdbe5, 0xe97e, 0xf8f7, 0x1081, 0x0108, 0x3393, 0x221a, 0x56a5,
            0x472c, 0x75b7, 0x643e, 0x9cc9, 0x8d40, 0xbfdb, 0xae52, 0xdaed,
            0xcb64, 0xf9ff, 0xe876, 0x2102, 0x308b, 0x0210, 0x1399, 0x6726,
            0x76af, 0x4434, 0x55bd, 0xad4a, 0xbcc3, 0x8e58, 0x9fd1, 0xeb6e,
            0xfae7, 0xc87c, 0xd9f5, 0x3183, 0x200a, 0x1291, 0x0318, 0x77a7,
            0x662e, 0x54b5, 0x453c, 0xbdcb, 0xac42, 0x9ed9, 0x8f50, 0xfbef,
            0xea66, 0xd8fd, 0xc974, 0x4204, 0x538d, 0x6116, 0x709f, 0x0420,
            0x15a9, 0x2732, 0x36bb, 0xce4c, 0xdfc5, 0xed5e, 0xfcd7, 0x8868,
            0x99e1, 0xab7a, 0xbaf3, 0x5285, 0x430c, 0x7197, 0x601e, 0x14a1,
            0x0528, 0x37b3, 0x263a, 0xdecd, 0xcf44, 0xfddf, 0xec56, 0x98e9,
            0x8960, 0xbbfb, 0xaa72, 0x6306, 0x728f, 0x4014, 0x519d, 0x2522,
            0x34ab, 0x0630, 0x17b9, 0xef4e, 0xfec7, 0xcc5c, 0xddd5, 0xa96a,
            0xb8e3, 0x8a78, 0x9bf1, 0x7387, 0x620e, 0x5095, 0x411c, 0x35a3,
            0x242a, 0x16b1, 0x0738, 0xffcf, 0xee46, 0xdcdd, 0xcd54, 0xb9eb,
            0xa862, 0x9af9, 0x8b70, 0x8408, 0x9581, 0xa71a, 0xb693, 0xc22c,
            0xd3a5, 0xe13e, 0xf0b7, 0x0840, 0x19c9, 0x2b52, 0x3adb, 0x4e64,
            0x5fed, 0x6d76, 0x7cff, 0x9489, 0x8500, 0xb79b, 0xa612, 0xd2ad,
            0xc324, 0xf1bf, 0xe036, 0x18c1, 0x0948, 0x3bd3, 0x2a5a, 0x5ee5,
            0x4f6c, 0x7df7, 0x6c7e, 0xa50a, 0xb483, 0x8618, 0x9791, 0xe32e,
            0xf2a7, 0xc03c, 0xd1b5, 0x2942, 0x38cb, 0x0a50, 0x1bd9, 0x6f66,
            0x7eef, 0x4c74, 0x5dfd, 0xb58b, 0xa402, 0x9699, 0x8710, 0xf3af,
            0xe226, 0xd0bd, 0xc134, 0x39c3, 0x284a, 0x1ad1, 0x0b58, 0x7fe7,
            0x6e6e, 0x5cf5, 0x4d7c, 0xc60c, 0xd785, 0xe51e, 0xf497, 0x8028,
            0x91a1, 0xa33a, 0xb2b3, 0x4a44, 0x5bcd, 0x6956, 0x78df, 0x0c60,
            0x1de9, 0x2f72, 0x3efb, 0xd68d, 0xc704, 0xf59f, 0xe416, 0x90a9,
            0x8120, 0xb3bb, 0xa232, 0x5ac5, 0x4b4c, 0x79d7, 0x685e, 0x1ce1,
            0x0d68, 0x3ff3, 0x2e7a, 0xe70e, 0xf687, 0xc41c, 0xd595, 0xa12a,
            0xb0a3, 0x8238, 0x93b1, 0x6b46, 0x7acf, 0x4854, 0x59dd, 0x2d62,
            0x3ceb, 0x0e70, 0x1ff9, 0xf78f, 0xe606, 0xd49d, 0xc514, 0xb1ab,
            0xa022, 0x92b9, 0x8330, 0x7bc7, 0x6a4e, 0x58d5, 0x495c, 0x3de3,
            0x2c6a, 0x1ef1, 0x0f78};

    private static byte[] getCRC(byte[] bytes) {
        int crc = 0xFFFF; // 初始值
        for (byte b : bytes) {
            crc = (crc >> 8) ^ CRC16Table[(crc ^ b) & 0xff];
        }
        byte[] b = new byte[2];
        b[0] = (byte) ((crc >> 8) ^ 0xff);
        b[1] = (byte) ((crc & 0xff) ^ 0xff);
        return b;
    }

    private static final char[] HEXES = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * byte数组 转换成 16进制小写字符串
     */
    public static String bytes2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(HEXES[(b >> 4) & 0x0F]);
            hex.append(HEXES[b & 0x0F]);
        }
        return hex.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // 发送打印小票机请求
        String whj = LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.CHINESE_DATE_TIME_PATTERN);
        String ticketInfo =
                "________________________________"
                        + "\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "卡号：kh123456" + "\r\n"
                        + "时间：" + whj + "\r\n"
                        + "时间：" + whj + "\r\n"
                        + "时间：" + whj + "\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "________________________________"
                        + "\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "\r\n"

                ;

        byte[] printerBytes = PrintUtil.getPrinterBytes(ticketInfo, 1, "");
        // for (byte printerByte : printerBytes) {
        //     System.out.print(printerByte);
        // }
        System.out.println(bytes2Hex(printerBytes));
    }
}
