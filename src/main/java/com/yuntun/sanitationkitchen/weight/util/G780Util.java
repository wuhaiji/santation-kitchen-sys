package com.yuntun.sanitationkitchen.weight.util;

import com.yuntun.sanitationkitchen.weight.propertise.UDCDataPackageFormat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wujihong
 */
public class G780Util {

    @Autowired
    private UDCDataPackageFormat UDCDataPackageFormat;

    /**
     * 获取设备号：11字节
     *
     * @author wujihong
     * @param bytes
     * @since 2020-12-01 15:49
     */
    public byte[] getDeviceNumber(byte[] bytes) {
        Integer deviceNumberIndex = UDCDataPackageFormat.getDataHeader().getDeviceNumberIndex();
        Integer deviceNumberSize = UDCDataPackageFormat.getDataHeader().getDeviceNumberSize();

        byte[] deviceNumber = new byte[deviceNumberSize];
        System.arraycopy(bytes, deviceNumberIndex, deviceNumber,0, deviceNumberSize);
        return deviceNumber;
    }

    public static Integer getDataPackageLength(byte[] bytes) {
        return null;
    }
}
