package com.yuntun.sanitationkitchen.model.entity;

import lombok.Data;

/**
 * @author wujihong
 */
@Data
public class VehicleValue {

    /**
     * 车辆的uid vehicleId
     */
    private Long vehicleId;

    /**
     * 车辆车牌号 vehicleNumber
     */
    private String vehicleNumber;
}
