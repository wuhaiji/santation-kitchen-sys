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
 * 车辆配置表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_vehicle_config")
public class VehicleConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 车俩最高车速 km/h
     */
    private Double speedMax;

    /**
     * 油耗监测频率 次/分钟
     */
    private Integer fuelFrequency;

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
