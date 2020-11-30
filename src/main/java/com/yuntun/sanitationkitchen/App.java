package com.yuntun.sanitationkitchen;

import org.mybatis.spring.annotation.MapperScan;
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
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
