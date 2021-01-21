package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.TrashWeightSerialMapper;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.dto.TrashWeightSerialDto;
import com.yuntun.sanitationkitchen.model.entity.PoundBillStatistic;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerial;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerialStatistic;
import com.yuntun.sanitationkitchen.service.ITrashWeightSerialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.yuntun.sanitationkitchen.util.FormatDateUtils.getDay2;

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
  public TrashWeightSerialStatistic getTrashDateTotal(TrashWeightSerialDto dto) {
    TrashWeightSerialStatistic trashWeightSerialStatistic = weightSerialMapper.getTrashDateTotal(dto);
    if (trashWeightSerialStatistic != null) {
      return trashWeightSerialStatistic;
    } else {
      return null;
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

  @Override
  public List<TrashWeightSerialStatistic> getCurrentWeekTrashTotal(TrashWeightSerialDto dto) {
    return weightSerialMapper.getCurrentWeekTrashTotal(dto);
  }

  @Override
  public List<TrashWeightSerialStatistic> getCurrentMonthTrashTotal() {
    return weightSerialMapper.getCurrentMonthTrashTotal();
  }

  @Override
  public List<TrashWeightSerialStatistic> getCurrentYearTrashTotal() {
    return weightSerialMapper.getCurrentYearTrashTotal();
  }

}
