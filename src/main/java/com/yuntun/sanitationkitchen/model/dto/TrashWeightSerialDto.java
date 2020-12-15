package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;

/**
 *
 * @author tang
 * @since 2020/12/15
 */
@Data
public class TrashWeightSerialDto extends BasePageDto {

    /**
     * 垃圾桶rfid
     */
    private String rfid;

    /**
     * 垃圾桶编号
     */
    private String code;

    /**
     * 餐馆名称
     */
    private String restaurantName;

}
