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
public class RolePermissionSaveDto {
    /**
     * 用户id
     */
    private Long roleId;

    /**
     * 角色id
     */
    private Long permissionId;
}
