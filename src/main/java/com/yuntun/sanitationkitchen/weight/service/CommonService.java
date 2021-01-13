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
    private UDCDataUtil udcDataUtil;

    public static final String DRIVER = "driver";

    public static final String VEHICLE = "vehicle";

    public static final String TRASH = "trash";

    /**
     * 根据rfid的epc号查询
     * 获取司机信息
     *
     * @return
     */
    public Driver getDriverInfo(String epc) {
        // 获取垃圾桶信息
        Driver driver = driverMapper.selectOne(new QueryWrapper<Driver>().lambda().eq(epc != null, Driver::getRfid, epc));
        if (driver == null) {
            log.error("司机的RFID无效！");
            throw new ServiceException("司机的RFID无效！");
        }
        return driver;
    }

    /**
     * 根据rfid的epc号查询
     * 获取垃圾桶信息
     *
     * @return
     */
    public TrashCan getTrashCanInfo(String epc) {
        // 获取垃圾桶信息
        TrashCan trashCan = trashCanMapper.selectOne(new QueryWrapper<TrashCan>().lambda().eq(epc != null, TrashCan::getRfid, epc));
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
    public Vehicle getVehicleInfo(String epc) {
        // 获取垃圾桶信息
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, epc));
        if (vehicle == null) {
            log.error("车辆的RFID无效！");
            throw new ServiceException("车辆的RFID无效！");
        }
        return vehicle;
    }

    /**
     * 根据dtu的设备号查询
     * 获取地磅信息
     *
     * @return
     */
    public Weighbridge getWeighbridgeInfo(String deviceNumber) {
        // 获取地磅信息
        Weighbridge weighbridge = weighbridgeMapper.selectOne(new QueryWrapper<Weighbridge>().eq("facility_code", deviceNumber));
        if (weighbridge == null) {
            log.error("地磅表中的设备编号无效！");
            throw new ServiceException("地磅表中的设备编号无效！");
        }
        return weighbridge;
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

    public TicketBill getTrashTicketBill(String epc) {
        Driver driver = driverMapper.selectOne(new QueryWrapper<Driver>().lambda().eq(Driver::getRfid, epc));
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getDriverName, driver.getName()).
                eq(Vehicle::getDriverPhone, driver.getPhone()));
        TicketBill ticketBill = new TicketBill();
        ticketBill.setDriverName(driver.getName());
        ticketBill.setPlateNo(vehicle.getNumberPlate());
        ticketBill.setTime(LocalDateTime.now());
        return ticketBill;
    }

    public TicketBill getBoundTicketBill(String epc) {
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, epc));
        TicketBill ticketBill = new TicketBill();
        ticketBill.setDriverName(vehicle.getDriverName());
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
    public void generatePoundBill (String deviceNumber, String vehicleEPC, Double boundWeight) {
        PoundBill poundBill = new PoundBill();

        Vehicle vehicleInfo = getVehicleInfo(vehicleEPC);

        String format = dtf.format(LocalDateTime.now());
        // 获取流水号
        poundBill.setSerialCode(vehicleInfo.getNumberPlate()+"-"+format);

        // 获取所属机构信息
        // 根据地磅的设备号去查询地磅所属机构信息
        Weighbridge weighbridge = weighbridgeMapper.selectOne(new QueryWrapper<Weighbridge>().select("sanitation_office_id").
                eq("net_device_code", deviceNumber));
        if (weighbridge == null) {
            log.error("地磅表中的网络设备编号无效！");
            throw new ServiceException("地磅表中的网络设备编号无效！");
        }
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

//        // 获取司机信息
//        Driver driver = driverMapper.selectOne(new QueryWrapper<Driver>().select("rfid", "name").
//                eq("name", vehicleInfo.getDriverName()).eq("phone", vehicleInfo.getDriverPhone()));
//        poundBill.setDriverRfid(driver.getRfid());
//        poundBill.setDriverName(driver.getName());

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
