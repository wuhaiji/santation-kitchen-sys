package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
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
@Accessors(chain = true)
public class WeighbridgeDto extends BasePageDto {

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
     * 设备状态0.在线 1.离线
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}