package com.yuntun.sanitationkitchen.vehicle.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.config.Scheduled.ScheduledTask;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleDayFuelCount;
import com.yuntun.sanitationkitchen.model.entity.VehicleRealTimeStatus;
import com.yuntun.sanitationkitchen.service.IVehicleDayFuelServiceCount;
import com.yuntun.sanitationkitchen.service.IVehicleRealTimeStatusService;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 每日油耗统计任务
 * </p>
 *
 * @author whj
 * @since 2020/12/15
 */
@Component
public class FuelDayCountTask implements ScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IVehicleService iVehicleService;

    @Autowired
    IVehicle iVehicle;

    @Autowired
    IVehicleRealTimeStatusService iVehicleRealTimeStatusService;

    @Autowired
    IVehicleDayFuelServiceCount iVehicleDayFuelServiceCount;

    @Override
    public void execute() {
        log.info("统计每日油耗开始/////////////////////////////////////////////////////////////////////////////////////");
        LocalDate now = LocalDate.now();
        //先从数据库查询所有车辆牌号
        List<String> plates = iVehicleService.list()
                .parallelStream()
                .map(Vehicle::getNumberPlate).collect(Collectors.toList());

        //再查询出车辆当天历史状态集合
        List<VehicleRealTimeStatus> vehicleRealTimeStatuses = new ArrayList<>();

        if (plates.size() > 0) {
            vehicleRealTimeStatuses = iVehicleRealTimeStatusService.list(
                    new QueryWrapper<VehicleRealTimeStatus>()
                            .in("plate", plates)
                            .ge("create_time", now)
                            .le("create_time", now.plusDays(1))
                            .orderByAsc("create_time")
            );
        }
        //按车辆分类
        Map<String, List<VehicleRealTimeStatus>> statusGroupByPlateMap = vehicleRealTimeStatuses.parallelStream()
                .collect(Collectors.groupingBy(VehicleRealTimeStatus::getPlate));

        //计算出油耗统计
        ArrayList<VehicleDayFuelCount> vehicleDayFuelCounts = new ArrayList<>();
        for (Map.Entry<String, List<VehicleRealTimeStatus>> entry : statusGroupByPlateMap.entrySet()) {
            List<VehicleRealTimeStatus> value = entry.getValue();

            //当天消耗量
            double fuelConsume = 0;

            //当天加油量
            double fuelAdd = 0;

            //上一次状态记录的剩余油量
            double lastOil = 0;

            //当天最后剩余油量
            double fuelRemaining = 0;

            if (EptUtil.isNotEmpty(vehicleRealTimeStatuses)) {
                String oil = vehicleRealTimeStatuses.get(vehicleRealTimeStatuses.size() - 1).getOil();
                if (EptUtil.isNotEmpty(oil)) {
                    fuelRemaining = Double.parseDouble(oil);
                }
            }

            //计算油量消耗
            for (VehicleRealTimeStatus vehicleRealTimeStatus : value) {
                String oil = vehicleRealTimeStatus.getOil();
                if (EptUtil.isEmpty(oil)) {
                    continue;
                }
                double oilDouble = Double.parseDouble(oil);
                if (oilDouble > lastOil) {
                    fuelAdd = oilDouble - lastOil;
                    lastOil = oilDouble;
                    continue;
                }
                fuelConsume += (lastOil - oilDouble);
                lastOil = oilDouble;
            }
            VehicleDayFuelCount vehicleDayFuelCount = new VehicleDayFuelCount()
                    .setDate(now)
                    .setFuelRemaining(fuelRemaining)
                    .setPlate(entry.getKey())
                    .setFuelConsume(fuelConsume)
                    .setFuelAdd(fuelAdd);
            vehicleDayFuelCounts.add(vehicleDayFuelCount);
        }

        iVehicleDayFuelServiceCount.saveBatch(vehicleDayFuelCounts);
        log.info("统计每日油耗完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
