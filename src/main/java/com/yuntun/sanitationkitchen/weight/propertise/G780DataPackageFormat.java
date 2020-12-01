package com.yuntun.sanitationkitchen.weight.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 有人网G780设备 数据包格式
 *
 * @author wujihong
 */
@Component //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "g780-data-package")
@Data
public class G780DataPackageFormat {

    // 标识位
    private Integer flagIndex;

    private Integer flagSize;

    // 数据头
    private G780DataHeader g780DataHeader;

}
