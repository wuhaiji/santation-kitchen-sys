package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleSaveDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.VehicleListVo;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import com.yuntun.sanitationkitchen.vehicle.api.IVehicle;
import com.yuntun.sanitationkitchen.vehicle.api.VehicleRealtimeStatusAdasDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @Limit("vehicle:list")
    @GetMapping("/list")
    public Result<Object> list(VehicleListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());


        IPage<Vehicle> iPage = iVehicleService.page(
                new Page<Vehicle>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<Vehicle>()
                        .eq(EptUtil.isNotEmpty(dto.getDriverName()), "driver_name", dto.getDriverName())
                        .eq(EptUtil.isNotEmpty(dto.getNumberPlate()), "number_plate", dto.getNumberPlate())
                        .eq(EptUtil.isNotEmpty(dto.getDriverPhone()), "driver_phone", dto.getDriverPhone())
                        .eq(EptUtil.isNotEmpty(dto.getPurchaseDate()), "purchase_date", dto.getPurchaseDate())
                        .eq(EptUtil.isNotEmpty(dto.getSanitationOfficeId()), "sanitation_office_Id", dto.getSanitationOfficeId())
                        .orderByDesc("create_time")
        );

        List<Vehicle> records = iPage.getRecords();
        //获取车辆实时信息
        List<String> plateNos = records.parallelStream().map(Vehicle::getNumberPlate).collect(Collectors.toList());
        List<VehicleRealtimeStatusAdasDto> vehicleRealtimeStatusAdasDtoList = iVehicle
                .ListVehicleRealtimeStatusByPlates(plateNos);

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
            return vehicleListVo;
        }).collect(Collectors.toList());

        RowData<VehicleListVo> data = new RowData<VehicleListVo>()
                .setRows(collect)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());


        return Result.ok(data);
    }


    @Limit("vehicle:options")
    @GetMapping("/options")
    public Result<Object> options() {
        List<Vehicle> list = iVehicleService.list();
        List<VehicleListVo> vehicleListVos = ListUtil.listMap(VehicleListVo.class, list);
        return Result.ok(vehicleListVos);
    }

    @GetMapping("/get/{uid}")
    @Limit("vehicle:get")
    public Result<Object> get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        Vehicle byId = iVehicleService.getOne(new QueryWrapper<Vehicle>().eq("uid", uid));
        if (EptUtil.isNotEmpty(byId))
            return Result.ok(byId);
        return Result.error(VehicleCode.ID_NOT_EXIST);
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

        checkRepeatedValue(dto.getNumberPlate(), dto.getRfid());


        Vehicle role = new Vehicle()
                .setUid(SnowflakeUtil.getUnionId())
                .setDriverName(dto.getDriverName())
                .setPurchaseDate(dto.getPurchaseDate())
                .setRfid(dto.getRfid())
                .setSanitationOfficeId(dto.getSanitationOfficeId())
                .setNumberPlate(dto.getNumberPlate())
                .setDriverPhone(dto.getDriverPhone())
                .setCreator(UserIdHolder.get());

        boolean save = iVehicleService.save(role);
        if (save)
            return Result.ok();
        return Result.error(VehicleCode.SAVE_ERROR);
    }

    private void checkRepeatedValue(String numberPlate, String rfid) {
        List<Vehicle> listNumberPlate = iVehicleService.list(
                new QueryWrapper<Vehicle>().eq("username", numberPlate)
        );
        if (listNumberPlate.size() > 0) {
            throw new ServiceException(VehicleCode.NUMBER_PLATE_ALREADY_EXISTS);
        }

        List<Vehicle> listRfid = iVehicleService.list(
                new QueryWrapper<Vehicle>().eq("username", rfid)
        );
        if (listRfid.size() > 0) {
            throw new ServiceException(VehicleCode.RFID_PLATE_ALREADY_EXISTS);
        }
    }

    @PostMapping("/update")
    @Limit("vehicle:update")
    public Result<Object> update(VehicleUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "车辆uid不能为空");

        checkRepeatedValue(dto.getNumberPlate(), dto.getRfid());

        Vehicle vehicle = new Vehicle().setUpdator(UserIdHolder.get());

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

        if (vehicle == null) {
            log.error("删除车辆异常->uid不存在");
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }
        boolean b = iVehicleService.remove(new QueryWrapper<Vehicle>().eq("uid", uid));
        if (b)
            return Result.ok();
        return Result.error(VehicleCode.DELETE_ERROR);
    }

}
