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
    public static byte[] response (byte[] bytes, int headerType, byte... dataBody) {
        List<Byte> ResponseList = new ArrayList<>();
        byte flag = (byte)udcDataUtil.getFlag(bytes);
        // 1.标识位
        ResponseList.add(flag);

        byte type = (byte)headerType;
        // 2.数据头类型
        ResponseList.add(type);

        // 数据包长度=两个*标识位(1)+数据头类型(1)+数据包表示长度(2)+设备号(11)+数据体(?)
        Integer flagSize = udcDataPackageFormat.getFlag().getSize();
        Integer typeSize = udcDataPackageFormat.getDataHeader().getTypeSize();
        Integer packageLengthSize = udcDataPackageFormat.getDataHeader().getPackageLengthSize();
        Integer deviceNumberSize = udcDataPackageFormat.getDataHeader().getDeviceNumberSize();
        Integer dataBodySize = dataBody.length;
        Integer dataPackageLength = 2*flagSize + typeSize + packageLengthSize + deviceNumberSize + dataBodySize;
        byte[] dataPackageBytes = BitOperator.integerTo2Bytes(dataPackageLength);
        // 3.数据包长度
        byteArrayToList(ResponseList, dataPackageBytes);

        byte[] deviceNumberBytes = udcDataUtil.getDeviceNumberBytes(bytes);
        // 4.获取设备号
        byteArrayToList(ResponseList, deviceNumberBytes);

        // 5.数据体
        byteArrayToList(ResponseList, dataBody);

        // 6.结束标识位
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
