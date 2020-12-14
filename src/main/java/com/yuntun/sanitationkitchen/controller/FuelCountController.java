package com.yuntun.sanitationkitchen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.FuelCountListVo;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
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
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Limit("data:fuelCount")
    @GetMapping("/list")
    public Result<Object> list(FuelCountListDto dto) {
        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());
        //先从数据库查询所有车辆牌号
        IPage<Vehicle> vehicleIPage = iVehicleService.page(
                new Page<Vehicle>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<Vehicle>().eq(EptUtil.isNotEmpty(dto.getNumberPlate()), "number_plate", dto.getNumberPlate())
        );

        List<Vehicle> records = vehicleIPage.getRecords();

        List<String> plates = records.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());

        List<VehicleRealtimeStatusAdasDto> list = iVehicle.ListVehicleRealtimeStatusByPlates(plates);
        Map<String, VehicleRealtimeStatusAdasDto> realtimeStatusAdasDtoMap = list.parallelStream().collect(Collectors.toMap(VehicleRealtimeStatusAdasDto::getPlate, i -> i));

        // List<FuelCountListVo> collect = list.parallelStream()
        //         .map(i ->
        //                 new FuelCountListVo()
        //                         .setFuelRemaining(EptUtil.isEmpty(i.getOil()) ? String.valueOf(0) : i.getOil())
        //                         .setNumberPlate(i.getPlate())
        //                         .setUpdateTime(LocalDateTime.now())
        //         )
        //         .collect(Collectors.toList());
        List<FuelCountListVo> collect = records
                .parallelStream()
                .map(i -> {
                    FuelCountListVo fuelCountListVo1 = new FuelCountListVo();
                    VehicleRealtimeStatusAdasDto vehicleRealtimeStatusAdasDto = realtimeStatusAdasDtoMap.get(i.getNumberPlate());
                    if (vehicleRealtimeStatusAdasDto != null) {
                        fuelCountListVo1
                                .setFuelRemaining(EptUtil.isEmpty(vehicleRealtimeStatusAdasDto.getOil()) ? String.valueOf(0) : vehicleRealtimeStatusAdasDto.getOil())
                                .setNumberPlate(vehicleRealtimeStatusAdasDto.getPlate())
                                .setUpdateTime(LocalDateTime.now());
                    }else{
                        fuelCountListVo1.setNumberPlate(i.getNumberPlate()).setFuelRemaining(String.valueOf(0)).setUpdateTime(LocalDateTime.now());
                    }
                    return fuelCountListVo1;
                })
                .collect(Collectors.toList());

        RowData<FuelCountListVo> rowData = new RowData<FuelCountListVo>()
                .setRows(collect)
                .setTotal(vehicleIPage.getTotal())
                .setTotalPages(vehicleIPage.getTotal());
        return Result.ok(rowData);
    }


    // @Limit("data:fuelCount")
    // @PostMapping("/export")
    // public void export(FuelCountListDto dto, HttpServletResponse response) throws IOException {
    //
    //     //先从数据库查询所有车辆牌号
    //     List<Vehicle> vehicles = iVehicleService.list(
    //             new QueryWrapper<Vehicle>()
    //                     .eq(EptUtil.isNotEmpty(dto.getNumberPlate()), "number_plate", dto.getNumberPlate())
    //     );
    //
    //     List<String> plates = vehicles.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
    //
    //     List<VehicleRealtimeStatusAdasDto> list = iVehicle.ListVehicleRealtimeStatusByPlates(plates);
    //
    //     List<FuelCountListVo> fuelCountListVos = list.parallelStream()
    //             .map(i ->
    //                     new FuelCountListVo()
    //                             .setFuelRemaining(EptUtil.isEmpty(i.getOil()) ? String.valueOf(0) : i.getOil())
    //                             .setNumberPlate(i.getPlate())
    //                             .setUpdateTime(LocalDateTime.now())
    //             )
    //             .collect(Collectors.toList());
    //
    //
    //     // 通过工具类创建writer，默认创建xls格式
    //     ExcelWriter writer = ExcelUtil.getWriter();
    //     // writer.addHeaderAlias("numberPlate", "车牌号");
    //     // writer.addHeaderAlias("updateTime", "更新时间");
    //     // writer.addHeaderAlias("fuelRemaining", "燃油余量");
    //
    //     // 一次性写出内容，使用默认样式，强制输出标题
    //     writer.write(fuelCountListVos, true);
    //     //out为OutputStream，需要写出到的目标流
    //     //response为HttpServletResponse对象
    //     response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
    //     response.setHeader("Content-Disposition", "attachment;filename=test.xlsx");
    //
    //     ServletOutputStream out = response.getOutputStream();
    //     writer.flush(out, true);
    //     // 关闭writer，释放内存
    //     writer.close();
    //     //此处记得关闭输出Servlet流
    //     IoUtil.close(out);
    //
    // }

    @Limit("data:fuelCount")
    @PostMapping("/export")
    public void export(FuelCountListDto dto, HttpServletResponse response) throws IOException {

        //先从数据库查询所有车辆牌号
        List<Vehicle> vehicles = iVehicleService.list(
                new QueryWrapper<Vehicle>()
                        .eq(EptUtil.isNotEmpty(dto.getNumberPlate()), "number_plate", dto.getNumberPlate())
        );

        List<String> plates = vehicles.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());

        List<VehicleRealtimeStatusAdasDto> list = iVehicle.ListVehicleRealtimeStatusByPlates(plates);

        List<FuelCountListVo> fuelCountListVos = list.parallelStream()
                .map(i ->
                        new FuelCountListVo()
                                .setFuelRemaining(EptUtil.isEmpty(i.getOil()) ? String.valueOf(0) : i.getOil())
                                .setNumberPlate(i.getPlate())
                                .setUpdateTime(LocalDateTime.now())
                )
                .collect(Collectors.toList());


        String[] headers = {"车牌号", "更新时间", "油耗余量"};

        // excel导出

    }

    @Data
    @Accessors(chain = true)
    public static class FuelCountListDto {
        Integer pageSize;
        Integer pageNo;
        String numberPlate;
    }
}
