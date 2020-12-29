package com.yuntun.sanitationkitchen.weight.propertise;

import com.yuntun.sanitationkitchen.config.YamlConfigFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 垃圾桶的称重设备 数据包格式
 *
 * @author wujihong
 */
@Component //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "trash")
@PropertySource(value = {"classpath:udc-data-package-format.yml"}, factory = YamlConfigFactory.class)
@Data
public class TrashDataPackageFormat {
}
