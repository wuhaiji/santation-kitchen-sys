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
 * 地磅表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class WeighbridgeListDto implements Serializable {

    private Integer pageSize;
    private Integer pageNo;

    private static final long serialVersionUID = 1L;

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
     * 创建时间
     */
    private LocalDateTime createTime;


}
