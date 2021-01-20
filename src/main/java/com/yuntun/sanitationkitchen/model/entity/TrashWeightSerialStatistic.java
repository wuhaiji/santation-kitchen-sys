package com.yuntun.sanitationkitchen.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 垃圾桶称重流水表
 * </p>
 *
 * @author tang
 * @since 2020-12-14
 */
/**
 * <p>
 * 垃圾桶称重统计
 * </p>
 *
 * @author yookfeng
 * @since 2020-12-02
 */
@Data
@Accessors(chain = true)
public class TrashWeightSerialStatistic {

  /**
   * 垃圾桶称重总重量
   */
  private Double totalWeight;
  /**
   * 单日称重数
   */
  private Integer dayCount;

  /**
   * 单月餐厨单数
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



