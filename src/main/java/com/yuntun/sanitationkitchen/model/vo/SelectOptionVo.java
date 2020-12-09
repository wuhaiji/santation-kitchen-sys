package com.yuntun.sanitationkitchen.model.vo;

import com.yuntun.sanitationkitchen.model.entity.*;
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
//    private List<String> brandList;

    /**
     * 设备型号
     */
//    private List<String> modelList;

    /**
     * 车辆
     */
    private List<VehicleValue> vehicleList;

    /**
     * 垃圾桶
     */
    private List<TrashCanValue> trashCanList;

    /**
     * 餐馆
     */
    private List<RestaurantValue> restaurantList;

    /**
     * 环卫机构
     * 环卫Uid sanitationOfficeId
     * 环卫机构名字 sanitationOfficeName
     */
    private List<SanitationOfficeValue> sanitationOfficeList;
}
