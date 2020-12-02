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
import com.yuntun.sanitationkitchen.model.dto.VehicleTypeListDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleTypeSaveDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.VehicleListVo;
import com.yuntun.sanitationkitchen.service.IVehicleTypeService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/list")
    @Limit("vehicleType:list")
    public Result<Object> list(VehicleTypeListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<VehicleType> iPage;
        iPage = iVehicleTypeService.page(
                new Page<VehicleType>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<VehicleType>()
                        .eq(EptUtil.isNotEmpty(dto.getName()), "name", dto.getName())
                        .eq(EptUtil.isNotEmpty(dto.getBrand()), "brand", dto.getBrand())
                        .eq(EptUtil.isNotEmpty(dto.getTrait()), "trait", dto.getTrait())
                        .orderByDesc("create_time")

        );

        List<VehicleListVo> vehicleListVos = ListUtil.listMap(VehicleListVo.class, iPage.getRecords());
        RowData<VehicleListVo> data = new RowData<VehicleListVo>()
                .setRows(vehicleListVos)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());

        return Result.ok(data);
    }


    @GetMapping("/options")
    @Limit("vehicleType:options")
    public Result<Object> options() {
        List<VehicleType> list = iVehicleTypeService.list();

        List<VehicleListVo> vehicleListVos = ListUtil.listMap(VehicleListVo.class, list);
        return Result.ok(vehicleListVos);
    }

    @GetMapping("/get/{uid}")
    @Limit("vehicleType:get")
    public Result<Object> get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        VehicleType byId = iVehicleTypeService.getOne(new QueryWrapper<VehicleType>().eq("uid", uid));

        if (EptUtil.isEmpty(byId)) {
            log.error("vehicle->get->查询车辆类型详情失败,uid:{}", uid);
            return Result.error(VehicleTypeCode.ID_NOT_EXIST);
        }
        return Result.ok(byId);
    }

    @PostMapping("/save")
    @Limit("vehicleType:save")
    public Result<Object> save(VehicleTypeSaveDto dto) {

        ErrorUtil.isStringLengthOutOfRange(dto.getBrand(), 2, 16, "品牌不能为空");
        ErrorUtil.isStringLengthOutOfRange(dto.getName(), 2, 16, "名称不能为空");
        ErrorUtil.isStringLengthOutOfRange(dto.getTrait(), 2, 100, "车辆特性");

        VehicleType vehicleType = new VehicleType();
        BeanUtils.copyProperties(dto, vehicleType);

        boolean save = iVehicleTypeService.save(vehicleType);
        if (!save) {
            log.error("vehicle->save->保存失败,VehicleTypeSaveDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @Limit("vehicleType:update")
    public Result<Object> update(VehicleUpdateDto dto) {

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
    @Limit("vehicleType:delete")
    public Result<Object> delete(@PathVariable("uid") Long uid) {

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
}