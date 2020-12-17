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
 *
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

        for (Cron cron : iCronService.list()) {
            Class<?> clazz;
            Object task;
            try {
                clazz = Class.forName(cron.getCronKey());
                task = context.getBean(clazz);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("spring_scheduled_cron表数据" + cron.getCronKey() + "有误", e);
            } catch (BeansException e) {
                throw new IllegalArgumentException(cron.getCronKey() + "未纳入到spring管理", e);
            }
            Assert.isAssignable(ScheduledTask.class, task.getClass(), "定时任务类必须实现ScheduledOfTask接口");
            // 可以通过改变数据库数据进而实现动态改变执行周期
            taskRegistrar.addTriggerTask(
                    ((Runnable) task),
                    triggerContext ->
                            new CronTrigger(iCronService.getById(cron.getId()).getCronExpression()).nextExecutionTime(triggerContext)
            );
        }
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(8);
    }

}
