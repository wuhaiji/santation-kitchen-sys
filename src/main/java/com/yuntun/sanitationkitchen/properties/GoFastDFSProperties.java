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
 * @since 2020/11/10
 */
@ConfigurationProperties(prefix = "gofastdfs.file")
@Component
@Data
public class GoFastDFSProperties {
    public String path;
    public String group;
    /**
     * 文件访问host
     */
    public String host;
}
