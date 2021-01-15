package com.yuntun.sanitationkitchen.constant;

public enum VehicleStatus {
    neverOnline(0),
    driving(1),
    parking(2),
    offline(3),
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
