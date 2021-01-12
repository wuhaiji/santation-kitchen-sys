package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 地磅配置表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class PoundBillDto extends BasePageDto{

    private Integer id;

    /**
     * uuid
     */
    private Long uid;

    /**
     * 流水号
     */
    private String serialCode;

    /**
     * 环卫所id
     */
    private Long sanitationOfficeId;

    /**
     * 车牌
     */
    private String numberPlate;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 垃圾箱
     */
    private Long trashCanId;

    /**
     * 垃圾箱名称
     */
    private String trashCanCode;

//    /**
//     * 司机rfid
//     */
//    private String driverRfid;
//
//    /**
//     * 司机名称
//     */
//    private String driverName;

    /**
     * 毛重
     */
    private Double grossWeight;

    /**
     * 皮重
     */
    private Double tare;

    /**
     * 净重
     */
    private Double netWeight;

    /**
     * 创建人id
     */
    private Long creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 开始时间
     */
    private LocalDate beginTime;

    /**
     * 结束时间
     */
    private LocalDate endTime;

    /**
     * 禁用状态
     */
    private Integer disabled;

    /**
     * 禁用人id
     */
    private Long disabledBy;

    /**
     * 禁用时间
     */
    private LocalDateTime disabledTime;

    /**
     * 修改者id
     */
    private Long updator;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除状态
     */
    private Integer deleted;

    /**
     * 删除人
     */
    private Long deletedBy;

    /**
     * 删除时间
     */
    private LocalDateTime deletedTime;

    /**
     * excel 文件名
     */
    private String fileName;

    /**
     * excel 头部标题
     */
    private String title;

    /**
     * excel 头部标题
     */
    private List<String> headers;

    /**
     * excel版本
     * 2003  2007及以上
     */
    private String version;


}
