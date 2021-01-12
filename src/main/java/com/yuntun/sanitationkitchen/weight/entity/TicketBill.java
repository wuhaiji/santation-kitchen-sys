package com.yuntun.sanitationkitchen.weight.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wujihong
 */
@Data
public class TicketBill {

    // DTU绑定的手机号
    private String cardNo;

    // 打印时间
    private LocalDateTime time;

    // 重量结果
    private String weight;

    // 驾驶人名字
    private String driverName;

    // 车牌号
    private String plateNo;

}
