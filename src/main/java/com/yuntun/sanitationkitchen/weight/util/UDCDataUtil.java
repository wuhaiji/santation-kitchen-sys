package com.yuntun.sanitationkitchen.weight.util;

import com.yuntun.sanitationkitchen.weight.propertise.RFIDDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.propertise.UDCDataPackageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujihong
 */
@Component
public class UDCDataUtil {

    @Autowired
    private UDCDataPackageFormat UDCDataPackageFormat;

    @Autowired
    private RFIDDataPackageFormat rfidDataPackageFormat;


    /**
     * 获取UDC标识：1字节
     *
     * @param bytes
     * @return
     */
    public int getFlag(byte[] bytes) {
        Integer flagIndex = UDCDataPackageFormat.getFlag().getIndex();
        Integer flagSize = UDCDataPackageFormat.getFlag().getSize();

        byte[] flag = new byte[flagSize];
        System.arraycopy(bytes, flagIndex, flag,0, flagSize);

        return BitOperator.byteToInteger(flag);
    }

    /**
     * 获取UDC协议数据包类型：1字节
     *
     * @param bytes
     * @return
     */
    public int getDataPackageType(byte[] bytes) {
        Integer typeIndex = UDCDataPackageFormat.getDataHeader().getTypeIndex();
        Integer typeSize = UDCDataPackageFormat.getDataHeader().getTypeSize();

        byte[] type = new byte[typeSize];
        System.arraycopy(bytes, typeIndex, type,0, typeSize);

        return BitOperator.byteToInteger(type);
    }

    /**
     * 获取UDC协议 数据头长度+首尾标识长度+数据体
     *
     * @param bytes
     * @return
     */
    public int getDataPackageLength(byte[] bytes) {
        Integer lengthIndex = UDCDataPackageFormat.getDataHeader().getLengthIndex();
        Integer lengthSize = UDCDataPackageFormat.getDataHeader().getLengthSize();

        byte[] length = new byte[lengthSize];
        System.arraycopy(bytes, lengthIndex, length,0, lengthSize);

        return BitOperator.byteToInteger(length);
    }

    /**
     * 获取设备号：11字节
     *
     * @author wujihong
     * @param bytes
     * @since 2020-12-01 15:49
     */
    public byte[] getDeviceNumberBytes(byte[] bytes) {
        Integer deviceNumberIndex = UDCDataPackageFormat.getDataHeader().getDeviceNumberIndex();
        Integer deviceNumberSize = UDCDataPackageFormat.getDataHeader().getDeviceNumberSize();

        byte[] deviceNumber = new byte[deviceNumberSize];
        System.arraycopy(bytes, deviceNumberIndex, deviceNumber,0, deviceNumberSize);

        return deviceNumber;
    }

    /**
     * 获取设备号：11字节
     *
     * @author wujihong
     * @param bytes
     * @since 2020-12-01 15:49
     */
    public String getDeviceNumber(byte[] bytes) {
        Integer deviceNumberIndex = UDCDataPackageFormat.getDataHeader().getDeviceNumberIndex();
        Integer deviceNumberSize = UDCDataPackageFormat.getDataHeader().getDeviceNumberSize();

        byte[] deviceNumber = new byte[deviceNumberSize];
        System.arraycopy(bytes, deviceNumberIndex, deviceNumber,0, deviceNumberSize);

        return new String(deviceNumber).trim();
    }

    /**
     * 获取设备IP：4字节
     *
     * @param bytes
     * @return
     */
    public String getDeviceIP(byte[] bytes) {
        Integer IPIndex = UDCDataPackageFormat.getDataHeader().getIpIndex();
        Integer IPSize = UDCDataPackageFormat.getDataHeader().getIpSize();

        byte[] IP = new byte[IPSize];
        System.arraycopy(bytes, IPIndex, IP,0, IPSize);

        StringBuffer deviceIP = new StringBuffer();
        for (int index = 0; index < IP.length; index++) {
            deviceIP.append(IP[index]);
            if (index < IP.length-1) {
                deviceIP.append(".");
            }
        }
        return deviceIP.toString().trim();
    }

    /**
     * 获取设备IP的端口：2字节
     *
     * @param bytes
     * @return
     */
    public int getDevicePort(byte[] bytes) {
        Integer portIndex = UDCDataPackageFormat.getDataHeader().getIpIndex();
        Integer portSize = UDCDataPackageFormat.getDataHeader().getIpSize();

        byte[] port = new byte[portSize];
        System.arraycopy(bytes, portIndex, port,0, portSize);

        return BitOperator.byteToInteger(port);
    }

    /**
     * 获取数据体(字节)
     *
     * @param bytes
     * @return
     */
    public byte[] getDataBody(byte[] bytes) {
        // 获取数据包长度
        int dataPackageLength = getDataPackageLength(bytes);

        Integer dataBodyIndex = UDCDataPackageFormat.getDataBody().getIndex();
        Integer flagSize = UDCDataPackageFormat.getFlag().getSize();
        Integer dataHeaderSize = UDCDataPackageFormat.getDataHeader().getSize();
        // 数据体长度 = 数据包长度-2*标志位长度-数据头长度
        Integer dataBodySize = dataPackageLength-2*flagSize-dataHeaderSize;

        byte[] dataBody = new byte[dataBodySize];
        System.arraycopy(bytes, dataBodyIndex, dataBody,0, dataBodySize);

        return dataBody;
    }

//    /**
//     * 获取数据体的rfid
//     *
//     * @param bytes
//     * @return
//     */
//    public byte[] getRFID(byte[] bytes) {
//        byte[] dataBody = getDataBody(bytes);
//        Integer RFIDSize = rfidDataPackageFormat.getRfidSize();
//
//        byte[] RFID = new byte[RFIDSize];
//        System.arraycopy(dataBody, 0, RFID,0, RFIDSize);
//
//        return RFID;
//    }
//
//    /**
//     * 获取数据体的rfid的epc号：12字节(16进制字符串显示)
//     *
//     * @param bytes
//     * @return
//     */
//    public String getEPC(byte[] bytes) {
//        // 获取rfid
//        byte[] RFID = getRFID(bytes);
//        Integer epcIndex = rfidDataPackageFormat.getEpcIndex();
//        Integer epcSize = rfidDataPackageFormat.getEpcSize();
//
//        // 获取rfid的epc号
//        byte[] EPC = new byte[epcSize];
//        System.arraycopy(RFID, epcIndex, EPC,0, epcSize);
//
//        return BitOperator.byteArrayToHex(EPC).trim();
//    }

    /**
     * 获取毛重数据
     *
     * @param bytes
     * @return
     */
    public double getGrossWeight(byte[] bytes) {
        return Byte.valueOf("0");
    }

    /**
     * 获取皮重数据
     *
     * @param bytes
     * @return
     */
    public double getTare(byte[] bytes) {
        return Byte.valueOf("0");
    }
}
