package com.yuntun.sanitationkitchen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 油耗监测设备表
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_sk_ticket_machine")
public class TicketMachine implements Serializable {

  private static final long serialVersionUID = 1L;

  @TableId(value = "id", type = IdType.AUTO)
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
   * 网络设备编号（特指DTU编号,最大11位---sim卡号）
   * SIM卡号（小票机绑定SIM卡号）
   */
  private String netDeviceCode;

  /**
   * 小票机的绑定设备类型 0：表示车辆 1：表示地磅
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
   * 唯一编号（两种情况） 1.垃圾桶的rfid--垃圾桶表的rfid 2.地磅或是DTU的编号--地磅表的
   */
  private String uniqueCode;

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


}
