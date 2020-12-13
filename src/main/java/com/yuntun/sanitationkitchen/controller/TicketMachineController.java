package com.yuntun.sanitationkitchen.controller;

import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.TicketMachineDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.ITicketMachineService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 小票机 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/ticketMachine")
public class TicketMachineController {

    @Autowired
    private ITicketMachineService iTicketMachineService;

    /**
     * 小票机 下拉框
     *
     * @author wujihong
     * @since 2020-12-02 11:21
     */
    @Limit("facilitiesAndEquipment:ticketMachine:query")
    @RequestMapping("/option")
    public Result selectTicketMachineOption() {
        return Result.ok(iTicketMachineService.selectTicketMachineOption());
    }

    /**
     * 分页查询小票机
     * @author wujihong
     * @param ticketMachineDto
     * @since 2020-12-02 11:21
     */
    @Limit("facilitiesAndEquipment:ticketMachine:query")
    @RequestMapping("/list")
    public Result list(TicketMachineDto ticketMachineDto) {
        ErrorUtil.PageParamError(ticketMachineDto.getPageSize(), ticketMachineDto.getPageNo());
        return Result.ok(iTicketMachineService.findTicketMachineList(ticketMachineDto));
    }

    /**
     * 根据uid查询小票机
     * @author wujihong
     * @param uid
     * @since 2020-12-02 11:30
     */
    @Limit("facilitiesAndEquipment:ticketMachine:query")
    @RequestMapping("/get/{uid}")
    public Result get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        return Result.ok(iTicketMachineService.findTicketMachineByUid(uid));
    }

    /**
     * 插入小票机
     * @author wujihong
     * @param ticketMachineDto
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/save")
    @Limit("facilitiesAndEquipment:ticketMachine:save")
    public Result save(@RequestBody TicketMachineDto ticketMachineDto) {
        ErrorUtil.isObjectNullContent(ticketMachineDto, "小票机信息");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getDeviceCode(), 2, 30, "设备编号");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getDeviceName(), 2, 30, "设备名称");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getBrand(), 2, 30, "品牌");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getModel(), 2, 30, "型号");
        ErrorUtil.isObjectNull(ticketMachineDto.getSanitationOfficeId(), "所属机构");
        ErrorUtil.isObjectNull(ticketMachineDto.getVehicleId(), "所在车辆");
        ErrorUtil.isObjectNull(ticketMachineDto.getStatus(), "状态");
        return Result.ok(iTicketMachineService.insertTicketMachine(ticketMachineDto));
    }

    /**
     * 根据uid修改小票机
     * @author wujihong
     * @param ticketMachineDto
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/update")
    @Limit("facilitiesAndEquipment:ticketMachine:update")
    public Result update(@RequestBody TicketMachineDto ticketMachineDto) {
        ErrorUtil.isObjectNullContent(ticketMachineDto, "小票机信息");
        ErrorUtil.isObjectNull(ticketMachineDto.getUid(), "uid");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getDeviceCode(), 2, 30, "设备编号");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getDeviceName(), 2, 30, "设备名称");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getBrand(), 2, 30, "品牌");
        ErrorUtil.isStringLengthOutOfRange(ticketMachineDto.getModel(), 2, 30, "型号");
        ErrorUtil.isObjectNull(ticketMachineDto.getSanitationOfficeId(), "所属机构");
        ErrorUtil.isObjectNull(ticketMachineDto.getVehicleId(), "所在车辆");
        ErrorUtil.isObjectNull(ticketMachineDto.getStatus(), "状态");
        ErrorUtil.isObjectNull(ticketMachineDto.getCreateTime(), "创建时间");
        return Result.ok(iTicketMachineService.updateTicketMachine(ticketMachineDto));
    }

    /**
     * 根据uid删除小票机
     * @author wujihong
     * @param uids
     * @since 2020-12-02 12:12
     */
    @RequestMapping("/delete")
    @Limit("facilitiesAndEquipment:ticketMachine:delete")
    public Result delete(@RequestParam(name = "uids[]", required = false) List<Long> uids) {
        ErrorUtil.isCollectionEmpty(uids,"uid");
        return Result.ok(iTicketMachineService.deleteTicketMachine(uids));
    }
}
