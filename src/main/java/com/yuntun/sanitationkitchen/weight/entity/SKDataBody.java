package com.yuntun.sanitationkitchen.weight.entity;

import lombok.Data;

import java.util.Set;

/**
 * 环卫设备发送的  数据体信息
 *
 * @author wujihong
 */
@Data
public class SKDataBody {

    private Set<String> epcs;

    private Double weight;

}
