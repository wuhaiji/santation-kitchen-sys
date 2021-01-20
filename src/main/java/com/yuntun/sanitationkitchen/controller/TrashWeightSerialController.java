package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.PoundBillCode;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.dto.TrashWeightSerialDto;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerial;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.properties.TrashCanSerialProperties;
import com.yuntun.sanitationkitchen.service.ITrashWeightSerialService;
import com.yuntun.sanitationkitchen.util.ExcelUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 垃圾桶称重流水
 *
 * @author tang
 * @since 2020/12/15
 */
@Slf4j
@RestController
@RequestMapping("/trash/weight")
public class TrashWeightSerialController {

  @Autowired
  private ITrashWeightSerialService trashWeightSerialService;

  @Autowired
  private TrashCanSerialProperties trashCanSerialProperties;


  LambdaQueryWrapper<TrashWeightSerial> buildCondition(TrashWeightSerialDto dto) {
    LambdaQueryWrapper<TrashWeightSerial> q = new LambdaQueryWrapper<>();
    q.like(!StringUtils.isBlank(dto.getCode()), TrashWeightSerial::getFacilityCode, dto.getCode())
        .like(!StringUtils.isBlank(dto.getRestaurantName()), TrashWeightSerial::getRestaurantName, dto.getRestaurantName())
        .like(!StringUtils.isBlank(dto.getRfid()), TrashWeightSerial::getTrashCanRfid, dto.getRfid())
        .like(!StringUtils.isBlank(dto.getDriverName()), TrashWeightSerial::getDriverName, dto.getDriverName())
        .orderByDesc(TrashWeightSerial::getCreateTime);
    return q;
  }

  @Limit("data:trashWeightSerial:query")
  @RequestMapping("/page")
  public Result page(TrashWeightSerialDto dto) {
    IPage<TrashWeightSerial> page = new Page<>();
    page.setSize(dto.getPageSize());
    page.setCurrent(dto.getPageNo());
    trashWeightSerialService.page(page, buildCondition(dto));

    RowData<TrashWeightSerial> pageBean = new RowData<TrashWeightSerial>()
        .setRows(page.getRecords())
        .setTotal(page.getTotal());

    return Result.ok(pageBean);
  }

  /**
   * 根据查询条件，统计垃圾重量
   *
   * @param dto
   * @return
   */
  @Limit("data:trashWeightSerial:query")
  @RequestMapping("/count/current/weight")
  public Result countCurrentWeight(TrashWeightSerialDto dto) {
    return Result.ok(trashWeightSerialService.countCurrentWeight(dto));
  }

  /**
   * 当天垃圾桶流水
   *
   * @param dto
   * @return
   */
  @Limit("data:trashWeightSerial:query")
  @RequestMapping("/date")
  public Result getTrashDateTotal(TrashWeightSerialDto dto) {
    return Result.ok(trashWeightSerialService.getTrashDateTotal(dto));
  }

  /**
   * 周餐余数量
   *
   * @param dto
   * @return
   */
  @Limit("data:trashWeightSerial:query")
  @RequestMapping("/week")
  public Result getWeekWeightList(TrashWeightSerialDto dto) {
    System.out.println("传入" + dto.toString());
    return Result.ok(trashWeightSerialService.getWeekWeightList(dto));
  }

  /**
   * 本月餐余数量
   *
   * @return
   */
  @Limit("data:trashWeightSerial:query")
  @RequestMapping("/month")
  public Result getMonthWeightList() {
    System.out.println("查询当月餐余数据！");
    return Result.ok(trashWeightSerialService.getCurrentMonthTrashTotal());
  }

  /**
   * 本年餐余数量
   *
   * @return
   */
  @Limit("data:trashWeightSerial:query")
  @RequestMapping("/year")
  public Result getYearWeightList() {
    System.out.println("查询当年餐余数据！");
    return Result.ok(trashWeightSerialService.getCurrentYearTrashTotal());
  }

  @Limit("data:trashWeightSerial:export")
  @RequestMapping("/export")
  public void export(TrashWeightSerialDto dto, HttpServletResponse response) {

    try {
      ExcelUtil.excelExport(
          response,
          trashCanSerialProperties.getFileName(),
          trashCanSerialProperties.getSheetName(),
          trashWeightSerialService.list(buildCondition(dto)),
          trashCanSerialProperties.getHeaders(),
          trashCanSerialProperties.getColumns());
    } catch (Exception e) {
      log.error("export PoundBill err", e);
      throw new ServiceException(PoundBillCode.EXPORT_EXCEL);
    }

  }

}
