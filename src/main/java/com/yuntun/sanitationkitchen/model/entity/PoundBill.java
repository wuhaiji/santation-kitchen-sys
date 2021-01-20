package com.yuntun.sanitationkitchen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_pound_bill")
public class PoundBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
     * 环卫所名称
     */
    private String sanitationOfficeName;

    /**
     * 车牌
     */
    private String numberPlate;

    /**
     * 司机rfid
     */
    private String driverRfid;

    /**
     * 司机名称
     */
    private String driverName;

    /**
     * 车辆id
     */
    private Long vehicleId;

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
