package com.yuntun.sanitationkitchen.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCameraCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleTypeCode;
import com.yuntun.sanitationkitchen.model.code.code40000.WeighbridgeConfigCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleConfigUpdateDto;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeConfigSaveDto;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeConfigUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.VehicleCamera;
import com.yuntun.sanitationkitchen.model.entity.VehicleConfig;
import com.yuntun.sanitationkitchen.model.entity.WeighbridgeConfig;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IWeighbridgeConfigService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 地磅配置表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/weighbridgeConfig")
public class WeighbridgeConfigController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IWeighbridgeConfigService iWeighbridgeConfigService;

    @GetMapping("/get/{uid}")
    @Limit("weighbridgeConfig:get")
    public Result<Object> get(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "参数");

        WeighbridgeConfig byId = iWeighbridgeConfigService.getOne(
                new QueryWrapper<WeighbridgeConfig>().eq("uid", uid)
        );

        if (EptUtil.isEmpty(byId)) {
            log.error("weighbridgeConfig->get->查询车在摄像头失败,uid:{}", uid);
            return Result.error(WeighbridgeConfigCode.ID_NOT_EXIST);
        }
        return Result.ok(byId);

    }

    @PostMapping("/save")
    @Limit("weighbridgeConfig:save")
    public Result<Object> save(WeighbridgeConfigSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getWeighingTolerance(), "称重公差值");

        WeighbridgeConfig entity = new WeighbridgeConfig()
                .setCreator(UserIdHolder.get())
                .setUid(SnowflakeUtil.getUnionId());
        BeanUtils.copyProperties(dto, entity);

        boolean save = iWeighbridgeConfigService.save(entity);
        if (!save) {
            log.error("WeighbridgeConfig->save->保存失败,WeighbridgeConfigSaveDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @Limit("weighbridgeConfig:update")
    public Result<Object> update(WeighbridgeConfigUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "车辆配置uid不能为空");

        WeighbridgeConfig entity = new WeighbridgeConfig().setUpdator(UserIdHolder.get());

        BeanUtils.copyProperties(dto, entity);

        boolean b = iWeighbridgeConfigService.update(
                entity,
                new QueryWrapper<WeighbridgeConfig>().eq("uid", dto.getUid())
        );
        if (!b) {
            log.error("weighbridgeConfig->update->修改地磅配置失败,WeighbridgeConfigUpdateDto:{}", JSON.toJSONString(dto));
            return Result.error(VehicleTypeCode.UPDATE_ERROR);
        }
        return Result.ok();
    }

}
