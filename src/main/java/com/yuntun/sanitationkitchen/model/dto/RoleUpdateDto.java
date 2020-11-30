package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/30
 */
@Data
@Accessors(chain = true)
public class RoleUpdateDto {
    /**
     * uuid
     */
    private Long roleId;

    /**
     * 是否是超级管理员1.不是，0.是
     */
    private Boolean roleType;

    /**
     * 角色
     */
    private String roleName;
}
