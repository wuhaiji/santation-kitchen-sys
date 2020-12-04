package com.yuntun.sanitationkitchen.model.vo;

import com.yuntun.sanitationkitchen.model.entity.SanitationOfficeValue;
import lombok.Data;
import java.util.List;

/**
 * @author wujihong
 */
@Data
public class SelectOptionVo {

    /**
     * 设备品牌
     */
    private List<String> brandList;

    /**
     * 设备型号
     */
    private List<String> modelList;

    /**
     * 环卫机构
     * 环卫Uid sanitationOfficeId
     * 环卫机构名字 sanitationOfficeName
     */
    private List<SanitationOfficeValue> sanitationOfficeList;
}
