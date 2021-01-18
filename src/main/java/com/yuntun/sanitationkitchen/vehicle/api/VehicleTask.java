package com.yuntun.sanitationkitchen.vehicle.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuntun.sanitationkitchen.config.Scheduled.ScheduledTask;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleDayFuelCount;
import com.yuntun.sanitationkitchen.model.entity.VehicleRealTimeStatus;
import com.yuntun.sanitationkitchen.service.IVehicleDayFuelCountService;
import com.yuntun.sanitationkitchen.service.IVehicleRealTimeStatusService;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/15
 */
@Component
public class VehicleTask implements ScheduledTask {
    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    @Autowired
    IVehicleService iVehicleService;

    @Autowired
    IVehicle iVehicle;

    @Autowired
    IVehicleRealTimeStatusService iVehicleRealTimeStatusService;

    @Autowired
    IVehicleDayFuelCountService iVehicleDayFuelCountService;

    @Override
    public void execute() {
        List<Vehicle> vehicles = iVehicleService.list();
        List<String> vehiclePates = vehicles.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
        List<VehicleRealtimeStatusAdasDto> realtimeStatuses = iVehicle.ListVehicleRealtimeStatusByPlates(vehiclePates);
        List<VehicleRealTimeStatus> vehicleRealTimeStatuses = realtimeStatuses.parallelStream().map(i -> {
            VehicleRealTimeStatus vehicleRealTimeStatus = new VehicleRealTimeStatus();
            BeanUtils.copyProperties(i, vehicleRealTimeStatus);
            return vehicleRealTimeStatus;
        }).collect(Collectors.toList());
        log.info("开始执行：获取车辆实时状态信息");

        // 保存状态记录
        iVehicleRealTimeStatusService.saveBatch(vehicleRealTimeStatuses);
        log.info("开始执行：获取车辆实时状态信息完成");
    }

    /**
     * 按天统计插入数据库
     */
    public void countEveryDayConsume(List<String> vehiclePates, List<VehicleRealTimeStatus> newStatusList) {
        //先从数据库查询出今天的数据
        LambdaQueryWrapper<VehicleRealTimeStatus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .in(VehicleRealTimeStatus::getPlate, vehiclePates)
                .ge(VehicleRealTimeStatus::getCreateTime, LocalDate.now())
                .ge(VehicleRealTimeStatus::getCreateTime, LocalDate.now().plusDays(1))
                .orderByDesc(VehicleRealTimeStatus::getCreateTime)
                .last("limit " + vehiclePates.size())
        ;
        //查出最近的油量状态集合并按车牌号分类
        Map<String, VehicleRealTimeStatus> vehicleRealTimeStatusMap = iVehicleRealTimeStatusService
                .list(new LambdaQueryWrapper<VehicleRealTimeStatus>().in(VehicleRealTimeStatus::getPlate, vehiclePates))
                .parallelStream()
                .collect(Collectors.toMap(VehicleRealTimeStatus::getPlate, i -> i));

        //查出当天油量集合并按车牌号分类
        Map<String, VehicleDayFuelCount> dayFuelMap = iVehicleDayFuelCountService
                .list(
                        new LambdaQueryWrapper<VehicleDayFuelCount>()
                                .in(VehicleDayFuelCount::getPlate, vehiclePates)
                                .eq(VehicleDayFuelCount::getDate, LocalDate.now())
                )
                .parallelStream()
                .collect(Collectors.toMap(VehicleDayFuelCount::getPlate, i -> i));

        List<VehicleDayFuelCount> vehicleDayFuelCounts = new ArrayList<>();
        for (VehicleRealTimeStatus newStatus : newStatusList) {
            String plate = newStatus.getPlate();
            VehicleRealTimeStatus oldStatus = vehicleRealTimeStatusMap.get(plate);
            double fuelConsume = 0;
            if (plate.equals(oldStatus.getPlate())) {
                double newOilNum = StringToDouble(newStatus.getOil());
                double oldOilNum = StringToDouble(oldStatus.getOil());
                //如果当前油量小于最近油量，说明油量消耗了，否则加油了
                if (newOilNum < oldOilNum) {
                    fuelConsume = oldOilNum - newOilNum;
                }
            }
            VehicleDayFuelCount vehicleDayFuelCount = dayFuelMap.get(plate);
            if (vehicleDayFuelCount != null) {
                vehicleDayFuelCount.setFuelConsume(vehicleDayFuelCount.getFuelConsume() + fuelConsume);
            } else {
                vehicleDayFuelCount = new VehicleDayFuelCount().setFuelConsume(fuelConsume).setPlate(plate).setDate(LocalDate.now());
            }
            vehicleDayFuelCounts.add(vehicleDayFuelCount);
        }
        if (vehicleDayFuelCounts.size() > 0) {
            iVehicleDayFuelCountService.updateBatchById(vehicleDayFuelCounts);
        }

    }

    private double StringToDouble(String number) {
        if (number == null || number.equals("")) {
            number = "0";
        }
        return Double.parseDouble(number);
    }
}
