package com.yuntun.sanitationkitchen.bean;

import com.venton.ss.common.core.NetAreaPoint;
import lombok.Data;

@Data
public class VehicleLocationBean {
    //车牌号 待定
    private String carNo;

    //车辆位置
    private NetAreaPoint location;
}
