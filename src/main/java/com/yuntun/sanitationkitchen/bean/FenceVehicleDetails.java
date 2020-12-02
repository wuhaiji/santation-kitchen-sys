package com.yuntun.sanitationkitchen.bean;



import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import java.util.List;
import lombok.Data;


@Data
public class FenceVehicleDetails {

    private String name;
    private int count;
    private List<Vehicle> vehicles;

    public FenceVehicleDetails() {
    }

    public FenceVehicleDetails(String name, int count, List<Vehicle> vehicles) {
        this.name = name;
        this.count = count;
        this.vehicles = vehicles;
    }
}
