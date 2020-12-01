package com.yuntun.sanitationkitchen.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
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
public class VehicleOptionsVo implements Serializable {

    /**
     * uuid
     */
    private Long uid;

    /**
     * 车牌
     */
    private String numberPlate;


}
