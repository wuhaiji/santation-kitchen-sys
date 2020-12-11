package com.yuntun.sanitationkitchen.vehicle.api;

import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/11
 */

@Component
public class VehicleTask {
    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IVehicle vehicle;
    @Autowired
    IVehicleService iVehicleService;

    // @Scheduled(cron = "0 0/2 * * * ? ")
    // public void execute() {
    //     List<Vehicle> list = iVehicleService.list();
    //     List<String> plates = list.parallelStream().map(i -> i.getNumberPlate()).collect(Collectors.toList());
    //     List<VehicleRealtimeStatusAdasDto> vehicleRealtimeStatusAdasDtos = vehicle.ListVehicleRealtimeStatusByPlates(plates);
    //
    // }
}
