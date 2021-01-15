package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.TrashWeightSerialMapper;
import com.yuntun.sanitationkitchen.model.dto.TrashWeightSerialDto;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerial;
import com.yuntun.sanitationkitchen.service.ITrashWeightSerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 垃圾桶称重流水表 服务实现类
 * </p>
 *
 * @author tang
 * @since 2020-12-14
 */
@Service
public class TrashWeightSerialServiceImpl extends ServiceImpl<TrashWeightSerialMapper, TrashWeightSerial> implements ITrashWeightSerialService {

  @Autowired
  private TrashWeightSerialMapper weightSerialMapper;

  @Override
  public Double getTrashDateTotal(TrashWeightSerialDto dto) {
    if (weightSerialMapper.getTrashDateTotal(dto) != null) {
      return weightSerialMapper.getTrashDateTotal(dto);
    } else {
      return 0.0;
    }
  }

  @Override
  public Double countCurrentWeight(TrashWeightSerialDto dto) {
    Double total = weightSerialMapper.countCurrentWeight(dto);
    if (total != null) {
      return total;
    } else {
      return 0D;
    }
  }

}
