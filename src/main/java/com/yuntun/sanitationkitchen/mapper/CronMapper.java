package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.model.entity.Cron;
import org.apache.ibatis.annotations.Mapper;

/**
* <p>
    * 定时任务表 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-12-15
*/
@Mapper
public interface CronMapper extends BaseMapper<Cron> {

}
