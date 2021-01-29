package com.yuntun.sanitationkitchen.weight.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.*;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.util.RedisUtils;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import com.yuntun.sanitationkitchen.weight.config.UDCDataHeaderType;
import com.yuntun.sanitationkitchen.weight.entity.SKDataBody;
import com.yuntun.sanitationkitchen.weight.entity.TicketBill;
import com.yuntun.sanitationkitchen.weight.resolve.ResolveProtocol;
import com.yuntun.sanitationkitchen.weight.util.MqttTool;
import com.yuntun.sanitationkitchen.weight.util.ObjectCopy;
import com.yuntun.sanitationkitchen.weight.util.PrintUtil;
import com.yuntun.sanitationkitchen.weight.util.UDCDataResponse;
import io.lettuce.core.RedisURI;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CommonService {

    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

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
    MqttTool mqttTool;

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
        TicketMachine ticketMachine = null;
        try {
            ticketMachine = getTicketMachineByDTU(deviceNumber);
        } catch (ServiceException ex) {
            return null;
        }

        log.info("根据dtu的设备号:{}--查询小票机信息:{}", deviceNumber, ticketMachine);
        // 根据小票机的唯一编号，去获取车辆信息
        // 唯一编号（两种情况）：
        // * 1.对应车辆表的rfid
        // * 2.对应地磅表的net_device_code
        Vehicle vehicle = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().lambda().
                eq(Vehicle::getRfid, ticketMachine.getUniqueCode()));
        if (vehicle == null) {
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
        List<TicketMachine> ticketMachineList = ticketMachineMapper.selectList(new QueryWrapper<TicketMachine>().lambda().
                eq(TicketMachine::getNetDeviceCode, deviceNumber));
        if (ticketMachineList == null || ticketMachineList.size() == 0) {
            log.error("小票机的网络设备编号:{}--无效！", deviceNumber);
            throw new ServiceException("小票机的网络设备编号无效！");
        }
        if (ticketMachineList.size() > 1) {
            log.error("DTU设备号为：{}--不能绑定在多个小票机上！", deviceNumber);
//            throw new ServiceException("一个DTU不能绑定在多个小票机上！");
        }
        return ticketMachineList.get(0);
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
            log.error("地磅表中的网络设备编号为：{}--无效！", deviceNumber);
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
            log.error("车辆的RFID为：{}--无效！", vehicleEPC);
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
        if (epc == null || epc.equals("")) {
            throw new ServiceException("rfid的epc号不能为空！");
        }

        System.out.println("epc--" + epc);
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

        throw new ServiceException("无效的EPC！");
    }

    // 根据司机EPC，去生成小票机
    public TicketBill getTicketBillByDriverEPC(String driverEPC, String deviceNumber) {
        Driver driver = getDriverInfo(driverEPC);
        TicketBill ticketBill = new TicketBill();
        ticketBill.setDriverName(driver.getName());
        ticketBill.setCardNo(deviceNumber);
        ticketBill.setTime(LocalDateTime.now());

        return ticketBill;
    }

    // 根据车辆EPC，去生成小票机
    public TicketBill getTicketBillByVehicleEPC(String vehicleEPC, String deviceNumber) {
        Vehicle vehicle = getVehicleInfo(vehicleEPC);
        TicketBill ticketBill = new TicketBill();
        ticketBill.setPlateNo(vehicle.getNumberPlate());
        ticketBill.setCardNo(deviceNumber);
        ticketBill.setTime(LocalDateTime.now());

        return ticketBill;
    }

    public SKDataBody resolve(byte[] dataBody) {
        log.info("开始解析数据！...resolve");
        SKDataBody skDataBody = new SKDataBody();

        for (ResolveProtocol resolveProtocol : resolveProtocolList) {
            SKDataBody resolve = resolveProtocol.resolveAll(dataBody);
            // 拷贝
            ObjectCopy.copyNotNullObject(resolve, skDataBody);
        }

        return skDataBody;
    }

    /**
     * 根据EPC号 处理数据
     *
     * @param ctx
     * @param bytes
     * @param deviceNumber
     * @param epc
     * @param rfidType
     */
    public void disposeEPC(ChannelHandlerContext ctx, byte[] bytes, String deviceNumber, String epc, String rfidType) {
        // 垃圾桶
        if (CommonService.TRASH.equals(rfidType)) {
            // 存储垃圾桶的epc
            RedisUtils.setValue("sk:" + deviceNumber + "_trashCanEPC", epc);
            log.info("DTU设备号为:{} 对垃圾桶下发数据采集指令！", deviceNumber);
            // 垃圾桶数据采集指令
            ctx.writeAndFlush(Unpooled.copiedBuffer(UDCDataResponse.response(bytes, UDCDataHeaderType.SEND_PACKAGE, UDCDataHeaderType.trashCollectOrder)));
        }

        // 车辆
        if (CommonService.VEHICLE.equals(rfidType)) {
            // 根据车辆EPC，去生成小票机流水信息
            TicketBill ticketBill = getTicketBillByVehicleEPC(epc, deviceNumber);
            log.info("ticketBill:{}", ticketBill);
            RedisUtils.setValue("sk:" + deviceNumber + "_vehicleEPC", epc);
            RedisUtils.setValue("sk:" + deviceNumber + "_ticketBill", ticketBill);

            // 对地磅下发数据采集指令（读取毛重）
            log.info("DTU设备号为:{} 对地磅下发数据采集指令！", deviceNumber);
            ctx.writeAndFlush(Unpooled.copiedBuffer(UDCDataResponse.response(bytes, UDCDataHeaderType.SEND_PACKAGE, UDCDataHeaderType.BoundCollectOrder)));
        }

        // 司机
        if (CommonService.DRIVER.equals(rfidType)) {
            // 根据司机EPC，去生成小票机
            TicketBill ticketBill = getTicketBillByDriverEPC(epc, deviceNumber);

            // 将此次小票机信息存入到redis中--直到称重结束，再打印小票机
            log.info("这是司机-------------");
            TicketBill redisTicketBill = (TicketBill) RedisUtils.getValue("sk:" + deviceNumber + "_ticketBill");
            if (redisTicketBill != null) {
                BeanUtils.copyProperties(redisTicketBill, ticketBill);
            }
            RedisUtils.setValue("sk:" + deviceNumber + "_ticketBill", ticketBill);
            RedisUtils.setValue("sk:" + deviceNumber + "_driverEPC", epc);
        }
    }

    // 生成磅单
    public void generatePoundBill(String deviceNumber, String vehicleEPC, String driverEPC, Double boundWeight) throws ServiceException {
        PoundBill poundBill = new PoundBill();

        Vehicle vehicleInfo = getVehicleInfo(vehicleEPC);

        String format = dtf.format(LocalDateTime.now());
        // 获取流水号
        poundBill.setSerialCode(vehicleInfo.getNumberPlate() + "-" + format);

        // 获取所属机构信息
        // 根据地磅的网络设备编号去查询地磅所属机构信息
        Weighbridge weighbridge = getWeighbridgeByDTU(deviceNumber);
        SanitationOffice sanitationOffice = sanitationOfficeMapper.selectOne(new QueryWrapper<SanitationOffice>().select("uid", "name").
                eq("uid", weighbridge.getSanitationOfficeId()));
        if (sanitationOffice == null) {
            log.error("地磅表中的机构id为：{}--无效！", weighbridge.getSanitationOfficeId());
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
        poundBill.setNetWeight(boundWeight - poundBill.getTare());

        poundBill.setUid(SnowflakeUtil.getUnionId());
        log.info("poundBill地磅流水:{}", poundBill);
        poundBillMapper.insert(poundBill);

    }

    // 生成垃圾桶流水
    public void generateTrashWeightSerial(List<Double> weightList, TrashCan trashCanInfo, Driver driverInfo) throws ServiceException {
        TrashWeightSerial trashWeightSerial = new TrashWeightSerial();

        // 获取垃圾桶的称重结果
        Double weightResult = getTrashWeight(weightList, trashCanInfo);

        // 获取餐馆信息
        Restaurant restaurant = restaurantMapper.selectOne(new QueryWrapper<Restaurant>().lambda().select(Restaurant::getUid, Restaurant::getName).
                eq(trashCanInfo.getRestaurantId() != null, Restaurant::getUid, trashCanInfo.getRestaurantId()));
        if (restaurant == null) {
            log.error("餐馆的uid为：{}--无效！", trashCanInfo.getRestaurantId());
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

        log.info("垃圾桶流水:{}", trashWeightSerial);
        trashWeightSerialMapper.insert(trashWeightSerial);
    }

    public Double getTrashWeight(List<Double> weightList, TrashCan trashCanInfo) {
        Double trashCanTareWeight = trashCanInfo.getWeight();
        Double weightResult = weightList.stream().filter(weight -> weight > trashCanTareWeight).collect(Collectors.averagingDouble(x -> x - trashCanTareWeight));
        log.info("称重结果：{}kg", weightResult);
        return weightResult;
    }

    /**
     * 发送到小票机打印
     *
     * @param ticketBill   小票信息
     * @param deviceNumber dtu卡号
     */
    public void send2TicketText(TicketBill ticketBill, String deviceNumber) {
        TicketMachine ticketMachine = this.getTicketMachineByDTU(deviceNumber);
        String trashCanEPC = RedisUtils.getString("sk:" + deviceNumber + "_trashCanEPC");
        TrashCan trashCanInfo=new TrashCan();
        if(trashCanEPC!=null){
             trashCanInfo = this.getTrashCanInfo(trashCanEPC);
        }else{
            log.warn("未找到缓存中的垃圾桶，deviceNumber:"+deviceNumber);
        }
        log.info("开始发送小票机打印信息,ticketBill"+ JSON.toJSONString(ticketBill));
        // 发送打印小票机请求
        String format = LocalDateTimeUtil.format(ticketBill.getTime(), DatePattern.CHINESE_DATE_TIME_PATTERN);
        String ticketInfo =
                "________________________________"
                        + "\r\n"
                        + "\r\n"
                        + "        垃圾桶称重结果\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "司机：" + ticketBill.getDriverName() + "\r\n"
                        + "\r\n"
                        + "车牌号：" + ticketBill.getPlateNo() + "\r\n"
                        + "\r\n"
                        + "称重重量：" + ticketBill.getWeight() + "\r\n"
                        + "\r\n"
                        + "垃圾桶RFID：" + trashCanInfo.getRfid() + "\r\n"
                        + "\r\n"
                        + "垃圾桶地址：" + trashCanInfo.getAddress() + "\r\n"
                        + "\r\n"
                        + "时间：" + format + "\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "________________________________"
                        + "\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "\r\n"
                        + "\r\n";
        byte[] printerBytes = PrintUtil.getPrinterBytes(ticketInfo, 1, "utf-8");
        String payload = PrintUtil.bytes2Hex(printerBytes);
        log.info("结果:{}", payload);
        log.info("小票机发送的数据hex:{}", payload);
        String deviceCode = ticketMachine.getDeviceCode();
        log.info("小票机发送的主题:{}", deviceCode);
        // MqttSenderUtil.getMqttSender().sendToMqtt(deviceCode, payload);
         mqttTool.publish(deviceCode, printerBytes);
    }
}
