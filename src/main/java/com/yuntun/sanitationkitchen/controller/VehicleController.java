package com.yuntun.sanitationkitchen.controller;


import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.bean.TrackBean;
import com.yuntun.sanitationkitchen.bean.VehicleBean;
import com.yuntun.sanitationkitchen.config.ThirdApiConfig;
import com.yuntun.sanitationkitchen.constant.VehicleStatus;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleSaveDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.yuntun.sanitationkitchen.model.entity.TicketMachine;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.model.vo.VehicleGetVo;
import com.yuntun.sanitationkitchen.model.vo.VehicleListVo;
import com.yuntun.sanitationkitchen.service.ISanitationOfficeService;
import com.yuntun.sanitationkitchen.service.ITicketMachineService;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import com.yuntun.sanitationkitchen.vehicle.api.IVehicle;
import com.yuntun.sanitationkitchen.vehicle.api.VehicleRealtimeStatusAdasDto;
import com.yuntun.sanitationkitchen.vehicle.api.VehicleVideoDto;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 车辆表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IVehicleService iVehicleService;

    @Autowired
    IVehicle iVehicle;

    @Autowired
    ISanitationOfficeService iSanitationOfficeService;

    @Autowired
    ITicketMachineService iTicketMachineService;

    @Autowired
    ThirdApiConfig thirdApiConfig;

    @GetMapping("/list")
    @Limit("vehicle:query")
    public Result<Object> list(VehicleListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Vehicle> iPage = iVehicleService.listPage(dto);

        List<Vehicle> records = iPage.getRecords();


        // 获取车辆实时信息
        List<String> plateNos = records.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
        List<VehicleRealtimeStatusAdasDto> vehicleRealtimeStatusAdasDtoList = new ArrayList<>();
        if (plateNos.size() > 0) {
            vehicleRealtimeStatusAdasDtoList = iVehicle
                    .ListVehicleRealtimeStatusByPlates(plateNos);
        }
        //查询所属单位名称
        List<Long> sanitationOfficeIds = records.parallelStream().map(Vehicle::getSanitationOfficeId).collect(Collectors.toList());

        List<SanitationOffice> sanitationOffices;

        if (EptUtil.isNotEmpty(sanitationOfficeIds)) {
            sanitationOffices = iSanitationOfficeService.list(new QueryWrapper<SanitationOffice>().in("uid", sanitationOfficeIds));
        } else {
            sanitationOffices = new ArrayList<>();
        }
        Map<Long, SanitationOffice> sanitationOfficeMap = sanitationOffices.parallelStream().collect(Collectors.toMap(SanitationOffice::getUid, i -> i));


        List<VehicleRealtimeStatusAdasDto> finalVehicleRealtimeStatusAdasDtoList = vehicleRealtimeStatusAdasDtoList;

        List<VehicleListVo> collect = records.parallelStream().map(i -> {
            VehicleListVo vehicleListVo = new VehicleListVo();
            BeanUtils.copyProperties(i, vehicleListVo);
            //循环找出油量信息和在线离线信息
            for (VehicleRealtimeStatusAdasDto status : finalVehicleRealtimeStatusAdasDtoList) {
                if (status.getPlate().equals(vehicleListVo.getNumberPlate())) {
                    vehicleListVo.setStatus(status.getVehicleStatus());
                    String oil = status.getOil();
                    if (EptUtil.isEmpty(oil)) {
                        vehicleListVo.setFuelRemaining(0.0);
                    }else{
                        vehicleListVo.setFuelRemaining(Double.valueOf(oil));
                    }
                }
            }

            SanitationOffice sanitationOffice = sanitationOfficeMap.get(i.getSanitationOfficeId());
            if (sanitationOffice != null) {
                vehicleListVo.setSanitationOfficeName(sanitationOffice.getName());
            }
            return vehicleListVo;
        }).collect(Collectors.toList());


        RowData<VehicleListVo> data = new RowData<VehicleListVo>()
                .setRows(collect)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }



    @GetMapping("/list/video")
    @Limit("vehicle:query")
    public Result<Object> videos() {

        //查询本服务的车辆信息
        List<Vehicle> vehicles = iVehicleService.list();
        //查询来源云车辆集合
        List<VehicleVideoDto> vehicleVideoDtos = iVehicle.listVideoVehicle();
        Map<String, VehicleVideoDto> vehicleVideoDtoMap = vehicleVideoDtos.parallelStream().collect(Collectors.toMap(VehicleVideoDto::getPlate, i -> i));

        List<VehicleVideoListVo> collect = vehicles.parallelStream().map(i -> {
            VehicleVideoListVo vehicleVideoListVo = new VehicleVideoListVo();
            VehicleVideoDto vehicleVideoDto = vehicleVideoDtoMap.get(i.getNumberPlate());
            if (vehicleVideoDto != null) {
                BeanUtils.copyProperties(vehicleVideoDto, vehicleVideoListVo);
            }
            vehicleVideoListVo.setPlate(i.getNumberPlate());
            return vehicleVideoListVo;
        }).collect(Collectors.toList());

        return Result.ok(collect);

    }


    @GetMapping("/list/track")
    @Limit("vehicle:query")
    public Result<Object> listTrack(VehicleTrackDataListDto dto) {
        //先查询来源云平台车辆的id
        List<VehicleBean> list = iVehicle.list();
        VehicleBean bean = null;
        for (VehicleBean vehicleBean : list) {
            if (vehicleBean.getPlate().equals(dto.getNumberPlate())) {
                bean = vehicleBean;
            }
        }
        List<TrackBean> trackBeans = new ArrayList<>();
        Long startTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.now()).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        Long endTime = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        //
        // Long startTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 8,17,0,0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // Long endTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 8,18,0,0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // Long startTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 7,17,0,0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // Long endTime = LocalDateTimeUtil.beginOfDay(LocalDateTime.of(2020, 7,18,0,0)).toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        if (dto.getStartTime() == null && dto.getEndTime() == null) {
            if (bean != null) {
                trackBeans = iVehicle.queryTrackData(
                        bean.getId(),
                        startTime,
                        endTime
                );
            }
        }
        return Result.ok(trackBeans);
    }

    @GetMapping("/options")
    @Limit("vehicle:query")
    public Result<Object> options() {
        List<Vehicle> list = iVehicleService.list();
        List<OptionsVo> optionsVos = list
                .parallelStream()
                .map(i -> new OptionsVo().setLabel(i.getNumberPlate()).setValue(i.getUid()))
                .collect(Collectors.toList());
        return Result.ok(optionsVos);
    }

    @GetMapping("/get/{uid}")
    @Limit("vehicle:query")
    public Result<Object> get(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "参数");
        Vehicle byId = iVehicleService.getOne(new QueryWrapper<Vehicle>().eq("uid", uid));
        if (EptUtil.isEmpty(byId))
            return Result.error(VehicleCode.ID_NOT_EXIST);
        VehicleGetVo vehicleGetVo = new VehicleGetVo();
        BeanUtils.copyProperties(byId, vehicleGetVo);

        return Result.ok(vehicleGetVo);

    }

    @PostMapping("/save")
    @Limit("vehicle:save")
    public Result<Object> save(VehicleSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getNumberPlate(), "车牌");
        ErrorUtil.isObjectNull(dto.getPurchaseDate(), "购买日期");
        ErrorUtil.isObjectNull(dto.getRfid(), "RFID");
        ErrorUtil.isObjectNull(dto.getSanitationOfficeId(), "所属机构id");
        ErrorUtil.isObjectNull(dto.getTypeId(), "车辆类型");

        // //查询来源云平台是否已经注册这个车辆
        // List<VehicleBean> list = iVehicle.list();
        // if(list.parallelStream().noneMatch(i -> dto.getNumberPlate().equals(i.getPlateNo()))){
        //     throw new ServiceException(VehicleCode.UNREGISTERED_IN_LYY);
        // }

        //检查数据库是否有重复值
        checkRepeatedValue(dto.getNumberPlate(), dto.getRfid());

        //冗余字段，机构名称
        SanitationOffice sanitationOffice = iSanitationOfficeService.getOne(
                new QueryWrapper<SanitationOffice>().eq("uid", dto.getSanitationOfficeId())
        );


        Vehicle role = new Vehicle()
                .setUid(SnowflakeUtil.getUnionId())
//                .setDriverName(dto.getDriverName())
                .setPurchaseDate(dto.getPurchaseDate())
                .setRfid(dto.getRfid())
                .setSanitationOfficeId(dto.getSanitationOfficeId())
                .setSanitationOfficeName(sanitationOffice.getName())
                .setNumberPlate(dto.getNumberPlate())
//                .setDriverPhone(dto.getDriverPhone())
                .setTypeId(dto.getTypeId())
                .setWeight(dto.getWeight())
                .setCreator(UserIdHolder.get());

        boolean save = iVehicleService.save(role);
        if (save)
            return Result.ok();
        return Result.error(VehicleCode.SAVE_ERROR);
    }

    private void checkRepeatedValue(String numberPlate, String rfid) {
        List<Vehicle> listNumberPlate = iVehicleService.list(
                new QueryWrapper<Vehicle>().eq("number_plate", numberPlate)
        );
        if (listNumberPlate.size() > 0) {
            throw new ServiceException(VehicleCode.NUMBER_PLATE_ALREADY_EXISTS);
        }

        List<Vehicle> listRfid = iVehicleService.list(
                new QueryWrapper<Vehicle>().eq("rfid", rfid)
        );
        if (listRfid.size() > 0) {
            throw new ServiceException(VehicleCode.RFID_PLATE_ALREADY_EXISTS);
        }
    }


    @GetMapping("/get/video/key")
    @Limit("vehicle:query")
    public Result<Object> videoKey() {
        return Result.ok(thirdApiConfig.getKey());
    }


    @PostMapping("/update")
    @Limit("vehicle:update")
    public Result<Object> update(VehicleUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "车辆uid不能为空");

        Vehicle older = iVehicleService.getOne(new QueryWrapper<Vehicle>().eq("uid", dto.getUid()));

        List<Vehicle> listNumberPlate = iVehicleService.list(
                new QueryWrapper<Vehicle>().eq("number_plate", dto.getNumberPlate())
        );


        List<Vehicle> listRfid = iVehicleService.list(
                new QueryWrapper<Vehicle>().eq("rfid", dto.getRfid())
        );

        if (older.getNumberPlate().equals(dto.getNumberPlate())) {
            if (listNumberPlate.size() > 1) {
                throw new ServiceException(VehicleCode.NUMBER_PLATE_ALREADY_EXISTS);
            }
        } else {
            if (listNumberPlate.size() > 0) {
                throw new ServiceException(VehicleCode.NUMBER_PLATE_ALREADY_EXISTS);
            }
        }

        if (older.getRfid().equals(dto.getRfid())) {
            if (listRfid.size() > 1) {
                throw new ServiceException(VehicleCode.RFID_PLATE_ALREADY_EXISTS);
            }
        } else {
            if (listRfid.size() > 0) {
                throw new ServiceException(VehicleCode.RFID_PLATE_ALREADY_EXISTS);
            }
        }


        Vehicle vehicle = new Vehicle().setUpdator(UserIdHolder.get());


        if (EptUtil.isNotEmpty(dto.getSanitationOfficeId())) {
            SanitationOffice sanitationOffice = iSanitationOfficeService.getOne(
                    new QueryWrapper<SanitationOffice>().eq("uid", dto.getSanitationOfficeId())
            );
            vehicle.setSanitationOfficeName(sanitationOffice.getName());
        }

        BeanUtils.copyProperties(dto, vehicle);

        boolean save = iVehicleService.update(vehicle,
                new QueryWrapper<Vehicle>().eq("uid", dto.getUid())
        );
        if (save)
            return Result.ok();
        return Result.error(VehicleCode.UPDATE_ERROR);


    }

    @PostMapping("/delete/{id}")
    @Limit("vehicle:delete")
    public Result<Object> delete(@PathVariable("id") Long uid) {
        ErrorUtil.isObjectNull(uid, "id");
        Vehicle vehicle = iVehicleService.getOne(new QueryWrapper<Vehicle>().eq("uid", uid));

        // 1.判断车辆是否存在
        if (vehicle == null) {
            log.error("删除车辆异常->uid不存在");
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        // 2.判断车辆是否可以删除（有无绑定小票机设备）
        List<TicketMachine> ticketMachines = iTicketMachineService.list(new QueryWrapper<TicketMachine>().eq("unique_code", vehicle.getRfid()));
        if (EptUtil.isNotEmpty(ticketMachines.size())) {
            log.error("不能删除，已绑定了小票机的车辆");
            throw new ServiceException(VehicleCode.DELETE_BIND_ERROR);
        }

        boolean b = iVehicleService.remove(new QueryWrapper<Vehicle>().eq("uid", uid));
        if (b)
            return Result.ok();
        return Result.error(VehicleCode.DELETE_ERROR);
    }

    @PostMapping("/delete/batch")
    @Limit("vehicle:delete")
    public Result<Object> deleteBatch(@RequestParam(value = "ids", required = false) List<Long> ids) {
        ErrorUtil.isCollectionEmpty(ids, "ids");
        if (ids.size() > 100) {
            throw new ServiceException(VehicleCode.IDS_TOO_MUCH);
        }
        // 2.判断车辆是否可以删除（有无绑定小票机设备）

        //判断查询辆的rfid
        List<Vehicle> vehicles = iVehicleService.list(new QueryWrapper<Vehicle>().in("uid", ids));
        List<String> vehicleRFIDs = vehicles.parallelStream().map(Vehicle::getRfid).collect(Collectors.toList());
        if (EptUtil.isNotEmpty(vehicleRFIDs)) {
            List<TicketMachine> ticketMachineList = iTicketMachineService.list(
                    new QueryWrapper<TicketMachine>()
                            .lambda()
                            .in(
                                    TicketMachine::getUniqueCode,
                                    vehicleRFIDs
                            )
            );
            if (EptUtil.isNotEmpty(ticketMachineList)) {
                log.error("不能删除，已绑定了小票机的车辆");
                throw new ServiceException(VehicleCode.DELETE_BIND_ERROR);
            }
        }

        boolean b = iVehicleService.remove(new QueryWrapper<Vehicle>().in("uid", ids));

        if (b)
            return Result.ok();
        return Result.error(VehicleCode.DELETE_ERROR);

    }
    @GetMapping("/online/rate")
    @Limit("vehicle:query")
    public Result<?> onlineRate() {
        List<Vehicle> vehicles = iVehicleService.list();
        // 获取车辆实时信息
        List<String> plateNos = vehicles.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
        List<VehicleRealtimeStatusAdasDto> realtimeStatuses = new ArrayList<>();
        if (plateNos.size() > 0) {
            realtimeStatuses = iVehicle.ListVehicleRealtimeStatusByPlates(plateNos);
        }
        //0：从未上线 1：行驶 2：停车 3：离线 4：服务到期
        List<VehicleRealtimeStatusAdasDto> onlineList = realtimeStatuses.parallelStream().filter(i -> {
            Integer vehicleStatus = i.getVehicleStatus();
            return vehicleStatus.equals(VehicleStatus.driving.value()) || vehicleStatus.equals(VehicleStatus.parking.value());
        }).collect(Collectors.toList());
        int rate = onlineList.size() * 100 / plateNos.size();

        return Result.ok(rate);
    }

    @Accessors(chain = true)
    @Data
    public static class VehicleVideoListVo {

        String driverPhone;
        private String driverName;

        private String VehBindPath;

        private String activationTme;

        private String activationTmeStr;

        private String activeType;

        private String address;

        private String appendixid;

        private String appendixnum;

        private String area;

        private String authority;

        private String brand;

        private String cameraLine;

        private int cameraNum;

        private String carhrough;

        private String carstar;

        private String catage;

        private String circle;

        private String cityAndCountyId;

        private String commType;

        private String company;

        private String container;

        private String containervolume;

        private String createTime;

        private String customMessage;

        private String customNo;

        private int cvehicleId;

        private int datausage;

        private int delFlag;

        private int displayYear;

        private String driverId;


        private String email;

        private String engineNo;

        private String expireDate;

        private String expireDateStr;

        private String extend;

        private String extendtwo;

        private String factoryNo;

        private String frameNo;

        private String goods;

        private int groupId;

        private String groupName;

        private String groupPhone;

        private String grouparea;

        private String groupperson;

        private String iccid;

        private String industry;

        private int initMilage;

        private String installDate;

        private String installPerson;

        private String installPlace;

        private String installStaue;

        private String installType;

        private String ipAddress;

        private int isAcc;

        private int isDangerous;

        private String isLoan;

        private int isStore;

        private int isVender;

        private String license;

        private String manufacturerId;

        private String nextReturnDue;

        private String nextReturnDueStr;

        private String nickName;

        private String operatingTypes;

        private String operator;

        private String organizationType;

        private String owner;

        private String ownerproperty;

        private int percentageOfFlow;

        private int peripheral;

        private String phone;

        private String plate;

        private String plateColor;

        private String producerID;

        private int productType;

        private String provincialId;

        private String readdress;

        private String recordPerson;

        private String remark;

        private int renewNum;

        private String renewalExpireDate;

        private String renewalExpireDateStr;

        private String rephone;

        private String representative;

        private String roadPermit;

        private String salesman;

        private String schooladr;

        private String schooltype;

        private String scope;

        private String seat;

        private String serviceCode;

        private String serviceExpireTime;

        private String serviceProvider;

        private String sex;

        private String sim;

        private String staypoint;

        private int storeState;

        private int storeUserID;

        private String strExpire;

        private String supervosperId;

        private String supervosprName;

        private String terminalIMEI;

        private String terminalId;

        private String terminalNo;

        private String terminalType;

        private String transport;

        private String tripREC;

        private String type;

        private String updateTime;

        private String validitytime;

        private String vehicleColor;

        private int vehicleId;

        private String vehicleLicense;

        private String vehicleShape;

        private int vehicleState;

        private String vehicleType;

        private String vehiclestate;

        private int version;

    }

    @Accessors(chain = true)
    @Data
    public static class VehicleTrackDataListDto {
        /**
         * 车辆id
         */
        String numberPlate;
        String startTime;
        LocalDateTime endTime;
    }

}
