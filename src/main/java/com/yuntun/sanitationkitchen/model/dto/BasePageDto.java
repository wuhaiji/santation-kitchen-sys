package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;

/**
 * 分页
 * @author tcx
 */
@Data
public class BasePageDto {
    /**
     * 当前页码
     */
    private Integer pageNo;
    /**
     * 每页条数
     */
    private Integer pageSize;
}
