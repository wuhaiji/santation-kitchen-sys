package com.yuntun.sanitationkitchen.weight.entity;

import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import com.yuntun.sanitationkitchen.weight.util.UDCDataUtil;
import lombok.Data;

/**
 * @author wujihong
 */
@Data
public class G780Data {

    public static UDCDataUtil udcDataUtil = SpringUtil.getBean(UDCDataUtil.class);

    // 标识
    private Integer flag;

    // 数据包类型
    private Integer dataPackageType;

    // 设备号
    private String deviceNumber;

    // 设备IP
    private String IP;

    // 设备端口
    private Integer port;

    // rfid
    private String rfid;

    // 毛重
    private Double grossWeight;

//    // 皮重
//    private Double tare;

    public G780Data() {
    }

    public G780Data(byte[] bytes) {
        this.flag = udcDataUtil.getFlag(bytes);
        this.dataPackageType = udcDataUtil.getDataPackageType(bytes);
        this.deviceNumber = udcDataUtil.getDeviceNumber(bytes);
        this.rfid = udcDataUtil.getRFID(bytes);
        this.grossWeight = udcDataUtil.getGrossWeight(bytes);
    }
}
