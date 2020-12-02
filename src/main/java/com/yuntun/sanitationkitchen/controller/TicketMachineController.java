package com.yuntun.sanitationkitchen.controller;

import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.TicketMachineDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.ITicketMachineService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 分页查询小票机
     * @author wujihong
     * @param ticketMachineDto
     * @since 2020-12-02 11:21
     */
    @Limit("ticketMachine:list")
    @GetMapping("/list")
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
    @Limit("ticketMachine:get")
    @GetMapping("/get/{uid}")
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
    @PostMapping("/save")
    @Limit("ticketMachine:save")
    public Result save(TicketMachineDto ticketMachineDto) {
        return Result.ok(iTicketMachineService.insertTicketMachine(ticketMachineDto));
    }

    /**
     * 根据uid修改小票机
     * @author wujihong
     * @param ticketMachineDto
     * @since 2020-12-02 11:39
     */
    @PostMapping("/update")
    @Limit("ticketMachine:update")
    public Result update(TicketMachineDto ticketMachineDto) {
        ErrorUtil.isObjectNull(ticketMachineDto.getUid(), "uid");
        return Result.ok(iTicketMachineService.updateTicketMachine(ticketMachineDto));
    }

    /**
     * 根据uid删除小票机
     * @author wujihong
     * @param uid
     * @since 2020-12-02 12:12
     */
    @PostMapping("/delete/{uid}")
    @Limit("ticketMachine:delete")
    public Result delete(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "uid");
        return Result.ok(iTicketMachineService.deleteTicketMachine(uid));
    }
}
