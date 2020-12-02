package com.yuntun.sanitationkitchen.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 车载摄像头表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class VehicleCameraOptionsVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 所属车辆id
     */
    private Long vehicleId;

}
