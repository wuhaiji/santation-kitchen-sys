package com.yuntun.sanitationkitchen.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
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


    private String sanitationOfficeName;

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
     * 0：从未上线 1：行驶 2：停车 3：离线 4：服务到期
     */
    private Integer status;

    /**
     * 购买日期
     */
    private LocalDate purchaseDate;

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
