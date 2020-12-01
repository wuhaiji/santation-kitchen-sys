package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.VehicleTypeMapper;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.service.IVehicleTypeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 车辆类型表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class VehicleTypeServiceImpl extends ServiceImpl<VehicleTypeMapper, VehicleType> implements IVehicleTypeService {

}
