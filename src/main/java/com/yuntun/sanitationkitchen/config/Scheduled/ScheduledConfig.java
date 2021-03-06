package com.yuntun.sanitationkitchen.config.Scheduled;

import cn.hutool.core.lang.Assert;
import com.yuntun.sanitationkitchen.model.entity.Cron;
import com.yuntun.sanitationkitchen.service.ICronService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>
 *  定时任务配置
 * </p>
 *
 * @author whj
 * @since 2020/12/15
 */
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {


    @Autowired
    private ApplicationContext context;

    @Autowired
    private ICronService iCronService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        //项目启动查询所有的定时任务，并且启动
        for (Cron cron : iCronService.list()) {
            Class<?> clazz;
            Object task;
            try {
                clazz = Class.forName(cron.getCronKey());
                task = context.getBean(clazz);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("未找到对应任务类：{}：" + cron.getCronKey(), e);
            } catch (BeansException e) {
                throw new IllegalArgumentException(cron.getCronKey() + "未纳入到spring管理", e);
            }
            Assert.isAssignable(ScheduledTask.class, task.getClass(), "定时任务类必须实现ScheduledOfTask接口");
            // 可以通过改变数据库数据进而实现动态改变执行周期
            taskRegistrar.addTriggerTask(
                    ((Runnable) task),
                    //下面这个函数会在当前触发时，查询数据库下一次触发的时间，由此可以动态改变定时任务频率
                    triggerContext ->
                            new CronTrigger(iCronService.getById(cron.getId()).getCronExpression()).nextExecutionTime(triggerContext)
            );
        }
    }

    @Bean
    public Executor taskExecutor() {
        //可根据cpu核心数配置线程数
        return Executors.newScheduledThreadPool(2);
    }

}
