package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.CronMapper;
import com.yuntun.sanitationkitchen.model.entity.Cron;
import com.yuntun.sanitationkitchen.service.ICronService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-15
 */
@Service
public class CronServiceImpl extends ServiceImpl<CronMapper, Cron> implements ICronService {

}
