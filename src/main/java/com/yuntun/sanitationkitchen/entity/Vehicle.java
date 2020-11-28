package com.yuntun.sanitationkitchen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author tang
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_ss_vehicle")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long carId;

    private String carName;

    private String plateNo;

    private String carMileage;

    private Integer carAge;

    private Long driverId;

    private String driverName;

    private String driverPhone;

    private LocalDate contractTime;

    private LocalDateTime createTime;

    private Long gpsId;

    private Long fuelDeviceId;

    private Long speedDeviceId;

    private Long weighDeviceId;

    private String fenceIds;

    private Long typeId;
    /**
     * 车辆是否在用的状态
     */
    private Integer status;
    /**
     * 车辆ID 注册到平台的Id
     */
    private String vehicleDeviceId;


}
