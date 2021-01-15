package com.yuntun.sanitationkitchen;

import com.yuntun.sanitationkitchen.weight.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/24
 */

@SpringBootApplication
@EnableScheduling // 开启定时任务功能
@MapperScan("com.yuntun.sanitationkitchen.mapper")
@EnableTransactionManagement
@Slf4j
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        try {
            new NettyServer().bind(8088);
        } catch (Exception e) {
            log.error("netty启动失败", e);
        }
    }

}
