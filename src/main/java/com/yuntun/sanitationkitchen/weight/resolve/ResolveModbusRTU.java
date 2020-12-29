package com.yuntun.sanitationkitchen.weight.resolve;

import com.yuntun.sanitationkitchen.weight.propertise.TrashDataPackageFormat;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import org.springframework.stereotype.Component;

/**
 * 垃圾桶Modbus-rtu 解析
 *
 * @author wujihong
 */
@Component
public class ResolveModbusRTU implements ResolveProtocol {

    public static TrashDataPackageFormat trashDataPackageFormat = SpringUtil.getBean(TrashDataPackageFormat.class);

    // 2.垃圾桶modbus-rtu 解析
    public void resolve(byte[] data) {

    }
}
