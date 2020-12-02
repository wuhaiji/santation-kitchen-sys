package com.yuntun.sanitationkitchen.bean;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * @author yookfeng 2020/8/17
 * @des
 **/
@Data
public class AsdsBean {

  /**
   * 车辆ID
   */
  private String id;
  /**
   * 报警开始时间毫秒值
   */
  private String ast;
  /**
   * 报警结束时间毫秒值
   */
  private String aet;
  /**
   * 文件路径
   */
  private String filePath;
  /**
   * 文件系统地址
   */
  private String gfPath;


  /**
   * 车牌号
   */

  private String plate;
  /**
   * 设备号
   */
  private String terminalNo;
  /**
   * 设备类型
   */
  private String terminalType;
  /**
   * SIM号
   */
  private String sim;
  /**
   * 疲劳
   */
  private String tired;
  /**
   * 打电话
   */
  private String call;
  /**
   * 吸烟
   */
  private String smoke;
  /**
   * 劫警
   */
  private String robbery;
  /**
   * FCW前车碰撞预警
   */
  private String fcw;
  /**
   * 道路偏离
   */
  private String ldw;
  /**
   * 镜头遮挡
   */
  private String cover;
  /**
   * 闭眼
   */
  private String closedEyes;
  /**
   * 打哈欠
   */
  private String yawn;
  /**
   * 姿态异常
   */
  private String ap;
  /**
   * 前车近距
   */
  private String fcd;
  /**
   * 行人检测预警
   */
  private String pcw;
  /**
   * 不系安全带
   */
  private String sbo;
  /**
   * ADAS设备故障
   */
  private String adasError;
  /**
   * 急刹车
   */
  private String brake;
  /**
   * 离岗
   */
  private String leavePost;
  /**
   * 急加速
   */
  private String ra;
  /**
   * 急减速
   */
  private String rd;
  /**
   * 急左转弯
   */
  private String slt;

  /**
   * 急右转弯
   */
  private String srt;

  /**
   * 低头
   */
  private String bowDown;

  /**
   * 文件详情
   */
  private FileDetail fileDetail;

  /**
   * 异常驾驶类型
   */
  private String adasType;
  /**
   * 异常驾驶次数
   */
  private String adasSize;
  /***
   * 开始时间
   */
  private LocalDateTime startTime;
  /**
   * 结束时间
   */
  private LocalDateTime endTime;
}
