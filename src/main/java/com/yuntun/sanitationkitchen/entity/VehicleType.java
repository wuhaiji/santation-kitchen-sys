package com.yuntun.sanitationkitchen.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author tang
 * @since 2020-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_ss_vehicle_type")
public class VehicleType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long typeId;

    private String typeName;

    private String typeCode;

    private String producerName;

    private String carFeature;

    private LocalDateTime typeCreateTime;

    private Long typeCreateId;

    private String typeCreater;


}
