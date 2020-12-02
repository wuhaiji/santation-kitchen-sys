package com.yuntun.sanitationkitchen.model.dto;

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
@Accessors(chain = true)
public class VehicleConfigUpdateDto {

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

}
