package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.bean.PoundBillBean;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.entity.PoundBill;
import com.yuntun.sanitationkitchen.model.entity.PoundBillStatistic;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 地磅配置表 Mapper 接口
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Mapper
public interface PoundBillMapper extends BaseMapper<PoundBill> {

  /**
   * 列出磅单列表
   *
   * @param poundBillDto dto
   * @return
   */
  List<PoundBillBean> listPoundBill(@Param("dto") PoundBillDto poundBillDto);

  /**
   * 列出磅单的总量
   *
   * @param poundBillDto
   * @return
   */
  Long countPoundBill(@Param("dto") PoundBillDto poundBillDto);

  /**
   * 本日磅单总重量
   *
   * @param poundBillDto
   * @return
   */
  PoundBillStatistic getPoundDateTotal(@Param("dto") PoundBillDto poundBillDto);


  List<PoundBillStatistic> getCurrentWeekPoundTotal(@Param("dto") PoundBillDto poundBillDto);

  /**
   * 根据条件，查询垃圾重量
   *
   * @param dto
   * @return
   */
  Integer countCurrentWeight(@Param("dto") PoundBillDto dto);

  List<PoundBillStatistic> getCurrentMonthPoundTotal();

  List<PoundBillStatistic> getCurrentYearPoundTotal();
}
