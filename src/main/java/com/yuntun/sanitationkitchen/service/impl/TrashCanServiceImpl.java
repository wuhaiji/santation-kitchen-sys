package com.yuntun.sanitationkitchen.service.impl;

import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.service.ITrashCanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 垃圾桶表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class TrashCanServiceImpl extends ServiceImpl<TrashCanMapper, TrashCan> implements ITrashCanService {

}
