package com.yuntun.sanitationkitchen.weight.entity;

import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Set;

/**
 * 环卫设备发送的  数据体信息
 *
 * @author wujihong
 */
@Data
public class SKDataBody {

    // epc
    private Set<String> epcs;

    // 垃圾桶重量
    private Double trashWeight;

    // 地磅称重 车辆
    private Double boundWeight;

}
