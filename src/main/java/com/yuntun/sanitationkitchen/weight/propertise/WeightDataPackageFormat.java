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
}
