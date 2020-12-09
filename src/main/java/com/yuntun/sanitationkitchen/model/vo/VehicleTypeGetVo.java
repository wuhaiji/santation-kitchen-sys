package com.yuntun.sanitationkitchen.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/8
 */
@Data
@Accessors(chain = true)
public class VehicleTypeGetVo {

    /**
     * uuid
     */
    private Long uid;

    /**
     * 名称
     */
    private String name;

    /**
     * 品牌
     */
    private String brand;
    /**
     * 型号
     */
    private String model;

    /**
     * 车辆特性
     */
    private String trait;

    /**
     * 创建人id
     */
    private Long creator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime createTime;


}
