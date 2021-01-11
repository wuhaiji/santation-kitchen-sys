package com.yuntun.sanitationkitchen.weight.propertise;

import com.yuntun.sanitationkitchen.config.YamlConfigFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 地磅设备 数据包格式
 *
 * @author wujihong
 */
@Component //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "weight")
@PropertySource(value = {"classpath:udc-data-package-format.yml"}, factory = YamlConfigFactory.class)
@Data
public class WeightDataPackageFormat {

    // 称重数据包大小
    private Integer size;

    // 开始标志
    private byte beginFlag;

    // 地磅数据
    private BoundData data;

    // 异或校验 位置
    private Integer verifyIndex;

    // 异或校验 大小
    private Integer verifySize;

    // 结束标志
    private byte endFlag;

    @Data
    public static class BoundData {

        // 地磅数据大小
        private Integer size;

        // 符号位 位置
        private Integer symbolIndex;

        // 符号位 大小
        private Integer symbolSize;

        // 整数部分 位置
        private Integer integerIndex;

        // 整数部分 大小
        private Integer integerSize;

        // 小数部分 位置
        private Integer decimalIndex;

        // 小数部分 大小
        private Integer decimalSize;

    }
}
