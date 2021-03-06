package com.yuntun.sanitationkitchen.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * <p>
 * 磅单统计
 * </p>
 *
 * @author yookfeng
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class PoundBillStatistic {

  /**
   * 磅单总重量
   */
  private Integer totalWeight;
  /**
   * 单日磅单数
   */
  private Integer dayCount;

  /**
   * 单月磅单数
   */
  private Integer monthCount;

  /**
   * 按日期返回：yyyy-MM-dd
   */
  private String date;

  /**
   * 按月份返回：yyyy-MM
   */
  private String month;


}
