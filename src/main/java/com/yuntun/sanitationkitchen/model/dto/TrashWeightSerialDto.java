package com.yuntun.sanitationkitchen.model.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author tang
 * @since 2020/12/15
 */
@Data
@Accessors(chain = true)
public class TrashWeightSerialDto extends BasePageDto {

  /**
   * 垃圾桶rfid
   */
  private String rfid;

  /**
   * 垃圾桶编号
   */
  private String code;

  /**
   * 餐馆名称
   */
  private String restaurantName;

  /**
   * 司机rfid
   */
  private String driverRfid;

  /**
   * 司机名称
   */
  private String driverName;


  /**
   * 开始时间
   */
  private LocalDate beginTime;

  /**
   * 结束时间
   */
  private LocalDate endTime;


}
