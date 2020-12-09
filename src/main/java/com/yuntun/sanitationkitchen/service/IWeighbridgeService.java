package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeDto;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeSaveDto;
import com.yuntun.sanitationkitchen.model.dto.WeighbridgeUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.Weighbridge;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;
import com.yuntun.sanitationkitchen.model.vo.WeighbridgeVo;

import java.util.List;

/**
 * <p>
 * 地磅表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
public interface IWeighbridgeService extends IService<Weighbridge> {

    RowData<WeighbridgeVo> findWeighbridgeList(WeighbridgeDto dto);

    SelectOptionVo selectWeighbridgeOption();

    WeighbridgeVo findWeighbridgeByUid(Long uid);

    Boolean insertWeighbridge(WeighbridgeDto dto);

    Boolean updateWeighbridge(WeighbridgeDto dto);

    Boolean deleteWeighbridge(List<Long> uids);
}
