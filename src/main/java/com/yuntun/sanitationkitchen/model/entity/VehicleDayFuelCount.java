package com.yuntun.sanitationkitchen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 车辆表
 * </p>
 *
 * @author whj
 * @since 2021-01-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_vehicle_day_fuel_count")
public class VehicleDayFuelCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车牌号
     */
    private String plate;

    /**
     * 油耗消耗统计，日期
     */
    private LocalDate date;

    /**
     * 当天油耗剩余，L
     */
    private Double fuelRemaining;

    /**
     * 当天加油量，L
     */
    private Double fuelAdd;

    /**
     * 当天消耗量，L
     */
    private Double fuelConsume;


}
