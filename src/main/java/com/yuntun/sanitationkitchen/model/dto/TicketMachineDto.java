package com.yuntun.sanitationkitchen.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * <p>
 * 小票机
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class TicketMachineDto extends BasePageDto {

    private Integer id;

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
     * 设备所属机构id
     */
    private Long sanitationOfficeId;

    /**
     * 设备所属机构名
     */
//    private String sanitationOfficeName;

    /**
     * 小票机的绑定设备类型
     * 0：表示车辆
     * 1：表示地磅
     */
    private Integer type;

    /**
     * 设备状态0.在线 1.离线
     */
    private Integer status;

    /**
     * 型号
     */
    private String model;

    /**
     * 唯一编号（两种情况）
     * 1.垃圾桶的rfid--垃圾桶表的rfid
     * 2.地磅或是DTU的编号--地磅表的
     */
    private String uniqueCode;

    /**
     * 车牌号
     */
//    private String vehicleNumber;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 创建人id
     */
    private Long creator;

    /**
     * 创建时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "GMT+8")
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8", shape=JsonFormat.Shape.STRING)
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createTime;

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
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime disabledTime;

    /**
     * 修改者id
     */
    private Long updator;

    /**
     * 修改时间
     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime deletedTime;


}
