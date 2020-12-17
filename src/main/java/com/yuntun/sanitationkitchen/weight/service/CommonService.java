package com.yuntun.sanitationkitchen.weight.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.model.entity.PoundBill;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.weight.entity.G780Data;
import com.yuntun.sanitationkitchen.weight.util.SpringUtil;
import com.yuntun.sanitationkitchen.weight.util.UDCDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wujihong
 */
@Component
public class CommonService {

    public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private TrashCanMapper trashCanMapper;

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

    public void generatePoundBill (G780Data g780Data) {
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().eq(Vehicle::getRfid, g780Data.getRfid()));
        String format = dtf.format(LocalDateTime.now());
        PoundBill poundBill = new PoundBill();
        // 获取流水号
        poundBill.setSerialCode(format+vehicle.getNumberPlate());
//        poundBill.set

    }


}
