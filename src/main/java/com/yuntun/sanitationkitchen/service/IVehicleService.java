package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 车辆表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
public interface IVehicleService extends IService<Vehicle> {

    IPage<Vehicle> listPage(VehicleListDto dto);

}
