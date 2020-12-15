package com.yuntun.sanitationkitchen.weight.propertise;

import com.yuntun.sanitationkitchen.config.YamlConfigFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 有人网G780设备 数据包格式
 *
 * @author wujihong
 */
@Component //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "udc-data-package-format")
@PropertySource(value = {"classpath:udc-data-package-format.yml"}, factory = YamlConfigFactory.class)
@Data
public class UDCDataPackageFormat {

    // 标识位
    private G780DataFlag flag;

    // 数据头
    private G780DataHeader dataHeader;

    @Data
    public static class G780DataFlag {

        private Integer index;

        private Integer size;
    }

    @Data
    public static class G780DataHeader {

        // 数据包类型
        private Integer typeIndex;

        private Integer typeSize;

        // 数据包长度（不包括数据体）
        private Integer lengthIndex;

        private Integer lengthSize;

        // 设备号
        private Integer deviceNumberIndex;

        private Integer deviceNumberSize;

        // ip
        private Integer ipIndex;

        private Integer ipSize;

        // 端口
        private Integer portIndex;

        private Integer portSize;

    }

}
