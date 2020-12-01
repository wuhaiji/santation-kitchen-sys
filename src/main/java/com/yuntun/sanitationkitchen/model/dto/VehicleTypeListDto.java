package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/30
 */
@Data
@Accessors(chain = true)
public class VehicleTypeListDto {

    Integer pageSize;
    Integer pageNo;

    /**
     * 名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 车辆特性
     */
    private String trait;


}
