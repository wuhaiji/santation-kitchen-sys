package com.yuntun.sanitationkitchen.model.entity;

import lombok.Data;

/**
 * @author wujihong
 */
@Data
public class TrashCanValue {


    /**
     * 垃圾桶的uid trashCanId
     */
    private Long trashCanId;

    /**
     * 垃圾桶编号 trashCanCode
     */
    private String trashCanCode;
}
