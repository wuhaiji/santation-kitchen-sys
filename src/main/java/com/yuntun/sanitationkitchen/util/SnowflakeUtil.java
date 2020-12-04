package com.yuntun.sanitationkitchen.util;

import cn.hutool.core.util.IdUtil;
import com.yuntun.sanitationkitchen.properties.IdProperties;

/**
 * <p>
 * 雪花算法工具类
 * </p>
 *
 * @author whj
 * @since 2020/12/1
 */
public class SnowflakeUtil {

    private static final IdProperties idProperties = SpringUtils.getBean("idProperties");

    public static Long getUnionId() {
        return IdUtil.getSnowflake(idProperties.getWorkerId(), idProperties.getDatacenterId()).nextId();
    }


}
