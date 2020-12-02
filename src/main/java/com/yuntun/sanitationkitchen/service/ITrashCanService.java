package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.dto.TrashCanDto;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.TrashCanVo;

/**
 * <p>
 * 垃圾桶表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
public interface ITrashCanService extends IService<TrashCan> {

    RowData<TrashCanVo> findTrashCanList(TrashCanDto trashCanDto);

    TrashCanVo findTrashCanByUid(Long uid);

    Boolean insertTrashCan(TrashCanDto trashCanDto);

    Boolean updateTrashCan(TrashCanDto trashCanDto);

    Boolean deleteTrashCan(Long uid);
}
