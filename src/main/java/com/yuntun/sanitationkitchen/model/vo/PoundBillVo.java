package com.yuntun.sanitationkitchen.model.vo;

import com.yuntun.sanitationkitchen.model.dto.BasePageDto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 地磅配置表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class PoundBillVo extends BasePageDto {

    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 流水号
     */
    private String serialCode;

    /**
     * 环卫所id
     */
    private Long sanitationOfficeId;

    /**
     * 环卫所name
     */
    private String sanitationOfficeName;

    /**
     * 车牌
     */
    private String numberPlate;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 司机rfid
     */
    private String driverRfid;


    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 垃圾箱
     */
    private Long trashCanId;

    /**
     * 垃圾箱名称
     */
    private String trashCanCode;

    /**
     * 毛重
     */
    private Double grossWeight;

    /**
     * 皮重
     */
    private Double tare;

    /**
     * 净重
     */
    private Double netWeight;

    /**
     * 创建人id
     */
    private Long creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 禁用人id
     */
    private Long disabledBy;

    /**
     * 禁用时间
     */
    private LocalDateTime disabledTime;

    /**
     * 修改者id
     */
    private Long updator;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除状态
     */
    private Integer deleted;

    /**
     * 删除人
     */
    private Long deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;


}
