package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
public class VehicleTypeSaveDto {

    /**
     * 名称
     */
    private String name;

    /**
     * 品牌
     */
    private String model;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 车辆特性
     */
    private String trait;

}
