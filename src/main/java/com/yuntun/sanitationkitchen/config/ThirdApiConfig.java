package com.yuntun.sanitationkitchen.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yookfeng
 * 2020/7/22
 * @des 三方设备API
 **/
@Component
@ConfigurationProperties(prefix = "lyy.api")
@Data
@Accessors(chain = true)
public class ThirdApiConfig {

    String authIp;

    String key;

    String videoUrl;


}
