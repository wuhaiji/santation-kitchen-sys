package com.yuntun.sanitationkitchen.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleTypeCode;
import com.yuntun.sanitationkitchen.model.dto.*;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.model.vo.VehicleListVo;
import com.yuntun.sanitationkitchen.model.vo.VehicleTypeGetVo;
import com.yuntun.sanitationkitchen.model.vo.VehicleTypeListVo;
import com.yuntun.sanitationkitchen.service.IVehicleService;
import com.yuntun.sanitationkitchen.service.IVehicleTypeService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 车辆类型表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/vehicleType")
public class VehicleTypeController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IVehicleTypeService iVehicleTypeService;
    @Autowired
    IVehicleService iVehicleService;


    @GetMapping("/list")
    @Limit("system:vehicleType:query")
    public Result<Object> list(VehicleTypeListDto dto) {
        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());
        IPage<VehicleType> iPage;
        iPage = iVehicleTypeService.page(
                new Page<VehicleType>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<VehicleType>()
                        .like(EptUtil.isNotEmpty(dto.getName()), "name", dto.getName())
                        .like(EptUtil.isNotEmpty(dto.getBrand()), "brand", dto.getBrand())
                        .like(EptUtil.isNotEmpty(dto.getModel()), "model", dto.getModel())
                        .likeRight(EptUtil.isNotEmpty(dto.getCreateTime()), "create_time", dto.getCreateTime())
                        .orderByDesc("create_time")

        );

        List<VehicleTypeListVo> vehicleTypeListVos = ListUtil.listMap(VehicleTypeListVo.class, iPage.getRecords());
        RowData<VehicleTypeListVo> data = new RowData<VehicleTypeListVo>()
                .setRows(vehicleTypeListVos)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());

        return Result.ok(data);
    }


    @GetMapping("/options")
    @Limit("system:vehicleType:query")
    public Result<Object> options() {
        List<VehicleType> list = iVehicleTypeService.list();
        List<OptionsVo> optionsVos = list
                .parallelStream()
                .map(i -> new OptionsVo().setLabel(i.getName()).setValue(i.getUid()))
                .collect(Collectors.toList());
        return Result.ok(optionsVos);
    }

    @GetMapping("/get/{uid}")
    @Limit("system:vehicleType:query")
    public Result<Object> get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        VehicleType byId = iVehicleTypeService.getOne(new QueryWrapper<VehicleType>().eq("uid", uid));
        if (EptUtil.isEmpty(byId)) {
            log.error("vehicle->get->查询车辆类型详情失败,uid:{}", uid);
            return Result.error(VehicleTypeCode.ID_NOT_EXIST);
        }
        VehicleTypeGetVo vehicleTypeGetVo = new VehicleTypeGetVo();
        BeanUtils.copyProperties(byId,vehicleTypeGetVo);

        return Result.ok(vehicleTypeGetVo);
    }

    @PostMapping("/save")
    @Limit("system:vehicleType:save")
    public Result<Object> save(VehicleTypeSaveDto dto) {

        ErrorUtil.isStringLengthOutOfRange(dto.getBrand(), 2, 16, "品牌不能为空");
        ErrorUtil.isStringLengthOutOfRange(dto.getName(), 2, 16, "名称不能为空");
        ErrorUtil.isStringLengthOutOfRange(dto.getTrait(), 2, 100, "车辆特性");

        VehicleType vehicleType = new VehicleType().setCreator(UserIdHolder.get()).setUid(SnowflakeUtil.getUnionId());
        BeanUtils.copyProperties(dto, vehicleType);

        boolean save = iVehicleTypeService.save(vehicleType);
        if (!save) {
            log.error("vehicle->save->保存失败,VehicleTypeSaveDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @Limit("system:vehicleType:update")
    public Result<Object> update(VehicleTypeUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "车辆类型uid不能为空");

        VehicleType vehicleType = new VehicleType().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(dto, vehicleType);
        boolean save = iVehicleTypeService.update(vehicleType,
                new QueryWrapper<VehicleType>().eq("uid", dto.getUid())
        );
        if (!save) {
            log.error("vehicle->update->修改车辆类型失败,VehicleUpdateDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.UPDATE_ERROR);
        }
        return Result.ok();

    }

    @PostMapping("/delete/{uid}")
    @Limit("system:vehicleType:delete")
    public Result<Object> delete(@PathVariable("uid") Long uid) {

        //查询车辆类型下面是否有车辆
        List<Vehicle> vehicles = iVehicleService.list(new QueryWrapper<Vehicle>().eq("sanitation_office_id", uid));
        if(EptUtil.isNotEmpty(vehicles)){
            throw new ServiceException(VehicleCode.HAS_VEHICLE_LIST);
        }
        ErrorUtil.isObjectNull(uid, "uid");

        VehicleType vehicleType = iVehicleTypeService.getOne(new QueryWrapper<VehicleType>().eq("uid", uid));
        if (vehicleType == null) {
            log.error("vehicle->delete->uid不存在,uid:{}", uid);
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        boolean b = iVehicleTypeService.remove(new QueryWrapper<VehicleType>().eq("uid", uid));

        if (!b) {
            log.error("vehicle->delete->删除车辆类型失败,uid:{}", uid);
            return Result.error(VehicleTypeCode.DELETE_ERROR);
        }
        return Result.ok();

    }

    @PostMapping("/delete/batch")
    @Limit("system:vehicleType:delete")
    public Result<Object> deleteBatch(@RequestParam("ids") List<Long> ids) {
        ErrorUtil.isCollectionEmpty(ids, "ids");
        boolean b = iVehicleTypeService.remove(new QueryWrapper<VehicleType>().in("uid", ids));
        if (b)
            return Result.ok();
        return Result.error(VehicleCode.DELETE_ERROR);

    }
}
