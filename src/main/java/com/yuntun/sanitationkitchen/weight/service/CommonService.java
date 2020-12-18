package com.yuntun.sanitationkitchen.weight.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.*;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import com.yuntun.sanitationkitchen.weight.entity.G780Data;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import com.yuntun.sanitationkitchen.weight.util.UDCDataUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wujihong
 */
@Component
@Slf4j
public class CommonService {

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

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
    private UDCDataUtil udcDataUtil;

    public static final String VEHICLE = "vehicle";

    public static final String TRASH = "trash";



    /**
     * 获取RFID的数据类型（车辆[地磅]数据、垃圾桶[车辆]数据）
     *
     * @param bytes
     * @return
     */
    public String getRFIDType(byte[] bytes) {
        String rfid = udcDataUtil.getRFID(bytes);
        if (rfid == null) {
            throw new ServiceException("rfid不能为空");
        }
        Integer vehicleCount = vehicleMapper.selectCount(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, rfid));
        if (vehicleCount != null && vehicleCount != 0) {
            return VEHICLE;
        }
        Integer trashCanCount = trashCanMapper.selectCount(new QueryWrapper<TrashCan>().lambda().eq(TrashCan::getRfid, rfid));
        if (trashCanCount != null && trashCanCount != 0) {
            return TRASH;
        }
        return rfid;
    }

    // 生成榜单
    public void generatePoundBill (G780Data g780Data) {
        if (g780Data == null) {
            log.error("地磅设备上传的信息为空！");
            throw new ServiceException("地磅设备上传的信息为空！");
        }
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, g780Data.getRfid()));

        String format = dtf.format(LocalDateTime.now());
        PoundBill poundBill = new PoundBill();
        // 获取流水号
        poundBill.setSerialCode(vehicle.getNumberPlate()+format);

        // 获取所属机构信息
        // 根据地磅的设备号去查询地磅所属机构信息
        Weighbridge weighbridge = weighbridgeMapper.selectOne(new QueryWrapper<Weighbridge>().select("sanitation_office_id").
                eq("device_code", g780Data.getDeviceNumber()));
        if (weighbridge == null) {
            log.error("地磅表中的设备编号无效！");
            throw new ServiceException("地磅表中的设备编号无效！");
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
        poundBill.setVehicleId(vehicle.getUid());
        poundBill.setNumberPlate(vehicle.getNumberPlate());

        // 获取毛重(单位：kg)
        poundBill.setGrossWeight(g780Data.getGrossWeight());

        // 获取皮重(单位：kg)
        poundBill.setTare(Double.valueOf(vehicle.getWeight().toString()));

        // 获取净重(单位：kg)
        poundBill.setNetWeight(poundBill.getGrossWeight()-poundBill.getTare());

        poundBill.setUid(SnowflakeUtil.getUnionId());
        poundBillMapper.insert(poundBill);

    }

    // 生成垃圾桶流水
    public void generateTrashWeightSerial(G780Data g780Data) {
        if (g780Data == null) {
            log.error("车辆设备上传的信息为空！");
            throw new ServiceException("车辆设备上传的信息为空！");
        }
        String rfid = g780Data.getRfid();
        TrashWeightSerial trashWeightSerial = new TrashWeightSerial();

        // 获取垃圾桶信息
        TrashCan trashCan = trashCanMapper.selectOne(new QueryWrapper<TrashCan>().lambda().select(TrashCan::getFacilityCode, TrashCan::getRfid, TrashCan::getRestaurantId).
                eq(rfid != null, TrashCan::getRfid, rfid));
        if (trashCan == null) {
            log.error("垃圾桶的RFID无效！");
            throw new ServiceException("垃圾桶的RFID无效！");
        }

        // 获取餐馆信息
        Restaurant restaurant = restaurantMapper.selectOne(new QueryWrapper<Restaurant>().lambda().select(Restaurant::getUid, Restaurant::getName).
                eq(trashCan.getRestaurantId() != null, Restaurant::getUid, trashCan.getRestaurantId()));
        if (restaurant == null) {
            log.error("餐馆的uid无效！");
            throw new ServiceException("餐馆的uid无效！");
        }

        // 获取垃圾桶rfid
        trashWeightSerial.setTrashCanRfid(trashCan.getRfid());

        // 获取垃圾桶设备编号
        trashWeightSerial.setFacilityCode(trashCan.getFacilityCode());

        // 获取餐馆信息
        trashWeightSerial.setRestaurantId(restaurant.getUid());
        trashWeightSerial.setRestaurantName(restaurant.getName());

        // 获取垃圾桶重量
        trashWeightSerial.setWeight(g780Data.getGrossWeight());

        trashWeightSerialMapper.insert(trashWeightSerial);

    }


}
