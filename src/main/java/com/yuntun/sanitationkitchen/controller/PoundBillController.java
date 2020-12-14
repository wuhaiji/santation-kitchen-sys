package com.yuntun.sanitationkitchen.controller;


import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IPoundBillService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 地磅配置表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@RestController
@RequestMapping("/poundBill")
@Slf4j
public class PoundBillController {

    @Autowired
    IPoundBillService iPoundBillService;

    /**
     * 地磅榜单 下拉框
     *
     * @author wujihong
     * @since 2020-12-02 11:21
     */
    @Limit("data:poundBill:query")
    @RequestMapping("/option")
    public Result selectPoundBillOption() {
        return Result.ok(iPoundBillService.selectPoundBillOption());
    }

    /**
     * 分页查询地磅磅单
     *
     * @param poundBillDto
     * @return
     */
    @Limit("data:poundBill:query")
    @RequestMapping("/list")
    public Result list(PoundBillDto poundBillDto) {
        return Result.ok(iPoundBillService.pagePoundBill(poundBillDto));
        //return Result.ok(iPoundBillService.findPoundBillList(poundBillDto));
    }

    /**
     * 用excel的形式导出榜单
     *
     * @param poundBillDto
     * @param response
     */
    @Limit("data:poundBill:export")
    @RequestMapping(value = "/export")
    public void export(PoundBillDto poundBillDto, HttpServletResponse response) {
        System.out.println("poundBillDto:"+poundBillDto);
        iPoundBillService.exportPoundBill(poundBillDto, response);
    }



}

