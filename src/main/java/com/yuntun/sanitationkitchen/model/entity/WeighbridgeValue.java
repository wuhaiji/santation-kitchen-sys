package com.yuntun.sanitationkitchen.model.entity;

import lombok.Data;

/**
 * @author wujihong
 */
@Data
public class WeighbridgeValue {

    /**
     * 地磅设施uid
     * 对应weighbridge表的uid字段
     */
    private Long weighbridgeId;

    /**
     * 地磅名 weighbridgeName
     * 对应weighbridge表的device_name字段
     */
    private String weighbridgeName;

    /**
     * 地磅设施编号 weighbridgeCode
     * 对应weighbridge表的facility_code字段
     */
    private String weighbridgeCode;

}
