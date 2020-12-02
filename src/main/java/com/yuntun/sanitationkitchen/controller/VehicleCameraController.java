package com.yuntun.sanitationkitchen.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCameraCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleCameraListDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleCameraSaveDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleCameraUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.VehicleCamera;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.VehicleCameraListVo;
import com.yuntun.sanitationkitchen.model.vo.VehicleCameraOptionsVo;
import com.yuntun.sanitationkitchen.service.IVehicleCameraService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 车载摄像头表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/vehicleCamera")
public class VehicleCameraController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IVehicleCameraService iVehicleCameraService;


    @GetMapping("/list")
    @Limit("vehicleCamera:list")
    public Result<Object> list(VehicleCameraListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<VehicleCamera> iPage = iVehicleCameraService.page(
                new Page<VehicleCamera>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<VehicleCamera>()
                        .eq(EptUtil.isNotEmpty(dto.getBrand()), "brand", dto.getBrand())
                        .eq(EptUtil.isNotEmpty(dto.getDeviceCode()), "device_code", dto.getDeviceCode())
                        .eq(EptUtil.isNotEmpty(dto.getDisabled()), "disabled", dto.getDisabled())
                        .eq(EptUtil.isNotEmpty(dto.getModel()), "model", dto.getModel())
                        .eq(EptUtil.isNotEmpty(dto.getSanitationOfficeId()), "sanitation_office_id", dto.getSanitationOfficeId())
                        .eq(EptUtil.isNotEmpty(dto.getStatus()), "status", dto.getStatus())

                        .orderByDesc("create_time")

        );

        List<VehicleCameraListVo> vehicleListVos = ListUtil.listMap(VehicleCameraListVo.class, iPage.getRecords());
        RowData<VehicleCameraListVo> data = new RowData<VehicleCameraListVo>()
                .setRows(vehicleListVos)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());

        return Result.ok(data);
    }


    @GetMapping("/options")
    @Limit("vehicleCamera:options")
    public Result<Object> options() {

        List<VehicleCamera> list = iVehicleCameraService.list(
                new QueryWrapper<VehicleCamera>()
        );

        List<VehicleCameraOptionsVo> vehicleListVos = ListUtil.listMap(VehicleCameraOptionsVo.class, list);

        return Result.ok(vehicleListVos);
    }

    @GetMapping("/get/{uid}")
    @Limit("vehicleCamera:get")
    public Result<Object> get(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "参数");

        VehicleCamera byId = iVehicleCameraService.getOne(new QueryWrapper<VehicleCamera>().eq("uid", uid));

        if (EptUtil.isEmpty(byId)) {
            log.error("vehicle->get->查询车在摄像头失败,uid:{}", uid);
            return Result.error(VehicleCameraCode.ID_NOT_EXIST);
        }
        return Result.ok(byId);

    }

    @PostMapping("/save")
    @Limit("vehicleCamera:save")
    public Result<Object> save(VehicleCameraSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getBrand(), "品牌");
        ErrorUtil.isObjectNull(dto.getDeviceCode(), "设备编号");
        ErrorUtil.isObjectNull(dto.getDeviceName(), "设备名称");
        ErrorUtil.isObjectNull(dto.getModel(), "型号");
        ErrorUtil.isObjectNull(dto.getSanitationOfficeId(), "所属机构id");
        ErrorUtil.isObjectNull(dto.getVehicleId(), "所属车辆id");

        checkRepeatedValue(dto.getDeviceCode(), dto.getDeviceName());

        VehicleCamera entity = new VehicleCamera()
                .setCreator(UserIdHolder.get())
                .setUid(SnowflakeUtil.getUnionId());
        BeanUtils.copyProperties(dto, entity);

        boolean b = iVehicleCameraService.save(entity);
        if (!b) {
            log.error("vehicleCamera->save->保存失败,VehicleCameraSaveDto:{}", JSON.toJSONString(dto));
            throw new ServiceException(VehicleCameraCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    private void checkRepeatedValue(String deviceCode, String deviceName) {
        List<VehicleCamera> listDeviceCode = iVehicleCameraService.list(
            new QueryWrapper<VehicleCamera>().eq("device_code", deviceCode)
        );
        if (listDeviceCode.size() > 0) {
            log.error("vehicleCamera->save->保存时异常，设备编码重复deviceCode：{}", deviceCode);
            throw new ServiceException(VehicleCameraCode.DEVICE_CODE_ALREADY_EXISTS);
        }

        List<VehicleCamera> listDeviceName = iVehicleCameraService.list(
            new QueryWrapper<VehicleCamera>().eq("device_name", deviceName)
        );
        if (listDeviceName.size() > 0) {
            log.error("vehicleCamera->save->保存时异常，设备名称重复deviceName：{}", deviceName);
            throw new ServiceException(VehicleCode.RFID_PLATE_ALREADY_EXISTS);
        }
    }

    @PostMapping("/update")
    @Limit("vehicleCamera:update")
    public Result<Object> update(VehicleCameraUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "uid不能为空");

        checkRepeatedValue(dto.getDeviceCode(), dto.getDeviceName());

        VehicleCamera entity = new VehicleCamera().setUpdator(UserIdHolder.get());

        BeanUtils.copyProperties(dto, entity);

        boolean b = iVehicleCameraService.update(entity,
                new QueryWrapper<VehicleCamera>().eq("uid", dto.getUid())
        );

        if (!b) {
            log.error("vehicleCamera->update->修改失败,VehicleCameraUpdateDto:{}", JSON.toJSONString(dto));
            throw new ServiceException(VehicleCameraCode.UPDATE_ERROR);
        }
        return Result.ok();


    }

    @PostMapping("/delete/{id}")
    @Limit("vehicleCamera:delete")
    public Result<Object> delete(@PathVariable("id") Long uid) {

        ErrorUtil.isObjectNull(uid, "id");

        VehicleCamera entity = iVehicleCameraService.getOne(new QueryWrapper<VehicleCamera>().eq("uid", uid));

        if (entity == null) {
            log.error("vehicleCamera->delete->删除异常，uid不存在,uid:{}", uid);
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        boolean b = iVehicleCameraService.remove(new QueryWrapper<VehicleCamera>().eq("uid", uid));

        if (!b) {
            log.error("vehicleCamera->delete->删除失败,id:{}", uid);
            throw new ServiceException(VehicleCameraCode.UPDATE_ERROR);
        }
        return Result.ok();
    }

}
