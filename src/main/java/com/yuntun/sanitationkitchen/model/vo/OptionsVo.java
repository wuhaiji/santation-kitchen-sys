package com.yuntun.sanitationkitchen.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *  下拉框vo
 * </p>
 *
 * @author whj
 * @since 2020/12/4
 */
@Data
@Accessors(chain = true)
public class OptionsVo {
    String label;
    Long value;
}
