package com.yuntun.sanitationkitchen.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleTypeCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleConfigSaveDto;
import com.yuntun.sanitationkitchen.model.dto.VehicleConfigUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.VehicleConfig;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IVehicleConfigService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 车辆配置表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/vehicleConfig")
public class VehicleConfigController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IVehicleConfigService iVehicleConfigService;

    @PostMapping("/save")
    @Limit("vehicleConfig:save")
    public Result<Object> save(VehicleConfigSaveDto dto) {

        ErrorUtil.isNumberValueLe(dto.getFuelFrequency(), 0, "油耗监测频率");
        ErrorUtil.isObjectNull(dto.getSpeedMax(), "车辆最高时速");

        VehicleConfig entity = new VehicleConfig()
                .setCreator(UserIdHolder.get())
                .setUid(SnowflakeUtil.getUnionId());
        BeanUtils.copyProperties(dto, entity);

        boolean save = iVehicleConfigService.save(entity);
        if (!save) {
            log.error("vehicle->save->保存失败,VehicleTypeSaveDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @Limit("vehicleConfig:update")
    public Result<Object> update(VehicleConfigUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "车辆配置uid不能为空");

        VehicleConfig entity = new VehicleConfig().setUpdator(UserIdHolder.get());

        BeanUtils.copyProperties(dto, entity);
        boolean save = iVehicleConfigService.update(entity,
                new QueryWrapper<VehicleConfig>().eq("uid", dto.getUid())
        );
        if (!save) {
            log.error("vehicle->update->修改车辆配置失败,VehicleUpdateDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.UPDATE_ERROR);
        }
        return Result.ok();

    }

}
