package com.yuntun.sanitationkitchen.config.Scheduled;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.model.entity.Cron;
import com.yuntun.sanitationkitchen.service.ICronService;
import com.yuntun.sanitationkitchen.util.SpringUtils;

public interface ScheduledTask extends Runnable {

    int DISABLED = 1;
    int NOT_DISABLED = 0;

    /**
     * 定时任务方法
     */
    void execute();

    /**
     * 实现控制定时任务启用或禁用的功能
     */
    @Override
    default void run() {
        ICronService iCronService = SpringUtils.getBean(ICronService.class);
        Cron scheduledCron = iCronService.getOne(new QueryWrapper<Cron>().eq("cron_key", this.getClass().getName()));
        if (DISABLED == scheduledCron.getStatus()) {
            return;
        }
        execute();
    }
}
