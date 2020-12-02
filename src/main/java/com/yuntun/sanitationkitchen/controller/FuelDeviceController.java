package com.yuntun.sanitationkitchen.controller;

import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.FuelDeviceDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IFuelDeviceService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 油耗监测设备表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/fuelDevice")
@Slf4j
public class FuelDeviceController {

    @Autowired
    private IFuelDeviceService iFuelDeviceService;

    /**
     * 分页查询油耗
     * @author wujihong
     * @param fuelDeviceDto
     * @since 2020-12-02 11:21
     */
    @Limit("fuelDevice:list")
    @GetMapping("/list")
    public Result list(FuelDeviceDto fuelDeviceDto) {
        ErrorUtil.PageParamError(fuelDeviceDto.getPageSize(), fuelDeviceDto.getPageNo());
        return Result.ok(iFuelDeviceService.findFuelDeviceServiceList(fuelDeviceDto));
    }

    /**
     * 根据uid查询油耗
     * @author wujihong
     * @param uid
     * @since 2020-12-02 11:30
     */
    @Limit("fuelDevice:get")
    @GetMapping("/get/{uid}")
    public Result get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        return Result.ok(iFuelDeviceService.findFuelDeviceServiceByUid(uid));
    }

    /**
     * 插入油耗
     * @author wujihong
     * @param fuelDeviceDto
     * @since 2020-12-02 11:39
     */
    @PostMapping("/save")
    @Limit("fuelDevice:save")
    public Result save(FuelDeviceDto fuelDeviceDto) {
        ErrorUtil.isStringLengthOutOfRange(fuelDeviceDto.getBrand(), 2, 16, "品牌");
        return Result.ok(iFuelDeviceService.insertFuelDevice(fuelDeviceDto));
    }

    /**
     * 根据uid修改油耗
     * @author wujihong
     * @param fuelDeviceDto
     * @since 2020-12-02 11:39
     */
    @PostMapping("/update")
    @Limit("fuelDevice:update")
    public Result update(FuelDeviceDto fuelDeviceDto) {
        ErrorUtil.isObjectNull(fuelDeviceDto.getUid(), "油耗uid");
        return Result.ok(iFuelDeviceService.updateFuelDevice(fuelDeviceDto));
    }

    /**
     * 根据uid删除油耗
     * @author wujihong
     * @param uid
     * @since 2020-12-02 12:12
     */
    @PostMapping("/delete/{uid}")
    @Limit("fuelDevice:delete")
    public Result delete(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "uid");
        return Result.ok(iFuelDeviceService.deleteFuelDevice(uid));
    }

}
