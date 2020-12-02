package com.yuntun.sanitationkitchen.controller;

import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.bean.VehicleBean;
import com.yuntun.sanitationkitchen.model.dto.FuelDeviceDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.vo.FuelCountListVo;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.vehicle.api.VehicleApi;
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
    VehicleApi vehicleApi;

    @Autowired
    IVehicleService iVehicleService;

    @Limit("fuelCount:list")
    @GetMapping("/list")
    public Result list(FuelDeviceDto fuelDeviceDto) {
        ErrorUtil.PageParamError(fuelDeviceDto.getPageSize(), fuelDeviceDto.getPageNo());

        List<VehicleBean> vehicleBeanList = vehicleApi.list();

        List<String> ids = vehicleBeanList.parallelStream()
                .map(i -> String.valueOf(i.getId()))
                .collect(Collectors.toList());

        List<VehicleBean> list = vehicleApi.getVehicleRealtimeData(ids);

        List<FuelCountListVo> collect = list.parallelStream()
                .map(i ->
                        new FuelCountListVo()
                                .setFuelRemaining(i.getOil())
                                .setPlateNo(i.getPlateNo())
                                .setUpdateTime(LocalDateTime.now())
                )
                .collect(Collectors.toList());

        return Result.ok(collect);
    }
}
