package com.yuntun.sanitationkitchen.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.experimental.Accessors;
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
public class TicketMachineVo {

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
     * 设备所属机构名字
     */
//    private String sanitationOfficeName;

    /**
     * 设备状态0.在线 1.离线
     */
    private Integer status;

    /**
     * 型号
     */
    private String model;

    /**
     * 车辆id
     */
    private Long vehicleId;

    /**
     * 车辆号码
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
//    @JsonFormat(pattern = DateConst.dateTimePattern)
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
//    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime disabledTime;

    /**
     * 修改者id
     */
    private Long updator;

    /**
     * 修改时间
     */
//    @JsonFormat(pattern = DateConst.dateTimePattern)
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
//    @JsonFormat(pattern = DateConst.dateTimePattern)
    private LocalDateTime deletedTime;


}
