package com.yuntun.sanitationkitchen.model.vo;

import com.yuntun.sanitationkitchen.model.dto.BasePageDto;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class WeighbridgeVo {

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

//    /**
//     * 设施编号（可能是DTU编号、地磅编号）
//     */
//    private String facilityCode;

    /**
     * 网络设备编号（特指DTU编号）
     * 最大11位
     */
    private String netDeviceCode;

    /**
     * 设备状态0.在线 1.离线
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}
