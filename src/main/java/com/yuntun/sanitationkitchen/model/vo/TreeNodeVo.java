package com.yuntun.sanitationkitchen.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/7
 */
@Data
@Accessors(chain = true)
public class TreeNodeVo {
    String label;
    Long value;
    Long parentId;
    List<TreeNodeVo> children;
}
