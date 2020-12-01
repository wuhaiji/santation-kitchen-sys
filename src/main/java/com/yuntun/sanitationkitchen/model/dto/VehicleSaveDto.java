package com.yuntun.sanitationkitchen.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 车辆表
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Data
@Accessors(chain = true)
public class VehicleSaveDto {
    /**
     * 车牌
     */
    private String numberPlate;

    /**
     * 所属环卫所id
     */
    private Long sanitationOfficeId;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 司机手机号
     */
    private String driverPhone;

    /**
     * 燃油余量
     */
    private Double fuelRemaining;

    /**
     * 状态0. 正常 1.离线
     */
    private Integer status;

    /**
     * 购买日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime purchaseDate;

    /**
     * 车辆rfid
     */
    private String rfid;

    /**
     * 车辆类型id
     */
    private Long typeId;

}
