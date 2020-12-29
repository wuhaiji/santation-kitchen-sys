package com.yuntun.sanitationkitchen.weight.propertise;

import com.yuntun.sanitationkitchen.config.YamlConfigFactory;
import com.yuntun.sanitationkitchen.weight.resolve.ResolveProtocol;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * RFID读卡器设备设备 数据包格式
 *
 * @author wujihong
 */
@Component //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "rfid")
@PropertySource(value = {"classpath:udc-data-package-format.yml"}, factory = YamlConfigFactory.class)
@Data
public class RFIDDataPackageFormat {

    // rfid长度
    private Integer rfidSize;

    // epc开始位置
    private Integer epcIndex;

    // epc号长度
    private Integer epcSize;

    // CRC校验码大小
    private Integer CRCSize;
}
