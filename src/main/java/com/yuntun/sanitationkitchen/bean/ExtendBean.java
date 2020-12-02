package com.yuntun.sanitationkitchen.bean;

import lombok.Data;

/**
 * @author yookfeng 2020/8/17
 * @des
 **/
@Data
public class ExtendBean {


  /**
   * 近光灯信号
   */
  private String lbSignal;
  /**
   * 远光灯信号
   */
  private String hbSignal;
  /**
   * 右转向灯信号
   */
  private String rtlSignal;
  /**
   * 左转向灯信号
   */
  private String ltlSignal;
  /**
   * 制动信号
   */
  private String brakeSignal;
  /**
   * 倒挡信号
   */
  private String RSignal;
  /**
   * 喇叭信号
   */
  private String hornSignal;
  /**
   * 空调信号
   */
  private String acSignal;
  /**
   * 正反转状态
   */
  private String turnDir;
}
