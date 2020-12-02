package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车载摄像头表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class VehicleCameraUpdateDto {

    private Long uid;
    /**
     * 设备名
     */
    private String deviceName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 所属车辆id
     */
    private Long vehicleId;

    /**
     * 设备所属机构id
     */
    private Long sanitationOfficeId;

    /**
     * 型号
     */
    private String model;

    /**
     * 品牌
     */
    private String brand;


}
