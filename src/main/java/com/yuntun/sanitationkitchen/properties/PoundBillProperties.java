package com.yuntun.sanitationkitchen.properties;

import com.yuntun.sanitationkitchen.config.YamlConfigFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wujihong
 */
@Component //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties(prefix = "pound-bill")
@PropertySource(value = {"classpath:pound-bill.yml"}, factory = YamlConfigFactory.class)
@Data
public class PoundBillProperties {

    // 文件名
    private String fileName;

    // 表格标题名(一个excel工作簿workbook可以有多个表格sheet)，Excel文档就是工作簿
    private String sheetName;

    // 表格:头部标题集合(即头部标题名)
    private List<String> headers;

    // 导出字段的列名集合
    private List<String> columns;

}
