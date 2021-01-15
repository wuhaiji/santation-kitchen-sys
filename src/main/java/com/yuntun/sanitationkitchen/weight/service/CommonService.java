package com.yuntun.sanitationkitchen.weight.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.*;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import com.yuntun.sanitationkitchen.weight.entity.G780Data;
import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
import com.yuntun.sanitationkitchen.weight.entity.TicketBill;
import com.yuntun.sanitationkitchen.weight.resolve.ResolveProtocol;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import com.yuntun.sanitationkitchen.weight.util.UDCDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujihong
 */
@Component
@Slf4j
public class CommonService {

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    private List<ResolveProtocol> resolveProtocolList;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private TrashCanMapper trashCanMapper;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Autowired
    private WeighbridgeMapper weighbridgeMapper;

    @Autowired
    private SanitationOfficeMapper sanitationOfficeMapper;

    @Autowired
    private PoundBillMapper poundBillMapper;

    @Autowired
    private TrashWeightSerialMapper trashWeightSerialMapper;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private TicketMachineMapper ticketMachineMapper;

    @Autowired
    private UDCDataUtil udcDataUtil;

    public static final String DRIVER = "driver";

    public static final String VEHICLE = "vehicle";

    public static final String TRASH = "trash";

    /**
     * 根据dtu的设备号查询
     * 获取车辆信息
     *
     * @return
     */
    public Vehicle getVehicleByDTU(String deviceNumber) {
        // 根据DTU设备号查询小票机信息
        TicketMachine ticketMachine = getTicketMachineByDTU(deviceNumber);
        // 根据小票机的唯一编号，去获取车辆信息
        // 唯一编号（两种情况）：
        // * 1.对应车辆表的rfid
        // * 2.对应地磅表的net_device_code
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().
                eq(Vehicle::getRfid, ticketMachine.getUniqueCode()));
        if (vehicle == null) {
            log.error("车辆的rfid无效！");
            throw new ServiceException("车辆的rfid无效！");
        }
        return vehicle;
    }

    /**
     * 根据dtu的设备号查询
     * 获取小票机信息
     *
     * @return
     */
    public TicketMachine getTicketMachineByDTU(String deviceNumber) {
        // 根据DTU设备号查询小票机信息
        TicketMachine ticketMachine = ticketMachineMapper.selectOne(new QueryWrapper<TicketMachine>().lambda().
                eq(TicketMachine::getNetDeviceCode, deviceNumber));
        if (ticketMachine == null) {
            log.error("小票机的网络设备编号无效！");
            throw new ServiceException("小票机的网络设备编号无效！");
        }
        return ticketMachine;
    }

    /**
     * 根据dtu的设备号查询
     * 获取地磅信息
     *
     * @return
     */
    public Weighbridge getWeighbridgeByDTU(String deviceNumber) {
        // 获取地磅信息
        Weighbridge weighbridge = weighbridgeMapper.selectOne(new QueryWrapper<Weighbridge>().lambda().eq(Weighbridge::getNetDeviceCode, deviceNumber));
        if (weighbridge == null) {
            log.error("地磅表中的网络设备编号无效！");
            throw new ServiceException("地磅表中的网络设备编号无效！");
        }
        return weighbridge;
    }

    /**
     * 根据司机rfid的epc号查询
     * 获取司机信息
     *
     * @return
     */
    public Driver getDriverInfo(String driverEPC) {
        // 获取垃圾桶信息
        Driver driver = driverMapper.selectOne(new QueryWrapper<Driver>().lambda().eq(driverEPC != null, Driver::getRfid, driverEPC));
        if (driver == null) {
            log.error("司机的RFID无效！");
            throw new ServiceException("司机的RFID无效！");
        }
        return driver;
    }

    /**
     * 根据垃圾桶rfid的epc号查询
     * 获取垃圾桶信息
     *
     * @return
     */
    public TrashCan getTrashCanInfo(String trashCanEPC) {
        // 获取垃圾桶信息
        TrashCan trashCan = trashCanMapper.selectOne(new QueryWrapper<TrashCan>().lambda().eq(trashCanEPC != null, TrashCan::getRfid, trashCanEPC));
        if (trashCan == null) {
            log.error("垃圾桶的RFID无效！");
            throw new ServiceException("垃圾桶的RFID无效！");
        }
        return trashCan;
    }

    /**
     * 根据rfid的epc号查询
     * 获取车辆信息
     *
     * @return
     */
    public Vehicle getVehicleInfo(String vehicleEPC) {
        // 获取垃圾桶信息
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, vehicleEPC));
        if (vehicle == null) {
            log.error("车辆的RFID无效！");
            throw new ServiceException("车辆的RFID无效！");
        }
        return vehicle;
    }

    /**
     * 获取RFID的数据类型（车辆[地磅]数据、垃圾桶[车辆]数据、驾驶员）
     *
     * @param epc
     * @return
     */
    public String getRFIDType(String epc) {

//        String epc = udcDataUtil.getEPC(bytes);
        if (epc == null) {
            throw new ServiceException("rfid的epc号不能为空");
        }

        System.out.println("epc--"+epc);
        Integer driverCount = driverMapper.selectCount(new QueryWrapper<Driver>().lambda().eq(Driver::getRfid, epc));
        if (driverCount != null && driverCount != 0) {
            return DRIVER;
        }

        Integer vehicleCount = vehicleMapper.selectCount(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, epc));
        if (vehicleCount != null && vehicleCount != 0) {
            return VEHICLE;
        }

        Integer trashCanCount = trashCanMapper.selectCount(new QueryWrapper<TrashCan>().lambda().eq(TrashCan::getRfid, epc));
        if (trashCanCount != null && trashCanCount != 0) {
            return TRASH;
        }
        return epc;
    }

    // 根据司机EPC，去生成小票机
    public TicketBill getTicketBillByDriverEPC(String driverEPC) {
        Driver driver = driverMapper.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getRfid, driverEPC));
        TicketBill ticketBill = new TicketBill();
        ticketBill.setDriverName(driver.getName());
        ticketBill.setTime(LocalDateTime.now());
        return ticketBill;
    }

    // 根据车辆EPC，去生成小票机
    public TicketBill getTicketBillByVehicleEPC(String vehicleEPC) {
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, vehicleEPC));
        TicketBill ticketBill = new TicketBill();
        ticketBill.setPlateNo(vehicle.getNumberPlate());
        ticketBill.setTime(LocalDateTime.now());
        return ticketBill;
    }

    public SKDataBody resolve(byte[] dataBody) {
        System.out.println("开始解析数据！...resolve");
        SKDataBody skDataBody = new SKDataBody();

        for (ResolveProtocol resolveProtocol:resolveProtocolList) {
            SKDataBody resolve = resolveProtocol.resolveAll(dataBody);
            if (!resolve.equals(new SKDataBody())) {
                skDataBody = resolve;
                System.out.println("resolveData:"+skDataBody);
            }
        }

        return skDataBody;
    }

    // 生成榜单
    public void generatePoundBill (String deviceNumber, String vehicleEPC, String driverEPC, Double boundWeight) {
        PoundBill poundBill = new PoundBill();

        Vehicle vehicleInfo = getVehicleInfo(vehicleEPC);

        String format = dtf.format(LocalDateTime.now());
        // 获取流水号
        poundBill.setSerialCode(vehicleInfo.getNumberPlate()+"-"+format);

        // 获取所属机构信息
        // 根据地磅的网络设备编号去查询地磅所属机构信息
        Weighbridge weighbridge = getWeighbridgeByDTU(deviceNumber);
        SanitationOffice sanitationOffice = sanitationOfficeMapper.selectOne(new QueryWrapper<SanitationOffice>().select("uid", "name").
                eq("uid", weighbridge.getSanitationOfficeId()));
        if (sanitationOffice == null) {
            log.error("地磅表中的机构id无效！");
            throw new ServiceException("地磅表中的机构id无效！");
        }
        poundBill.setSanitationOfficeId(sanitationOffice.getUid());
        poundBill.setSanitationOfficeName(sanitationOffice.getName());

        // 获取车牌号
        poundBill.setVehicleId(vehicleInfo.getUid());
        poundBill.setNumberPlate(vehicleInfo.getNumberPlate());

        // 获取司机信息
        Driver driver = getDriverInfo(driverEPC);
        poundBill.setDriverRfid(driver.getRfid());
        poundBill.setDriverName(driver.getName());

        // 获取毛重(单位：kg)
        poundBill.setGrossWeight(boundWeight);

        // 获取皮重(单位：kg)
        poundBill.setTare(Double.valueOf(vehicleInfo.getWeight().toString()));

        // 获取净重(单位：kg)
        poundBill.setNetWeight(boundWeight-poundBill.getTare());

        poundBill.setUid(SnowflakeUtil.getUnionId());
        System.out.println("poundBill地磅流水:"+poundBill);
        poundBillMapper.insert(poundBill);

    }

    // 生成垃圾桶流水
    public void generateTrashWeightSerial(List<Double> weightList, TrashCan trashCanInfo, Driver driverInfo) {
        TrashWeightSerial trashWeightSerial = new TrashWeightSerial();

        // 获取垃圾桶的称重结果
        Double weightResult = getTrashWeight(weightList, trashCanInfo);

        // 获取餐馆信息
        Restaurant restaurant = restaurantMapper.selectOne(new QueryWrapper<Restaurant>().lambda().select(Restaurant::getUid, Restaurant::getName).
                eq(trashCanInfo.getRestaurantId() != null, Restaurant::getUid, trashCanInfo.getRestaurantId()));
        if (restaurant == null) {
            log.error("餐馆的uid无效！");
            throw new ServiceException("餐馆的uid无效！");
        }

        // 获取垃圾桶rfid
        trashWeightSerial.setTrashCanRfid(trashCanInfo.getRfid());

        // 获取垃圾桶设备编号
        trashWeightSerial.setFacilityCode(trashCanInfo.getFacilityCode());

        // 获取餐馆信息
        trashWeightSerial.setRestaurantId(restaurant.getUid());
        trashWeightSerial.setRestaurantName(restaurant.getName());

        // 获取垃圾桶重量
        trashWeightSerial.setWeight(weightResult);

        // 获取司机信息
        trashWeightSerial.setDriverRfid(driverInfo.getRfid());
        trashWeightSerial.setDriverName(driverInfo.getName());

        System.out.println("trashWeightSerial垃圾桶流水:"+trashWeightSerial);
        trashWeightSerialMapper.insert(trashWeightSerial);
    }

    public Double getTrashWeight(List<Double> weightList, TrashCan trashCanInfo) {
        Double trashCanTareWeight = trashCanInfo.getWeight();
        Double weightResult = weightList.stream().filter(weight -> weight > trashCanTareWeight).collect(Collectors.averagingDouble(x -> x-trashCanTareWeight));
        System.out.println("称重结果："+weightResult+"kg");

        return weightResult;
    }


}
