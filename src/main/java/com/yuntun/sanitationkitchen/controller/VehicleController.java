package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.bean.VehicleBean;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.TicketMachineDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleSaveDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.model.vo.TicketMachineVo;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/list")
    @Limit("vehicle:query")
    public Result<Object> list(VehicleListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Vehicle> iPage = iVehicleService.listPage(dto);

        List<Vehicle> records = iPage.getRecords();
        // 获取车辆实时信息
        List<String> plateNos = records.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
        List<VehicleRealtimeStatusAdasDto> vehicleRealtimeStatusAdasDtoList = iVehicle
                .ListVehicleRealtimeStatusByPlates(plateNos);
        log.info("车辆实时状态列表：{}", vehicleRealtimeStatusAdasDtoList);

        Map<String, Vehicle> vehicleMap = records.parallelStream().collect(Collectors.toMap(i -> i.getNumberPlate(), i -> i));


        //查询来源云的车辆集合
        List<VehicleBean> vehicleBeans = iVehicle.list();
        // 找出本系统中对应的车辆
        vehicleBeans.parallelStream().map(i -> {
            Vehicle vehicle = vehicleMap.get(i.getPlateNo());
            if (vehicle != null) {
                return i;
            }
            return null;
        }).collect(Collectors.toList());

        //查询所属单位名称
        List<Long> sanitationOfficeIds = records.parallelStream().map(Vehicle::getSanitationOfficeId).collect(Collectors.toList());

        List<SanitationOffice> sanitationOffices;

        if (EptUtil.isNotEmpty(sanitationOfficeIds)) {
            sanitationOffices = iSanitationOfficeService.list(new QueryWrapper<SanitationOffice>().in("uid", sanitationOfficeIds));
        } else {
            sanitationOffices = new ArrayList<>();
        }
        Map<Long, SanitationOffice> sanitationOfficeMap = sanitationOffices.parallelStream().collect(Collectors.toMap(SanitationOffice::getUid, i -> i));


        List<VehicleListVo> collect = records.parallelStream().map(i -> {
            VehicleListVo vehicleListVo = new VehicleListVo();
            BeanUtils.copyProperties(i, vehicleListVo);
            //循环找出油量信息和在线离线信息
            for (VehicleRealtimeStatusAdasDto status : vehicleRealtimeStatusAdasDtoList) {
                if (status.getPlate().equals(vehicleListVo.getNumberPlate())) {
                    vehicleListVo.setStatus(status.getVehicleStatus());
                    String oil = status.getOil();
                    if (EptUtil.isEmpty(oil)) {
                        vehicleListVo.setFuelRemaining(0.0);
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

        ErrorUtil.isStringLengthOutOfRange(dto.getDriverName(), 2, 16, "司机名称");
        ErrorUtil.isObjectNull(dto.getDriverPhone(), "电话");
        ErrorUtil.isObjectNull(dto.getNumberPlate(), "车牌");
        ErrorUtil.isObjectNull(dto.getPurchaseDate(), "购买日期");
        ErrorUtil.isObjectNull(dto.getRfid(), "RFID");
        ErrorUtil.isObjectNull(dto.getSanitationOfficeId(), "所属机构id");
        ErrorUtil.isObjectNull(dto.getTypeId(), "车辆类型");

        checkRepeatedValue(dto.getNumberPlate(), dto.getRfid());

        SanitationOffice sanitationOffice = iSanitationOfficeService.getOne(
                new QueryWrapper<SanitationOffice>().eq("uid", dto.getSanitationOfficeId())
        );


        Vehicle role = new Vehicle()
                .setUid(SnowflakeUtil.getUnionId())
                .setDriverName(dto.getDriverName())
                .setPurchaseDate(dto.getPurchaseDate())
                .setRfid(dto.getRfid())
                .setSanitationOfficeId(dto.getSanitationOfficeId())
                .setSanitationOfficeName(sanitationOffice.getName())
                .setNumberPlate(dto.getNumberPlate())
                .setDriverPhone(dto.getDriverPhone())
                .setTypeId(dto.getTypeId())
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
        RowData<TicketMachineVo> ticketMachineList = iTicketMachineService.findTicketMachineList(new TicketMachineDto().setVehicleId(uid));
        if (ticketMachineList.getRows() != null) {
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
    public Result<Object> deleteBatch(@RequestParam("ids") List<Long> ids) {
        ErrorUtil.isCollectionEmpty(ids, "ids");
        boolean b = iVehicleService.remove(new QueryWrapper<Vehicle>().in("uid", ids));
        if (b)
            return Result.ok();
        return Result.error(VehicleCode.DELETE_ERROR);

    }

}
