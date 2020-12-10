package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/1
 */
@Data
@Accessors(chain = true)
public class VehicleTypeUpdateDto {

    private Long Uid;
    /**
     * 名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 品牌
     */
    private String model;

    /**
     * 车辆特性
     */
    private String trait;

}
