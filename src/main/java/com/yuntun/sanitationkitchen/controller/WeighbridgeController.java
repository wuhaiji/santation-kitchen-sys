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
import com.yuntun.sanitationkitchen.model.code.code40000.WeighbridgeCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleUpdateDto;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeListDto;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeSaveDto;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.model.entity.Weighbridge;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.WeighbridgeListVo;
import com.yuntun.sanitationkitchen.model.vo.WeighbridgeOptionsVo;
import com.yuntun.sanitationkitchen.service.IWeighbridgeService;
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
 * 地磅表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/weighbridge")
public class WeighbridgeController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IWeighbridgeService iWeighbridgeService;

    
    @GetMapping("/list")
    @Limit("weighbridge:list")
    public Result<Object> list(WeighbridgeListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Weighbridge> iPage = iWeighbridgeService.page(
                new Page<Weighbridge>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<Weighbridge>()
                        .eq(EptUtil.isNotEmpty(dto.getBrand()), "brand", dto.getBrand())

                        .orderByDesc("create_time")

        );

        List<WeighbridgeListVo> vehicleListVos = ListUtil.listMap(WeighbridgeListVo.class, iPage.getRecords());

        RowData<WeighbridgeListVo> data = new RowData<WeighbridgeListVo>()
                .setRows(vehicleListVos)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());

        return Result.ok(data);
    }


    @GetMapping("/options")
    @Limit("weighbridge:options")
    public Result<Object> options() {

        List<Weighbridge> list = iWeighbridgeService.list();

        List<WeighbridgeOptionsVo> vehicleListVos = ListUtil.listMap(WeighbridgeOptionsVo.class, list);

        return Result.ok(vehicleListVos);
    }

    @GetMapping("/get/{uid}")
    @Limit("weighbridge:get")
    public Result<Object> get(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "参数");

        Weighbridge byId = iWeighbridgeService.getOne(new QueryWrapper<Weighbridge>().eq("uid", uid));

        if (EptUtil.isEmpty(byId)) {
            log.error("weighbridge->get->查询车辆类型详情失败,uid:{}", uid);
            return Result.error(WeighbridgeCode.ID_NOT_EXIST);
        }
        return Result.ok(byId);
    }

    @PostMapping("/save")
    @Limit("weighbridge:save")
    public Result<Object> save(WeighbridgeSaveDto dto) {

        ErrorUtil.isStringLengthOutOfRange(dto.getBrand(), 2, 30, "品牌不能为空");
        ErrorUtil.isStringLengthOutOfRange(dto.getDeviceCode(), 2, 30, "设备名称");
        ErrorUtil.isStringLengthOutOfRange(dto.getModel(), 2, 30, "型号");
        ErrorUtil.isStringLengthOutOfRange(dto.getRfid(), 2, 30, "RFID");
        ErrorUtil.isObjectNull(dto.getMaxWeighing(), "最大称重量(kg)");
        ErrorUtil.isObjectNull(dto.getSanitationOfficeId(), "所属机构id");

        Weighbridge entity = new Weighbridge();
        BeanUtils.copyProperties(dto, entity);

        boolean save = iWeighbridgeService.save(entity);
        if (!save) {
            log.error("weighbridge->save->保存地磅失败,WeighbridgeSaveDto:{}", JSON.toJSONString(dto));
            return Result.error(WeighbridgeCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @Limit("weighbridge:update")
    public Result<Object> update(WeighbridgeUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "车辆类型uid不能为空");

        Weighbridge entity = new Weighbridge().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(dto, entity);
        boolean save = iWeighbridgeService.update(
                entity,
                new QueryWrapper<Weighbridge>().eq("uid", dto.getUid())
        );
        if (!save) {
            log.error("vehicle->update->修改地磅失败,WeighbridgeUpdateDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.UPDATE_ERROR);
        }
        return Result.ok();

    }

    @PostMapping("/delete/{uid}")
    @Limit("weighbridge:delete")
    public Result<Object> delete(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "uid");

        Weighbridge vehicleType = iWeighbridgeService.getOne(new QueryWrapper<Weighbridge>().eq("uid", uid));
        if (vehicleType == null) {
            log.error("weighbridge->delete->uid不存在,uid:{}", uid);
            throw new ServiceException(WeighbridgeCode.ID_NOT_EXIST);
        }

        boolean b = iWeighbridgeService.remove(new QueryWrapper<Weighbridge>().eq("uid", uid));

        if (!b) {
            log.error("weighbridge->delete->删除地磅失败,uid:{}", uid);
            return Result.error(WeighbridgeCode.DELETE_ERROR);
        }
        return Result.ok();

    }
}
