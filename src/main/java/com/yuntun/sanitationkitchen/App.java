package com.yuntun.sanitationkitchen;

import com.yuntun.sanitationkitchen.weight.NettyServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/24
 */

@SpringBootApplication
@MapperScan("com.yuntun.sanitationkitchen.mapper")
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        new NettyServer().bind(8088);
    }
}
