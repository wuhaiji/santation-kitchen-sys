package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.dto.TrashWeightSerialDto;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerial;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerialStatistic;

/**
 * <p>
 * 垃圾桶称重流水表 服务类
 * </p>
 *
 * @author tang
 * @since 2020-12-14
 */
public interface ITrashWeightSerialService extends IService<TrashWeightSerial> {

  /**
   * 单日垃圾桶总量
   * @param dto
   * @return
   */
  TrashWeightSerialStatistic getTrashDateTotal(TrashWeightSerialDto dto);

  Double countCurrentWeight(TrashWeightSerialDto dto);
}
