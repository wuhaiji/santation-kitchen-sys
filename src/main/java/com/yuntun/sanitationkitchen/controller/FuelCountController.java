package com.yuntun.sanitationkitchen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.bean.VehicleBean;
import com.yuntun.sanitationkitchen.model.dto.FuelDeviceDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.vo.FuelCountListVo;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.vehicle.api.IVehicle;
import com.yuntun.sanitationkitchen.vehicle.api.VehicleApi;
import com.yuntun.sanitationkitchen.vehicle.api.VehicleRealtimeStatusAdasDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/2
 */

@RestController
@RequestMapping("/fuel/count")
public class FuelCountController {
    @Autowired
    IVehicle iVehicle;


    @Autowired
    IVehicleService iVehicleService;

    @Limit("fuelCount:list")
    @GetMapping("/list")
    public Result list(FuelDeviceDto dto) {
        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        //先从数据库查询所有车辆牌号
        IPage<Vehicle> vehicleIPage = iVehicleService.listPage(
                new VehicleListDto()
                        .setPageSize(dto.getPageSize())
                        .setPageNo(dto.getPageNo())
        );

        List<String> plates = ListUtil.listMap(String.class, vehicleIPage.getRecords());

        List<VehicleRealtimeStatusAdasDto> list = iVehicle.ListVehicleRealtimeStatusByIds(plates);

        List<FuelCountListVo> collect = list.parallelStream()
                .map(i ->
                        new FuelCountListVo()
                                .setFuelRemaining(i.getOil())
                                .setPlateNo(i.getPlate())
                                .setUpdateTime(LocalDateTime.now())
                )
                .collect(Collectors.toList());

        return Result.ok(collect);
    }
}
