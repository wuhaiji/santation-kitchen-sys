package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/30
 */
@Data
@Accessors(chain = true)
public class VehicleListDto {

    Integer pageSize;
    Integer pageNo;
    /**
     * uuid
     */
    private Long uid;

    /**
     * 称重公差值
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
