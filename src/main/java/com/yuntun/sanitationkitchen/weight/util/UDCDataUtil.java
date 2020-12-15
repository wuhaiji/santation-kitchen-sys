package com.yuntun.sanitationkitchen.weight.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wujihong
 */
@Component
public class UDCDataUtil {

    public static final String VEHICLE = "vehicle";

    public static final String TRASH = "trash";

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private TrashCanMapper trashCanMapper;

    public String getUDCDataType(String rfid) {
        if (rfid != null) {
            throw new ServiceException("rfid不能为空");
        }
        Integer vehicleCount = vehicleMapper.selectCount(new QueryWrapper<Vehicle>().eq("rfid", rfid));
        if (vehicleCount != null && vehicleCount != 0) {
            return VEHICLE;
        }
        Integer trashCanCount = trashCanMapper.selectCount(new QueryWrapper<TrashCan>().eq("rfid", rfid));
        if (trashCanCount != null && trashCanCount != 0) {
            return TRASH;
        }
        return null;
    }
}
