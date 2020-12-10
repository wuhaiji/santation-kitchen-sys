package com.yuntun.sanitationkitchen.controller;

import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IWeighbridgeService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    @RequestMapping("/list")
    @Limit("facilitiesAndEquipment:weighbridge:query")
    public Result<Object> list(WeighbridgeDto dto) {
        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());
        return Result.ok(iWeighbridgeService.findWeighbridgeList(dto));
    }


    @RequestMapping("/option")
    @Limit("facilitiesAndEquipment:weighbridge:query")
    public Result<Object> options() {
        return Result.ok(iWeighbridgeService.selectWeighbridgeOption());
    }

    @RequestMapping("/get/{uid}")
    @Limit("facilitiesAndEquipment:weighbridge:query")
    public Result<Object> get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        return Result.ok(iWeighbridgeService.findWeighbridgeByUid(uid));
    }

    @RequestMapping("/save")
    @Limit("facilitiesAndEquipment:weighbridge:save")
    public Result<Object> save(@RequestBody WeighbridgeDto dto) {
        ErrorUtil.isObjectNullContent(dto, "地磅信息");
        ErrorUtil.isStringLengthOutOfRange(dto.getBrand(), 2, 30, "品牌不能为空");
        ErrorUtil.isStringLengthOutOfRange(dto.getDeviceCode(), 2, 30, "设备名称");
        ErrorUtil.isStringLengthOutOfRange(dto.getModel(), 2, 30, "型号");
        ErrorUtil.isStringLengthOutOfRange(dto.getRfid(), 2, 30, "RFID");
        ErrorUtil.isObjectNull(dto.getMaxWeighing(), "最大称重量(kg)");
        ErrorUtil.isObjectNull(dto.getSanitationOfficeId(), "所属机构id");

        return Result.ok(iWeighbridgeService.insertWeighbridge(dto));
    }

    @RequestMapping("/update")
    @Limit("facilitiesAndEquipment:weighbridge:update")
    public Result<Object> update(@RequestBody WeighbridgeDto dto) {
        ErrorUtil.isObjectNull(dto.getUid(), "车辆类型uid不能为空");
        return Result.ok(iWeighbridgeService.updateWeighbridge(dto));
    }

    @RequestMapping("/delete")
    @Limit("facilitiesAndEquipment:weighbridge:delete")
    public Result<Object> delete(@RequestParam(value = "uids[]", required = false) List<Long> uids) {
        ErrorUtil.isListEmpty(uids,"uid");
        return Result.ok(iWeighbridgeService.deleteWeighbridge(uids));
    }
}
