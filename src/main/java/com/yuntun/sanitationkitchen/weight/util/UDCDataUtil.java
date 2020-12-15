package com.yuntun.sanitationkitchen.weight.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.weight.propertise.UDCDataPackageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujihong
 */
@Component
public class UDCDataUtil {

    public static final String VEHICLE = "vehicle";

    public static final String TRASH = "trash";

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private TrashCanMapper trashCanMapper;

    @Autowired
    private UDCDataPackageFormat UDCDataPackageFormat;

    /**
     * 获取UDC标识：1字节
     *
     * @param bytes
     * @return
     */
    public byte[] getFlag(byte[] bytes) {
        Integer flagIndex = UDCDataPackageFormat.getFlag().getIndex();
        Integer flagSize = UDCDataPackageFormat.getFlag().getSize();

        byte[] flag = new byte[flagSize];
        System.arraycopy(bytes, flagIndex, flag,0, flagSize);
        return flag;
    }

    /**
     * 获取UDC协议数据包类型：1字节
     *
     * @param bytes
     * @return
     */
    public byte[] getDataPackageType(byte[] bytes) {
        Integer typeIndex = UDCDataPackageFormat.getDataHeader().getTypeIndex();
        Integer typeSize = UDCDataPackageFormat.getDataHeader().getTypeSize();

        byte[] type = new byte[typeSize];
        System.arraycopy(bytes, typeIndex, type,0, typeSize);
        return type;
    }

    /**
     * 获取UDC协议 数据头长度+首尾标识
     *
     * @param bytes
     * @return
     */
    public byte[] getDataPackageLength(byte[] bytes) {
        Integer lengthIndex = UDCDataPackageFormat.getDataHeader().getLengthIndex();
        Integer lengthSize = UDCDataPackageFormat.getDataHeader().getLengthSize();

        byte[] length = new byte[lengthSize];
        System.arraycopy(bytes, lengthIndex, length,0, lengthSize);
        return length;
    }

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

    public String getUDCDataType(String rfid) {
        if (rfid != null) {
            throw new ServiceException("rfid不能为空");
        }
        Integer vehicleCount = vehicleMapper.selectCount(new QueryWrapper<Vehicle>().eq("rfid", rfid));
        if (vehicleCount != null && vehicleCount != 0) {
            return VEHICLE;
        }
        Integer trashCanCount = trashCanMapper.selectCount(new QueryWrapper<TrashCan>().eq("rfid", rfid));
        if (trashCanCount != null && trashCanCount != 0) {
            return TRASH;
        }
        return null;
    }
}
