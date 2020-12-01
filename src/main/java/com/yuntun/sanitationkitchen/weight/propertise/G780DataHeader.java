package com.yuntun.sanitationkitchen.weight.propertise;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wujihong
 */
@Component //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "data-header")
@Data
public class G780DataHeader {

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
