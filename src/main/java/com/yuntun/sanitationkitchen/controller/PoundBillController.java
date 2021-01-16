package com.yuntun.sanitationkitchen.controller;


import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IPoundBillService;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
   * 根据查询条件，统计垃圾重量
   *
   * @param poundBillDto
   * @return
   */
  @Limit("data:poundBill:query")
  @RequestMapping("/count/current/weight")
  public Result countCurrentWeight(PoundBillDto poundBillDto) {
    return Result.ok(iPoundBillService.countCurrentWeight(poundBillDto));
  }


  /**
   * 当天地磅磅单
   *
   * @param poundBillDto
   * @return
   */
  @Limit("data:poundBill:query")
  @RequestMapping("/date")
  public Result getPoundDateTotal(PoundBillDto poundBillDto) {
    return Result.ok(iPoundBillService.getPoundDateTotal(poundBillDto));
  }


  /**
   * 周地磅磅单数量
   *
   * @param poundBillDto
   * @return
   */
  @Limit("data:poundBill:query")
  @RequestMapping("/week")
  public Result getPoundWeekTotal(PoundBillDto poundBillDto) {
    return Result.ok(iPoundBillService.getPoundDateTotal(poundBillDto));
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
    System.out.println("poundBillDto:" + poundBillDto);
    iPoundBillService.exportPoundBill(poundBillDto, response);
  }


}

