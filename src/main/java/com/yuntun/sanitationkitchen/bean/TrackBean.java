package com.yuntun.sanitationkitchen.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yookfeng 2020/8/17
 * @ 车辆轨迹
 **/
@Data
@Accessors(chain = true)
public class TrackBean {

  /**
   * 设备时间
   */
  private String devTime;
  /**
   * 经度
   */
  private Double lon;

  /**
   * 纬度
   */
  private Double lat;
  /**
   * 是否补传数据
   */
  private String isRealTime;
  /**
   * 方向
   */
  private String direct;
  /**
   * 里程
   */
  private String mileage;
  /**
   * Acc状态
   */
  private String isAcc;
  /**
   * 速度
   */
  private Double speed;

  /**
   * 定位状态
   */
  private String isPos;
  /**
   * 停车时长
   */
  private String stopTime;
  /**
   * 行停状态
   */
  private String stopState;
  /**
   * 油量
   */
  private String oil;
  /**
   * 传感器速度
   */
  private String sensorSpeed;
  /**
   * 温度
   */
  private String temperature;
  /**
   * 报警
   */
  private String alarm;

  /**
   * 刻度(电阻)
   */
  private String scale;

  /**
   * 扩展数据
   */
  private ExtendBean extend;


}
