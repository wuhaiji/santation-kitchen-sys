package com.yuntun.sanitationkitchen.properties;

import com.yuntun.sanitationkitchen.config.YamlConfigFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties(prefix = "trash-can-serial")
@PropertySource(value = {"classpath:excel-trash-can-serial.yml"}, factory = YamlConfigFactory.class)
@Data
public class TrashCanSerialProperties {

    private String fileName;

    private String sheetName;

    private List<String> headers;

    private List<String> columns;
}
