package com.yuntun.sanitationkitchen.weight.util;

import com.yuntun.sanitationkitchen.weight.propertise.G780DataPackageFormat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wujihong
 */
public class G780Util {

    @Autowired
    private G780DataPackageFormat g780DataPackageFormat;

    /**
     * 获取设备号：11字节
     *
     * @author wujihong
     * @param bytes
     * @since 2020-12-01 15:49
     */
    public byte[] getDeviceNumber(byte[] bytes) {
        Integer deviceNumberIndex = g780DataPackageFormat.getG780DataHeader().getDeviceNumberIndex();
        Integer deviceNumberSize = g780DataPackageFormat.getG780DataHeader().getDeviceNumberSize();

        byte[] deviceNumber = new byte[deviceNumberSize];
        System.arraycopy(bytes, deviceNumberIndex, deviceNumber,0, deviceNumberSize);
        return deviceNumber;
    }

    public static Integer getDataPackageLength(byte[] bytes) {
        return null;
    }
}
