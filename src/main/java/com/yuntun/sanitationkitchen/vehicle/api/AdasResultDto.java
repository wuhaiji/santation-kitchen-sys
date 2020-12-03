package com.yuntun.sanitationkitchen.vehicle.api;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 来源云平台ADAS api 返回结果对象
 * </p>
 *
 * @author whj
 * @since 2020/12/3
 */
@Data
@Accessors(chain = true)
public class AdasResultDto<T> {

    public static final int FLAG_ERROR = 0;

    public static final int FLAG_SUCCESS = 1;
    /**
     * 返回结果状态,0：错误，1：成功
     */
    Integer flag;
    /**
     * 返回状态描述,flag为0时，msg返回错误原因
     */
    String msg;

    /**
     * 结果列表
     */
    T obj;

    /**
     * 额外信息
     */
    String extend;
}
