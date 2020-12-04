package com.yuntun.sanitationkitchen.model.entity;

import lombok.Data;

/**
 * @author wujihong
 */
@Data
public class SanitationOfficeValue {

    /**
     * 环卫Uid sanitationOfficeId
     */
    private Long sanitationOfficeId;

    /**
     * 环卫机构名字 sanitationOfficeName
     */
    private String sanitationOfficeName;
}
