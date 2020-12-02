package com.yuntun.sanitationkitchen.bean;

import lombok.Data;

/**
 * @author yookfeng 2020/8/17
 * @des
 **/
@Data
public class MileBean {

  /**
   * 开始计时时间
   */
  private String beginTime;
  /**
   * 结束时间
   */
  private String endTime;
  /**
   * 开始纬度
   */
  private String latBegin;
  /**
   * 结束纬度
   */
  private String latEnd;
  /**
   * 开始经度
   */
  private String lonBegin;
  /**
   * 结束经度
   */
  private String lonEnd;
  /**
   * 开始里程
   */
  private String mileageBegin;
  /**
   * 结束里程
   */
  private String mileageEnd;
  /**
   * 车牌
   */
  private String plate;
  /**
   * 速度
   */
  private String speed;
  /**
   * 本次行程的持续里程，累积算法
   */
  private String thisMileage;
  /**
   * 本次行程的持续时间，秒
   */
  private String thisSecs;


}
