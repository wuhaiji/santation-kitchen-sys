package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.VehicleDayFuelCountMapper;
import com.yuntun.sanitationkitchen.model.entity.VehicleDayFuelCount;
import com.yuntun.sanitationkitchen.service.IVehicleDayFuelServiceCount;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆每日油耗统计，服务类
 * </p>
 *
 * @author whj
 * @since 2021-01-18
 */
@Service
public class VehicleDayFuelServiceCountImpl extends ServiceImpl<VehicleDayFuelCountMapper, VehicleDayFuelCount> implements IVehicleDayFuelServiceCount {

}
