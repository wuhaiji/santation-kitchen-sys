package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.aop.Limit;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.interceptor.UserIdHolder;
import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.code.code20000.PermissionCode;
import com.yuntun.sanitationkitchen.model.code.code20000.RoleCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleTypeCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleSaveDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleTypeListDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.VehicleListVo;
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
    @Limit("vehicle:list")
    @GetMapping("/list")
    public Result<Object> list(VehicleTypeListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<VehicleType> iPage;
        try {
            iPage = iVehicleTypeService.page(
                    new Page<VehicleType>()
                            .setSize(dto.getPageSize())
                            .setCurrent(dto.getPageNo()),
                    new QueryWrapper<VehicleType>()
                            .eq(EptUtil.isNotEmpty(dto.getName()),"name",dto.getName())
                            .eq(EptUtil.isNotEmpty(dto.getBrand()),"brand",dto.getBrand())
                            .eq(EptUtil.isNotEmpty(dto.getTrait()),"trait",dto.getTrait())
                            .orderByDesc("create_time")

            );
        } catch (Exception e) {
            log.error("Exception:",e);
            throw new ServiceException(CommonCode.SERVER_ERROR);
        }
        List<VehicleListVo> vehicleListVos = ListUtil.listMap(VehicleListVo.class, iPage.getRecords());
        RowData<VehicleListVo> data = new RowData<VehicleListVo>()
                .setRows(vehicleListVos)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }


    @Limit("vehicle:options")
    @GetMapping("/options")
    public Result<Object> options() {
        List<VehicleType> list;
        try {
            list = iVehicleTypeService.list();
        } catch (Exception e) {
            log.error("Exception:",e);
            throw new ServiceException(CommonCode.SERVER_ERROR);
        }
        List<VehicleListVo> vehicleListVos = ListUtil.listMap(VehicleListVo.class, list);
        return Result.ok(vehicleListVos);
    }

    @GetMapping("/get/{id}")
    @Limit("vehicle:get")
    public Result<Object> get(@PathVariable("id") Long id) {
        ErrorUtil.isObjectNull(id, "参数");
        try {
            VehicleType byId = iVehicleTypeService.getById(id);
            if (EptUtil.isNotEmpty(byId))
                return Result.ok(byId);
            return Result.error(VehicleTypeCode.UPDATE_ERROR);
        } catch (Exception e) {
            log.error("Exception:",e);
            throw new ServiceException(CommonCode.SERVER_ERROR);
        }

    }

    // @PostMapping("/save")
    // @Limit("vehicle:save")
    // public Result<Object> save(VehicleSaveDto dto) {
    //
    //     ErrorUtil.isStringLengthOutOfRange(dto.getDriverName(), 2, 16, "司机名称");
    //     ErrorUtil.isObjectNull(dto.getDriverPhone(), "电话");
    //     ErrorUtil.isObjectNull(dto.getNumberPlate(), "车牌");
    //     ErrorUtil.isObjectNull(dto.getPurchaseDate(), "购买日期");
    //     ErrorUtil.isObjectNull(dto.getRfid(), "RFID");
    //     ErrorUtil.isObjectNull(dto.getSanitationOfficeId(), "所属机构id");
    //
    //     Vehicle role = new Vehicle()
    //             .setUid(SnowflakeUtil.getUnionId())
    //             .setDriverName(dto.getDriverName())
    //             .setPurchaseDate(dto.getPurchaseDate())
    //             .setRfid(dto.getRfid())
    //             .setSanitationOfficeId(dto.getSanitationOfficeId())
    //             .setNumberPlate(dto.getNumberPlate())
    //             .setDriverPhone(dto.getDriverPhone())
    //             .setCreator(UserIdHolder.get());
    //
    //     try {
    //         boolean save = i.save(role);
    //         if (save)
    //             return Result.ok();
    //         return Result.error(VehicleTypeCode.UPDATE_ERROR);
    //     } catch (Exception e) {
    //         log.error("Exception:",e);
    //         throw new ServiceException(CommonCode.SERVER_ERROR);
    //     }
    //
    // }
    //
    // @PostMapping("/update")
    // @Limit("vehicle:update")
    // public Result<Object> update(VehicleUpdateDto dto) {
    //
    //     ErrorUtil.isObjectNull(dto.getUid(), "车辆uid不能为空");
    //
    //     Vehicle vehicle = new Vehicle().setUpdator(UserIdHolder.get());
    //     BeanUtils.copyProperties(dto, vehicle);
    //     try {
    //         boolean save = iVehicleService.update(vehicle,
    //                 new QueryWrapper<Vehicle>().eq("uid", dto.getUid())
    //         );
    //         if (save)
    //             return Result.ok();
    //         return Result.error(VehicleTypeCode.UPDATE_ERROR);
    //     } catch (Exception e) {
    //         log.error("Exception:",e);
    //         throw new ServiceException(CommonCode.SERVER_ERROR);
    //     }
    //
    // }
    //
    // @PostMapping("/delete/{id}")
    // @Limit("vehicle:delete")
    // public Result<Object> delete(@PathVariable("id") Long id) {
    //     ErrorUtil.isObjectNull(id, "id");
    //     Vehicle uid = null;
    //     try {
    //         uid = iVehicleService.getOne(new QueryWrapper<Vehicle>().eq("uid", id));
    //     } catch (Exception e) {
    //         log.error("Exception",e);
    //         throw new ServiceException(CommonCode.SERVER_ERROR);
    //     }
    //     if(uid==null){
    //         log.error("删除车辆异常->uid不存在");
    //         throw new ServiceException(VehicleCode.ID_NOT_EXIST);
    //     }
    //     try {
    //         boolean b = iVehicleService.removeById(id);
    //         if (b)
    //             return Result.ok();
    //         return Result.error(RoleCode.DELETE_ERROR);
    //     } catch (Exception e) {
    //         log.error("Exception:", e);
    //         throw new ServiceException(RoleCode.DELETE_ERROR);
    //     }
    // }
}
