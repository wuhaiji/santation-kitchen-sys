package com.yuntun.sanitationkitchen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_vehicle_real_time_status")
public class VehicleRealTimeStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String adasId;

    private String plate;

    private String terminalNo;

    @TableField("terminalType")
    private String terminalType;

    @TableField("isPos")
    private Integer isPos;

    private Integer acc;

    @TableField("sensorSpeed")
    private Integer sensorSpeed;

    private String temperature;

    private String oil;

    private String scale;

    @TableField("formatTime")
    private String formatTime;

    private BigDecimal direct;

    private BigDecimal lon;

    private BigDecimal lat;

    @TableField("gpsTime")
    private LocalDateTime gpsTime;

    @TableField("alarmInfo")
    private String alarmInfo;

    private BigDecimal speed;

    private Integer mlileage;

    @TableField("vehicleStatus")
    private Integer vehicleStatus;

    @TableField("lbSignal")
    private Integer lbSignal;

    @TableField("hbSignal")
    private String hbSignal;

    @TableField("rtlSignal")
    private String rtlSignal;

    @TableField("ltlSignal")
    private String ltlSignal;

    @TableField("brakeSignal")
    private String brakeSignal;

    @TableField("RSignal")
    private String RSignal;

    @TableField("hornSignal")
    private String hornSignal;

    @TableField("acSignal")
    private String acSignal;

    @TableField("netModel")
    private Integer netModel;

    @TableField("turnDir")
    private String turnDir;

    private LocalDateTime createTime;


}
