package com.yuntun.sanitationkitchen.controller;


import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IPoundBillService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Limit("count:poundBill:query")
    @RequestMapping("/option")
    public Result selectPoundBillOption() {
        return Result.ok(iPoundBillService.selectPoundBillOption());
    }

    @Limit("count:poundBill:query")
    @RequestMapping("/list")
    public Result list(PoundBillDto poundBillDto) {
        ErrorUtil.isObjectNullContent(poundBillDto, "地磅榜单查询信息");
        return Result.ok(iPoundBillService.findPoundBillList(poundBillDto));
    }

    @Limit("count:poundBill:export")
    @RequestMapping("/export")
    public void export(PoundBillDto poundBillDto, HttpServletResponse response) {
        iPoundBillService.exportPoundBill(poundBillDto, response);
    }


}

