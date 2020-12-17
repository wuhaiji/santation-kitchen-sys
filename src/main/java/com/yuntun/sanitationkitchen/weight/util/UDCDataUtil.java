package com.yuntun.sanitationkitchen.weight.util;

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
     * 获取UDC协议 数据头长度+首尾标识长度
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
        // 获取数据头长度+首尾标识长度
        int dataPackageLength = getDataPackageLength(bytes);

        Integer dataBodyIndex = dataPackageLength-1;
        Integer dataBodySize = bytes.length-dataPackageLength;

        byte[] dataBody = new byte[dataBodySize];
        System.arraycopy(bytes, dataBodyIndex, dataBody,0, dataBodySize);

        return dataBody;
    }

    /**
     * 获取数据体的rfid号：13字节
     *
     * @param bytes
     * @return
     */
    public String getRFID(byte[] bytes) {
        byte[] dataBody = getDataBody(bytes);
        Integer RFIDSize = UDCDataPackageFormat.getDataBody().getRfidSize();

        byte[] RFID = new byte[RFIDSize];
        System.arraycopy(dataBody, 0, RFID,0, RFIDSize);

        return new String(RFID).trim();
    }

    /**
     * 获取毛重数据
     *
     * @param bytes
     * @return
     */
    public double getGrossWeight(byte[] bytes) {
        byte[] dataBody = getDataBody(bytes);
        Integer RFIDSize = UDCDataPackageFormat.getDataBody().getRfidSize();
        Integer grossWeightSize = UDCDataPackageFormat.getDataBody().getGrossWeight();

        byte[] grossWeight = new byte[grossWeightSize];
        System.arraycopy(dataBody, RFIDSize, grossWeight,0, grossWeightSize);

       return BitOperator.bytesToDouble(grossWeight);
    }

    /**
     * 获取皮重数据
     *
     * @param bytes
     * @return
     */
    public double getTare(byte[] bytes) {
        byte[] dataBody = getDataBody(bytes);
        Integer RFIDSize = UDCDataPackageFormat.getDataBody().getRfidSize();
        Integer grossWeightSize = UDCDataPackageFormat.getDataBody().getGrossWeight();
        Integer tareSize = UDCDataPackageFormat.getDataBody().getTare();

        byte[] tare = new byte[tareSize];
        System.arraycopy(dataBody, RFIDSize+grossWeightSize, tare,0, tareSize);

        return BitOperator.bytesToDouble(tare);
    }
}
