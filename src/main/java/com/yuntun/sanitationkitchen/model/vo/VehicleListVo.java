package com.yuntun.sanitationkitchen.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class VehicleListVo implements Serializable {

    /**
     * uuid
     */
    private Long uid;

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
    private LocalDateTime purchaseDate;

    /**
     * 车辆rfid
     */
    private String rfid;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm")
    private LocalDateTime createTime;





}
