package com.yuntun.sanitationkitchen.vehicle.api;

import com.yuntun.sanitationkitchen.config.Scheduled.ScheduledTask;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleRealTimeStatus;
import com.yuntun.sanitationkitchen.service.IVehicleRealTimeStatusService;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Autowired
    IVehicleService iVehicleService;

    @Autowired
    IVehicle iVehicle;

    @Autowired
    IVehicleRealTimeStatusService iVehicleRealTimeStatusService;

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


        iVehicleRealTimeStatusService.saveBatch(vehicleRealTimeStatuses);

    }
}
