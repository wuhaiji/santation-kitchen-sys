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
 * 地磅表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_weighbridge")
public class Weighbridge implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 所属环卫所id
     */
    private Long sanitationOfficeId;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 型号
     */
    private String model;

    /**
     * 最大称重，单位kg
     */
    private Integer maxWeighing;

    /**
     * 地磅rfid
     */
    private String rfid;

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
