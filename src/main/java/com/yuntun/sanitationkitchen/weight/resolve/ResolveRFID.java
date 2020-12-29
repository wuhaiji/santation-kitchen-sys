package com.yuntun.sanitationkitchen.weight.resolve;

import com.yuntun.sanitationkitchen.weight.propertise.RFIDDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import com.yuntun.sanitationkitchen.weight.util.UDCDataUtil;

/**
 * rfid读卡器 解析
 *
 * @author wujihong
 */
public class ResolveRFID implements ResolveProtocol {

    public static RFIDDataPackageFormat rfidDataPackageFormat = SpringUtil.getBean(RFIDDataPackageFormat.class);

//    public static UDCDataUtil udcDataUtil = SpringUtil.getBean(UDCDataUtil.class);

    // 1.rfid读卡器 解析
    public void resolve(byte[] data) {
        if (data.length >= rfidDataPackageFormat.getRfidSize()) {

        }
    }
}
