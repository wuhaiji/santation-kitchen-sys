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
    @Limit("ticketMachine:list")
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
    @Limit("ticketMachine:list")
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
    @Limit("ticketMachine:get")
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
    @Limit("ticketMachine:save")
    public Result save(@RequestBody TicketMachineDto ticketMachineDto) {
        System.out.println("ticketMachineDto:"+ticketMachineDto);
        return Result.ok(iTicketMachineService.insertTicketMachine(ticketMachineDto));
    }

    /**
     * 根据uid修改小票机
     * @author wujihong
     * @param ticketMachineDto
     * @since 2020-12-02 11:39
     */
    @RequestMapping("/update")
    @Limit("ticketMachine:update")
    public Result update(@RequestBody TicketMachineDto ticketMachineDto) {
        System.out.println("ticketMachineDto:"+ticketMachineDto);
        ErrorUtil.isObjectNull(ticketMachineDto.getUid(), "uid");
        return Result.ok(iTicketMachineService.updateTicketMachine(ticketMachineDto));
    }

    /**
     * 根据uid删除小票机
     * @author wujihong
     * @param uids
     * @since 2020-12-02 12:12
     */
    @RequestMapping("/delete")
    @Limit("ticketMachine:delete")
    public Result delete(@RequestParam("uids[]") List<Long> uids) {
        ErrorUtil.isObjectNull(uids, "uids");
        return Result.ok(iTicketMachineService.deleteTicketMachine(uids));
    }
}
