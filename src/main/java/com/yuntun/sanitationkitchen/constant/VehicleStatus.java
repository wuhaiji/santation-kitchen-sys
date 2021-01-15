package com.yuntun.sanitationkitchen.constant;

public enum VehicleStatus {
    /**
     * 从未上线
     */
    neverOnline(0),
    /**
     * 行驶中
     */
    driving(1),
    /**
     * 停车中
     */
    parking(2),
    /**
     * 离线
     */
    offline(3),
    /**
     * 服务到期
     */
    Service_Expired(4),
    ;
    Integer status;

    public Integer value() {
        return status;
    }

    VehicleStatus(Integer status) {
        this.status = status;
    }
}
