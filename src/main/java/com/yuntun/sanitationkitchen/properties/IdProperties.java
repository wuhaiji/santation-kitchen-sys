package com.yuntun.sanitationkitchen.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/1
 */
@ConfigurationProperties(prefix = "id")
@Component
@Data
public class IdProperties {
    long workerId=1;
    long datacenterId=1;
}
