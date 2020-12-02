package com.yuntun.sanitationkitchen.bean;

import lombok.Data;

/**
 * @author yookfeng 2020/8/17
 * @des
 **/
@Data
public class TireBean {

  /**
   * 轮胎编号
   */
  private String tyreIdentifier;
  /**
   * 胎压
   */
  private String tyrePressure;
  /**
   * 轮胎温度
   */
  private String tyrePemperature;
  /**
   * 状态
   */
  private String status;
  /**
   * 报警状态
   */
  private String alarmStatus;
}
