package com.yuntun.sanitationkitchen.bean;


import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/**
 * @author yookfeng
 */
@Data
@ToString
public class VehicleBean {
  private String id;

  private Long carId;

  private String carName;

  private String carMileage;

  private Integer carAge;

  private Long driverId;

  private String driverName;

  private String driverPhone;

  private LocalDate contractTime;

  private LocalDateTime createTime;

  private Long gpsId;

  private Long fuelDeviceId;

  private Long speedDeviceId;

  private Long weighDeviceId;

  private String fenceIds;

  /**
   * 车辆类型id
   */
  private Long typeId;
  /**
   * 车辆类型详细信息
   */
  private VehicleType vehicleType;
  /**
   * 车辆是否在用的状态
   */
  private Integer status;
  /**
   * 车辆ID 注册到平台的Id
   */
  private String vehicleDeviceId;
  /**
   * 车辆电子围栏信息
   */
  private List<FenceBean> fences;

  /**
   * 车牌号
   */
  private String plate;

  /**
   * 车辆管理列表前端显示
   */
  private String vehicleDeviceStatusValue;


  /**
   * 设备号
   */
  private String terminalNo;
  /**
   * SIM号
   */
  private String sim;
  /**
   * 终端类型
   */
  private String terminalType;
  /**
   * 定位方式
   */
  private String isPos;
  /**
   * ACC状态
   */
  private String acc;
  /**
   * 传感器速度
   */
  private String sensorSpeed;
  /**
   * 温度
   */
  private String temperature;
  /**
   * 油量值
   */
  private String oil;
  /**
   * 刻度(电阻)
   */
  private String scale;
  /**
   * 格式化时间
   */
  private String formatTime;
  /**
   * 方向
   */
  private String direct;
  /**
   * 经度
   */
  private String lon;
  /**
   * 纬度
   */
  private String lat;
  /**
   * 设备时间
   */
  private String gpsTime;
  /**
   * 报警信息
   */
  private String alarmInfo;
  /**
   * 速度
   */
  private String speed;
  /**
   * 里程
   */
  private String mlileage;
  /**
   * 车辆平台状态
   */
  private Integer vehicleDeviceStatus;
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
   * 网络格式
   */
  private String netModel;
  /**
   * 正反转状态
   */
  private String turnDir;

  /**
   * 胎压信息
   */
  private List<TireBean> tireBeans;

//  private String id;           //   车辆ID
//  private String plate;    //  车牌号
//  private String terminalNo;   //   设备号
//  private String sim;          // SIM号
//  private String terminalType;//   终端类型
}
