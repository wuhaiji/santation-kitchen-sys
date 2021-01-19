package com.yuntun.sanitationkitchen.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/2
 */
@Data
@Accessors(chain = true)
public class FuelCountListVo {

    /**
     * 车牌号
     */
    private String numberPlate;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime updateTime;
    /**
     * 油耗余量
     */
    private String fuelRemaining;

    /**
     * 油耗消耗
     */
    private Double fuelConsume;

    /**
     * 今日消耗
     */
    private Double todayConsume;
}
