package com.yuntun.sanitationkitchen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 垃圾桶称重流水表
 * </p>
 *
 * @author tang
 * @since 2020-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_trash_weight_serial")
public class TrashWeightSerial implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 垃圾桶RFID
     */
    private String trashCanRfid;

    /**
     * 垃圾桶编号
     */
    private String facilityCode;

    /**
     * 餐馆ID
     */
    private Long restaurantId;

    /**
     * 餐馆名称
     */
    private String restaurantName;

    /**
     * 司机rfid
     */
    private String driverRfid;
    /**
     * 司机rfid
     */
    private String driverName;

    /**
     * 重量
     */
    private Double weight;


    private LocalDateTime createTime;

}
