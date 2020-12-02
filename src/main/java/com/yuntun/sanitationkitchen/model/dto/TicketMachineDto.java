package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

/**
 * <p>
 * 小票机
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class TicketMachineDto extends BasePageDto {

    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 设备名
     */
    private String deviceName;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备所属机构id
     */
    private Long sanitationOfficeId;

    /**
     * 设备状态0.在线 1.离线
     */
    private Integer status;

    /**
     * 型号
     */
    private String model;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 品牌
     */
    private String brand;

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