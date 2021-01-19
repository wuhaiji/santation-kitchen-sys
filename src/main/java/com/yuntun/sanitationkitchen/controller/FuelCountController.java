package com.yuntun.sanitationkitchen.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleDayFuelCount;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.FuelCountListVo;
import com.yuntun.sanitationkitchen.service.IVehicleDayFuelCountService;
import com.yuntun.sanitationkitchen.service.IVehicleRealTimeStatusService;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ExcelUtil;
import com.yuntun.sanitationkitchen.vehicle.api.IVehicle;
import com.yuntun.sanitationkitchen.vehicle.api.VehicleRealtimeStatusAdasDto;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    @Autowired
    IVehicleDayFuelCountService iVehicleDayFuelCountService;

    private List<FuelCountListVo> getFuelCountListVos(List<Vehicle> records) {

        List<String> plates = records.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
        List<VehicleRealtimeStatusAdasDto> vehicleRealtimeStatusAdasDtoList = new ArrayList<>();
        if (plates.size() > 0) {
            vehicleRealtimeStatusAdasDtoList = iVehicle.ListVehicleRealtimeStatusByPlates(plates);
        }

        Map<String, VehicleRealtimeStatusAdasDto> statusMap = vehicleRealtimeStatusAdasDtoList
                .parallelStream()
                .collect(Collectors.toMap(VehicleRealtimeStatusAdasDto::getPlate, i -> i));

        //查询今日的油量消耗
        Map<String, VehicleDayFuelCount> vehicleDayFuelCountMap = iVehicleDayFuelCountService
                .list(
                        new LambdaQueryWrapper<VehicleDayFuelCount>()
                                .in(VehicleDayFuelCount::getPlate, plates)
                                .eq(VehicleDayFuelCount::getDate, LocalDate.now())
                )
                .parallelStream()
                .collect(Collectors.toMap(VehicleDayFuelCount::getPlate, i -> i));


        return records.parallelStream().map(i -> {
            VehicleRealtimeStatusAdasDto status = statusMap.get(i.getNumberPlate());
            VehicleDayFuelCount vehicleDayFuelCount = vehicleDayFuelCountMap.get(i.getNumberPlate());
            FuelCountListVo fuelCountListVo = new FuelCountListVo();
            fuelCountListVo
                    .setNumberPlate(i.getNumberPlate())
                    .setUpdateTime(LocalDateTime.now())
            ;
            if (EptUtil.isNotEmpty(status)) {
                fuelCountListVo.setFuelRemaining(status.getOil());
                if(status.getOil().equals("")){
                    fuelCountListVo.setFuelRemaining("0");
                }
            }else{
                fuelCountListVo.setFuelRemaining("0");
            }

            if (EptUtil.isNotEmpty(vehicleDayFuelCount)) {
                fuelCountListVo.setTodayConsume(vehicleDayFuelCount.getFuelConsume());
            }else{
                fuelCountListVo.setTodayConsume(0.0);
            }

            return fuelCountListVo;
        }).collect(Collectors.toList());
    }

    @Limit("data:fuelCount:query")
    @GetMapping("/list")
    public Result<Object> list(FuelCountListDto dto) {
        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());
        //先从数据库查询所有车辆牌号
        IPage<Vehicle> vehicleIPage = iVehicleService.listPage(
                new VehicleListDto()
                        .setPageNo(dto.getPageNo())
                        .setPageSize(dto.getPageSize())
                        .setNumberPlate(dto.getNumberPlate())
        );

        List<Vehicle> records = vehicleIPage.getRecords();


        List<FuelCountListVo> fuelCountListVos = getFuelCountListVos(records);

        RowData<FuelCountListVo> rowData = new RowData<FuelCountListVo>()
                .setRows(fuelCountListVos)
                .setTotal(vehicleIPage.getTotal())
                .setTotalPages(vehicleIPage.getTotal());
        return Result.ok(rowData);
    }

    @Limit("data:fuelCount:query")
    @GetMapping("/list/day/consume")
    public Result<?> listDayConsume(FuelCountListDto dto) {
        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());
        //先从数据库查询所有车辆牌号
        Page<VehicleDayFuelCount> page = iVehicleDayFuelCountService.page(
                new Page<VehicleDayFuelCount>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new LambdaQueryWrapper<VehicleDayFuelCount>()
                        .eq(EptUtil.isNotEmpty(dto.getNumberPlate()),VehicleDayFuelCount::getPlate, dto.getNumberPlate())
                        .eq(EptUtil.isNotEmpty(dto.getDate()),VehicleDayFuelCount::getDate, dto.getDate())
        );
        List<VehicleDayFuelCount> records = page.getRecords();
        RowData<VehicleDayFuelCount> rowData = new RowData<VehicleDayFuelCount>()
                .setRows(records)
                .setTotal(page.getTotal())
                .setTotalPages(page.getTotal());
        return Result.ok(rowData);
    }

    @Limit("data:fuelCount:export")
    @PostMapping("/export")
    public void export(FuelCountListDto dto, HttpServletResponse response) throws Exception {

        //先从数据库查询所有车辆牌号
        List<Vehicle> vehicles = iVehicleService.list(
                new QueryWrapper<Vehicle>()
                        .lambda()
                        .like(EptUtil.isNotEmpty(dto.getNumberPlate()), Vehicle::getNumberPlate, dto.getNumberPlate())
        );

        List<FuelCountListVo> fuelCountListVos = this.getFuelCountListVos(vehicles);

        String[] headers = {"序号", "车牌号", "更新时间", "油耗余量(L)", "油量消耗(L)"};
        String[] keys = {"number", "numberPlate", "updateTime", "fuelRemaining", "fuelConsume"};

        String sheetName = "油耗统计";

        ExcelUtil.excelExport(response, "油耗统计.xlsx", sheetName, fuelCountListVos, Arrays.asList(headers), Arrays.asList(keys));
    }

    // private List<FuelCountListVo> getFuelCountListVos(FuelCountListDto dto, List<Vehicle> records, List<String> plates) {
    //
    //
    //     List<VehicleRealTimeStatus> vehicleRealTimeStatuses = new ArrayList<>();
    //     if (plates.size() <= 0) {
    //         vehicleRealTimeStatuses = iVehicleRealTimeStatusService.list(
    //                 new QueryWrapper<VehicleRealTimeStatus>()
    //                         .in("plate", plates)
    //                         .ge(EptUtil.isNotEmpty(dto.getStartDate()), "create_time", dto.getStartDate())
    //                         .le(EptUtil.isNotEmpty(dto.getEndDate()), "create_time", dto.getEndDate())
    //                         .orderByAsc("create_time")
    //         );
    //     }
    //
    //
    //     //把车的状态的历史记录按车分类
    //     Map<String, List<VehicleRealTimeStatus>> vehicleRealTimeStatusesMap = vehicleRealTimeStatuses.parallelStream()
    //             .collect(Collectors.groupingBy(VehicleRealTimeStatus::getPlate));
    //
    //     //车辆统计数据
    //     HashMap<String, FuelCountListVo> allFuelConsumeMap = getStringFuelCountListVoHashMap(vehicleRealTimeStatuses, vehicleRealTimeStatusesMap);
    //
    //     return records
    //             .parallelStream()
    //             .map(i -> {
    //                 FuelCountListVo fuelCountListVo1 = new FuelCountListVo();
    //                 FuelCountListVo fuelCountListVo = allFuelConsumeMap.get(i.getNumberPlate());
    //                 if (fuelCountListVo != null) {
    //                     fuelCountListVo1
    //                             .setFuelRemaining(fuelCountListVo.getFuelRemaining())
    //                             .setNumberPlate(i.getNumberPlate())
    //                             .setUpdateTime(LocalDateTime.now())
    //                             .setFuelConsume(fuelCountListVo.getFuelConsume())
    //                     ;
    //                 } else {
    //                     fuelCountListVo1
    //                             .setNumberPlate(i.getNumberPlate())
    //                             .setFuelRemaining(String.valueOf(0))
    //                             .setUpdateTime(LocalDateTime.now())
    //                             .setFuelConsume(0D)
    //                     ;
    //                 }
    //                 return fuelCountListVo1;
    //             })
    //             .collect(Collectors.toList());
    // }
    //
    // private HashMap<String, FuelCountListVo> getStringFuelCountListVoHashMap(
    //         List<VehicleRealTimeStatus> vehicleRealTimeStatuses,
    //         Map<String, List<VehicleRealTimeStatus>> vehicleRealTimeStatusesMap
    // ) {
    //     //统计油量消耗
    //     HashMap<String, FuelCountListVo> allFuelConsumeMap = new HashMap<>();
    //     for (Map.Entry<String, List<VehicleRealTimeStatus>> entry : vehicleRealTimeStatusesMap.entrySet()) {
    //         List<VehicleRealTimeStatus> value = entry.getValue();
    //         double fuelConsume = 0;
    //         //
    //         double lastOil = 0;
    //         //剩余油量
    //
    //         //最新剩余油量
    //         String fuelRemaining = "0";
    //         if (EptUtil.isNotEmpty(vehicleRealTimeStatuses)) {
    //             String oil = vehicleRealTimeStatuses.get(vehicleRealTimeStatuses.size() - 1).getOil();
    //             if (EptUtil.isNotEmpty(oil)) {
    //                 fuelRemaining = oil;
    //             }
    //         }
    //
    //         //计算油量消耗
    //         for (VehicleRealTimeStatus vehicleRealTimeStatus : value) {
    //             String oil = vehicleRealTimeStatus.getOil();
    //             if (EptUtil.isEmpty(oil)) {
    //                 continue;
    //             }
    //             double oilDouble = Double.parseDouble(oil);
    //             if (oilDouble > lastOil) {
    //                 lastOil = oilDouble;
    //                 continue;
    //             }
    //             fuelConsume += (lastOil - oilDouble);
    //             lastOil = oilDouble;
    //         }
    //         allFuelConsumeMap.put(entry.getKey(), new FuelCountListVo().setFuelConsume(fuelConsume).setFuelRemaining(fuelRemaining));
    //     }
    //     return allFuelConsumeMap;
    // }
    @Data
    @Accessors(chain = true)
    public static class FuelCountListDto {

        Integer pageSize;
        Integer pageNo;
        LocalDate date;
        String numberPlate;
    }

}
