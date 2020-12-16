package com.yuntun.sanitationkitchen.weight.util;

import com.yuntun.sanitationkitchen.weight.config.UDCDataHeaderType;
import com.yuntun.sanitationkitchen.weight.propertise.UDCDataPackageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wujihong
 */
public class UDCDataResponse {

    public static UDCDataUtil udcDataUtil = SpringUtil.getBean(UDCDataUtil.class);

    public static UDCDataPackageFormat udcDataPackageFormat = SpringUtil.getBean(UDCDataPackageFormat.class);

    /**
     * UDC的登录响应
     *
     * @param bytes
     * @return
     */
    public static byte[] loginResponse(byte[] bytes) {
        byte [] loginResponseBytes = response(bytes, UDCDataHeaderType.LOGIN_RESPONSE_PACKAGE);
        return loginResponseBytes;
    }

    /**
     * 心跳响应
     *
     * @param bytes
     * @return
     */
    public static byte[] heartResponse(byte[] bytes) {
        byte [] heartResponseBytes = response(bytes, UDCDataHeaderType.HEART_RESPONSE_PACKAGE);
        return heartResponseBytes;
    }

    /**
     * 数据上报响应
     *
     * @param bytes
     * @return
     */
    public static byte[] dataUploadResponse(byte[] bytes) {
        byte [] dataUploadResponseBytes = response(bytes, UDCDataHeaderType.UPLOAD_RESPONSE_PACKAGE);
        return dataUploadResponseBytes;
    }

    /**
     * 离线响应
     *
     * @param bytes
     * @return
     */
    public static byte[] offlineResponse(byte[] bytes) {
        byte [] offlineResponseBytes = response(bytes, UDCDataHeaderType.OFFLINE_RESPONSE_PACKAGE);
        return offlineResponseBytes;
    }


    /**
     * 根据指定请求头类型获取服务器响应设备数据
     *
     * @param bytes
     * @param headerType
     * @return
     */
    public static byte[] response (byte[] bytes, int headerType) {
        List<Byte> ResponseList = new ArrayList<>();
        // 1.开始标识位
        byte flag = (byte)udcDataUtil.getFlag(bytes);
        ResponseList.add(flag);

        // 2.数据头类型
        byte type = (byte)headerType;
        ResponseList.add(type);

        // 3.获取设备号
        byte[] deviceNumberBytes = udcDataUtil.getDeviceNumberBytes(bytes);

        // 4.数据包长度（减去数据体长度,但因为没有数据体）:两个*标识位(1)+数据头类型(1)+设备号(11)+数据包长度(2)
        Integer flagSize = udcDataPackageFormat.getFlag().getSize();
        Integer typeSize = udcDataPackageFormat.getDataHeader().getTypeSize();
        Integer deviceNumberSize = udcDataPackageFormat.getDataHeader().getDeviceNumberSize();
        Integer lengthSize = udcDataPackageFormat.getDataHeader().getLengthSize();

        Integer dataPackageLength = 2*flagSize+typeSize+deviceNumberSize+lengthSize;
        byte[] dataPackageBytes = BitOperator.integerTo2Bytes(dataPackageLength);
        byteArrayToList(ResponseList, dataPackageBytes);

        byteArrayToList(ResponseList, deviceNumberBytes);

        // 5.结束标识位
        ResponseList.add(flag);
        byte[] ResponseBytes = new byte[ResponseList.size()];

        for (int index = 0 ; index < ResponseList.size(); index++) {
            ResponseBytes[index] = ResponseList.get(index);
        }
        return ResponseBytes;
    }

    /**
     * 将bytes数组放入到list中
     *
     * @param list
     * @param bytes
     * @return
     */
    public static List<Byte> byteArrayToList(List<Byte> list, byte[] bytes) {
        for (Byte b:bytes) {
            list.add(b);
        }
        return list;
    }
}
