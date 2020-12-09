package com.yuntun.sanitationkitchen.model.entity;

import lombok.Data;

/**
 * @author wujihong
 */
@Data
public class RestaurantValue {

    /**
     * 餐馆的uid restaurantId
     */
    private Long restaurantId;

    /**
     * 餐馆名 restaurantName
     */
    private String restaurantName;
}
