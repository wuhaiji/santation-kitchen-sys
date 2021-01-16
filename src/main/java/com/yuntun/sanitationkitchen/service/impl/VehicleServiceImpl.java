package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.util.EptUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * <p>
 * 车辆表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements IVehicleService {

    @Override
    public IPage<Vehicle> listPage(VehicleListDto dto) {
        return this.page(
                new Page<Vehicle>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<Vehicle>()
//                        .like(EptUtil.isNotEmpty(dto.getDriverName()), "driver_name", dto.getDriverName())
                        .like(EptUtil.isNotEmpty(dto.getNumberPlate()), "number_plate", dto.getNumberPlate())
//                        .like(EptUtil.isNotEmpty(dto.getDriverPhone()), "driver_phone", dto.getDriverPhone())
                        .likeRight(EptUtil.isNotEmpty(dto.getPurchaseDate()), "purchase_date",dto.getPurchaseDate())
                        .eq(EptUtil.isNotEmpty(dto.getSanitationOfficeId()), "sanitation_office_Id", dto.getSanitationOfficeId())
                        .orderByDesc("create_time")
        );
    }
}
