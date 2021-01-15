package com.yuntun.sanitationkitchen.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 车辆表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class VehicleGetVo {

    /**
     * uuid
     */
    private Long uid;

    /**
     * 车牌号
     */
    private String numberPlate;

    /**
     * 所属环卫所id
     */
    private Long sanitationOfficeId;

    private String sanitationOfficeName;

//    /**
//     * 司机名称
//     */
//    private String driverName;
//
//    /**
//     * 司机手机号
//     */
//    private String driverPhone;

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
    private LocalDate purchaseDate;

    /**
     * 类型id
     */
    private Long typeId;

    /**
     * 车辆rfid
     */
    private String rfid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 车辆皮重 kg
     */
    private Integer weight;
}
