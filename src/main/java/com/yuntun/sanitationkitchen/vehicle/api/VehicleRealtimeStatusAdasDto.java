package com.yuntun.sanitationkitchen.vehicle.api;

import cn.hutool.core.date.DateUtil;
import com.yuntun.sanitationkitchen.constant.DateConst;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 来源云根据车牌号查询车辆动态数据 ，返回数据对象
 * </p>
 *
 * @author whj
 * @since 2020/12/3
 */
@Data
@Accessors(chain = true)
public class VehicleRealtimeStatusAdasDto {
    /**
     * 车辆ID
     */
    String id;
    /**
     * 车牌号
     */
    String plate;
    /**
     * 设备号
     */
    String terminalNo;

    /**
     * 设备类型
     */
    String terminalType;

    /**
     * 定位方式，0：不定位，1：GPS
     */
    Integer isPos;

    /**
     * ACC状态 1：关 0：开
     */
    Integer acc;

    /**
     * sensorSpeed，默认值为0，单位km/h
     */
    Integer sensorSpeed;

    /**
     * 默认值为空字符串，单位度，温度
     */
    String temperature;

    /**
     * 油量值，默认值为0，单位升
     */
    String oil;

    /**
     * 刻度(电阻)默认值为0，计算油量值的刻度。例如100%对应油量值100L
     */
    String scale;

    /**
     * formatTime，格式化时间，车辆状态持续时间(例如：行驶10分钟)，默认从未上线
     */

    String formatTime;

    /**
     * 方向，设备从未上线，值默认为零
     */
    Double direct;

    /**
     * 经度，设备从未上线，值默认为零
     */
    Double lon;

    /**
     * 纬度
     */
    Double lat;

    /**
     * 设备时间，设备从未上线，值默认为空字符串
     */
    @DateTimeFormat(pattern = DateConst.dateTimePattern)
    String gpsTime;

    /**
     * 报警信息，多个报警之间以英文”,”分隔
     */
    String alarmInfo;

    /**
     * 速度设备从未上线，值默认为零。单位：km/h
     */
    Double speed;

    /**
     * 里程设备从未上线或设备没有里程，值为零。单位：m
     */
    Double mlileage;

    /**
     * 车辆状态,0：从未上线 1：行驶 2：停车 3：离线 4：服务到期
     */
    Integer vehicleStatus;

    /**
     * 近光灯信号,默认值为0，1开启近光灯
     */
    Integer lbSignal;

    /**
     * 远光灯信号默认值为0，1开启远光灯
     */
    Integer hbSignal;

    /**
     * rtlSignal右转向灯信号默认值为0，1开启右转向灯
     */
    String rtlSignal;

    /**
     * 左转向灯信号,默认值为0，1开启左转向灯
     */
    String ltlSignal;

    /**
     * 制动信号默认值为0，1制动信号
     */
    String brakeSignal;

    /**
     * 倒挡信号默认值为0，1倒挡信号
     */
    String RSignal;

    /**
     * 喇叭信号默认值为0，1喇叭信号
     */
    String hornSignal;

    /**
     * 空调信号,默认值为0，1空调信号
     */
    String acSignal;

    /**
     * 网络格式默认值为空字符串，2G/4G
     */
    Integer netModel;

    /**
     * 正反转状态1：正转 0：反转 -1：无正反转状态
     */
    Integer turnDir;

    /**
     * 胎压信息
     */
    List<TirePressureInfo> tirePressureInfo;

    @Data
    @Accessors(chain = true)
    public static class TirePressureInfo {
        /**
         * 轮胎编号默认值为空字符串
         */
        String tyreIdentifier;

        /**
         * 胎压默认值为空字符串，单位Kpa/Psi/Bar
         */
        String tyrePressure;

        /**
         * 轮胎温度,默认值为空字符串，单位℃/℉
         */
        String tyrePemperature;

        /**
         * 状态,默认值为0
         */
        String status;
        /**
         * 报警状态,默认值为0
         */
        String alarmStatus;

    }

}
