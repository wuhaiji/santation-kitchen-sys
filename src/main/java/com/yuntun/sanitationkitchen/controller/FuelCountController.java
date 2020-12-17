package com.yuntun.sanitationkitchen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleRealTimeStatus;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.FuelCountListVo;
import com.yuntun.sanitationkitchen.service.IVehicleRealTimeStatusService;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ExcelUtil;
import com.yuntun.sanitationkitchen.vehicle.api.IVehicle;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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


    @Autowired
    IVehicleRealTimeStatusService iVehicleRealTimeStatusService;

    // @Limit("data:fuelCount")
    // @GetMapping("/list")
    // public Result<Object> list(FuelCountListDto dto) {
    //     ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());
    //     //先从数据库查询所有车辆牌号
    //     IPage<Vehicle> vehicleIPage = iVehicleService.page(
    //             new Page<Vehicle>()
    //                     .setSize(dto.getPageSize())
    //                     .setCurrent(dto.getPageNo()),
    //             new QueryWrapper<Vehicle>()
    //                     .like(EptUtil.isNotEmpty(dto.getNumberPlate()), "number_plate", dto.getNumberPlate())
    //     );
    //
    //     List<Vehicle> records = vehicleIPage.getRecords();
    //
    //     List<String> plates = records.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
    //
    //     List<VehicleRealtimeStatusAdasDto> list = iVehicle.ListVehicleRealtimeStatusByPlates(plates);
    //     Map<String, VehicleRealtimeStatusAdasDto> realtimeStatusAdasDtoMap = list.parallelStream().collect(Collectors.toMap(VehicleRealtimeStatusAdasDto::getPlate, i -> i));
    //
    //     // List<FuelCountListVo> collect = list.parallelStream()
    //     //         .map(i ->
    //     //                 new FuelCountListVo()
    //     //                         .setFuelRemaining(EptUtil.isEmpty(i.getOil()) ? String.valueOf(0) : i.getOil())
    //     //                         .setNumberPlate(i.getPlate())
    //     //                         .setUpdateTime(LocalDateTime.now())
    //     //         )
    //     //         .collect(Collectors.toList());
    //     List<FuelCountListVo> collect = records
    //             .parallelStream()
    //             .map(i -> {
    //                 FuelCountListVo fuelCountListVo1 = new FuelCountListVo();
    //                 VehicleRealtimeStatusAdasDto vehicleRealtimeStatusAdasDto = realtimeStatusAdasDtoMap.get(i.getNumberPlate());
    //                 if (vehicleRealtimeStatusAdasDto != null) {
    //                     fuelCountListVo1
    //                             .setFuelRemaining(EptUtil.isEmpty(vehicleRealtimeStatusAdasDto.getOil()) ? String.valueOf(0) : vehicleRealtimeStatusAdasDto.getOil())
    //                             .setNumberPlate(vehicleRealtimeStatusAdasDto.getPlate())
    //                             .setUpdateTime(LocalDateTime.now());
    //                 } else {
    //                     fuelCountListVo1.setNumberPlate(i.getNumberPlate()).setFuelRemaining(String.valueOf(0)).setUpdateTime(LocalDateTime.now());
    //                 }
    //                 return fuelCountListVo1;
    //             })
    //             .collect(Collectors.toList());
    //
    //     RowData<FuelCountListVo> rowData = new RowData<FuelCountListVo>()
    //             .setRows(collect)
    //             .setTotal(vehicleIPage.getTotal())
    //             .setTotalPages(vehicleIPage.getTotal());
    //     return Result.ok(rowData);
    // }

    // @Limit("data:fuelCount")
    // @PostMapping("/export")
    // public void export(FuelCountListDto dto, HttpServletResponse response) throws Exception {
    //
    //     //先从数据库查询所有车辆牌号
    //     List<Vehicle> vehicles = iVehicleService.list(
    //             new QueryWrapper<Vehicle>()
    //                     .like(EptUtil.isNotEmpty(dto.getNumberPlate()), "number_plate", dto.getNumberPlate())
    //     );
    //
    //     List<String> plates = vehicles.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
    //
    //     List<VehicleRealtimeStatusAdasDto> vehicleRealtimeStatusAdasDtos = iVehicle.ListVehicleRealtimeStatusByPlates(plates);
    //     Map<String, VehicleRealtimeStatusAdasDto> vehicleMap = vehicleRealtimeStatusAdasDtos.parallelStream().collect(Collectors.toMap(i -> i.getPlate(), i -> i));
    //     List<FuelCountListVo> fuelCountListVos = vehicles.parallelStream().map(i -> {
    //         VehicleRealtimeStatusAdasDto vehicleRealtimeStatusAdasDto = vehicleMap.get(i.getNumberPlate());
    //         FuelCountListVo fuelCountListVo = new FuelCountListVo();
    //         if (vehicleRealtimeStatusAdasDto != null) {
    //             String oil = vehicleRealtimeStatusAdasDto.getOil();
    //             fuelCountListVo.setFuelRemaining(EptUtil.isEmpty(oil) ? String.valueOf(0) : oil)
    //                     .setNumberPlate(i.getNumberPlate())
    //             ;
    //         } else {
    //             fuelCountListVo.setFuelRemaining("0");
    //         }
    //         fuelCountListVo.setNumberPlate(i.getNumberPlate()).setUpdateTime(LocalDateTime.now());
    //         return fuelCountListVo;
    //     }).collect(Collectors.toList());
    //
    //
    //     String[] headers = {"序号", "车牌号", "更新时间", "油耗余量(单位L)"};
    //     String[] keys = {"number", "numberPlate", "updateTime", "fuelRemaining"};
    //
    //     ExcelUtil.excelExport(response, "油耗统计.xlsx", "油耗统计", fuelCountListVos, Arrays.asList(headers), Arrays.asList(keys));
    // }

    @Limit("data:fuelCount")
    @GetMapping("/list")
    public Result<Object> list(FuelCountListDto dto) {
        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());
        //先从数据库查询所有车辆牌号
        IPage<Vehicle> vehicleIPage = iVehicleService.listPage(
                new VehicleListDto()
                        .setPageNo(dto.getPageNo())
                        .setPageSize(dto.getPageSize())
                        .setNumberPlate(dto.getNumberPlate()
                )

        );

        List<Vehicle> records = vehicleIPage.getRecords();

        List<String> plates = records.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());

        List<FuelCountListVo> collect = getFuelCountListVos(dto, records, plates);

        RowData<FuelCountListVo> rowData = new RowData<FuelCountListVo>()
                .setRows(collect)
                .setTotal(vehicleIPage.getTotal())
                .setTotalPages(vehicleIPage.getTotal());
        return Result.ok(rowData);
    }

    private List<FuelCountListVo> getFuelCountListVos(FuelCountListDto dto, List<Vehicle> records, List<String> plates) {


        List<VehicleRealTimeStatus> vehicleRealTimeStatuses = new ArrayList<>();
        if(plates.size()<=0){
            vehicleRealTimeStatuses = iVehicleRealTimeStatusService.list(
                    new QueryWrapper<VehicleRealTimeStatus>()
                            .in("plate", plates)
                            .ge(EptUtil.isNotEmpty(dto.getStartDate()), "create_time", dto.getStartDate())
                            .le(EptUtil.isNotEmpty(dto.getEndDate()), "create_time", dto.getEndDate())
                            .orderByAsc("create_time")
            );
        }



        //把车的状态的历史记录按车分类
        Map<String, List<VehicleRealTimeStatus>> vehicleRealTimeStatusesMap = vehicleRealTimeStatuses.parallelStream().collect(Collectors.groupingBy(VehicleRealTimeStatus::getPlate));

        //车辆统计数据
        HashMap<String, FuelCountListVo> allFuelConsumeMap = getStringFuelCountListVoHashMap(vehicleRealTimeStatuses, vehicleRealTimeStatusesMap);

        List<FuelCountListVo> collect = records
                .parallelStream()
                .map(i -> {
                    FuelCountListVo fuelCountListVo1 = new FuelCountListVo();
                    FuelCountListVo fuelCountListVo = allFuelConsumeMap.get(i.getNumberPlate());
                    if (fuelCountListVo != null) {
                        fuelCountListVo1
                                .setFuelRemaining(fuelCountListVo.getFuelRemaining())
                                .setNumberPlate(i.getNumberPlate())
                                .setUpdateTime(LocalDateTime.now())
                                .setFuelConsume(fuelCountListVo.getFuelConsume())
                        ;
                    } else {
                        fuelCountListVo1
                                .setNumberPlate(i.getNumberPlate())
                                .setFuelRemaining(String.valueOf(0))
                                .setUpdateTime(LocalDateTime.now())
                                .setFuelConsume(0D)
                        ;
                    }
                    return fuelCountListVo1;
                })
                .collect(Collectors.toList());
        return collect;
    }

    private HashMap<String, FuelCountListVo> getStringFuelCountListVoHashMap(
            List<VehicleRealTimeStatus> vehicleRealTimeStatuses,
            Map<String, List<VehicleRealTimeStatus>> vehicleRealTimeStatusesMap
    ) {
        //统计油量消耗
        HashMap<String, FuelCountListVo> allFuelConsumeMap = new HashMap<>();
        for (Map.Entry<String, List<VehicleRealTimeStatus>> entry : vehicleRealTimeStatusesMap.entrySet()) {
            List<VehicleRealTimeStatus> value = entry.getValue();
            double fuelConsume = 0;
            double lastOil = 0;
            String fuelRemaining = "0";
            if (EptUtil.isNotEmpty(vehicleRealTimeStatuses)) {
                String oil = vehicleRealTimeStatuses.get(vehicleRealTimeStatuses.size() - 1).getOil();
                if (EptUtil.isNotEmpty(oil)) {
                    fuelRemaining = oil;
                }
            }

            for (VehicleRealTimeStatus vehicleRealTimeStatus : value) {
                String oil = vehicleRealTimeStatus.getOil();
                if (EptUtil.isEmpty(oil)) {
                    continue;
                }
                double oilDouble = Double.parseDouble(oil);
                if (oilDouble > lastOil) {
                    lastOil = oilDouble;
                    continue;
                }
                fuelConsume += (lastOil - oilDouble);
                lastOil = oilDouble;
            }
            allFuelConsumeMap.put(entry.getKey(), new FuelCountListVo().setFuelConsume(fuelConsume).setFuelRemaining(fuelRemaining));
        }
        return allFuelConsumeMap;
    }


    @Limit("data:fuelCount")
    @PostMapping("/export")
    public void export(FuelCountListDto dto, HttpServletResponse response) throws Exception {

        //先从数据库查询所有车辆牌号
        List<Vehicle> vehicles = iVehicleService.list(
                new QueryWrapper<Vehicle>()
                        .lambda()
                        .like(EptUtil.isNotEmpty(dto.getNumberPlate()), Vehicle::getNumberPlate, dto.getNumberPlate())
        );

        List<String> plates = vehicles.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());

        List<FuelCountListVo> fuelCountListVos = this.getFuelCountListVos(dto, vehicles, plates);


        String[] headers = {"序号", "车牌号", "更新时间", "油耗余量(L)", "油量消耗(L)"};
        String[] keys = {"number", "numberPlate", "updateTime", "fuelRemaining", "fuelConsume"};

        String sheetName = "油耗统计";
        if (dto.getEndDate() != null && dto.getStartDate() != null) {
            sheetName += ":" + dto.getStartDate() + "~" + dto.getEndDate();
        }

        ExcelUtil.excelExport(response, "油耗统计.xlsx", sheetName, fuelCountListVos, Arrays.asList(headers), Arrays.asList(keys));
    }

    @Data
    @Accessors(chain = true)
    public static class FuelCountListDto {

        Integer pageSize;
        Integer pageNo;

        LocalDate startDate;
        LocalDate endDate;

        String numberPlate;
    }
}
