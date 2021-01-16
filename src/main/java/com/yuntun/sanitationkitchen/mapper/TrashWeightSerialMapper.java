package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.model.dto.TrashWeightSerialDto;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerial;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerialStatistic;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 垃圾桶称重流水表 Mapper 接口
 * </p>
 *
 * @author tang
 * @since 2020-12-14
 */
public interface TrashWeightSerialMapper extends BaseMapper<TrashWeightSerial> {

  /**
   * @param dto
   * @return
   */
  TrashWeightSerialStatistic getTrashDateTotal(@Param("dto") TrashWeightSerialDto dto);

  Double countCurrentWeight(@Param("dto") TrashWeightSerialDto dto);
}
