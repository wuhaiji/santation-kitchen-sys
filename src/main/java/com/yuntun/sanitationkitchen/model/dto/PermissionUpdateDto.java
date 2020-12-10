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
public class PermissionUpdateDto {


    private Long uid;
    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限tag
     */
    private String permissionTag;

    /**
     * 权限父级id  0:顶级菜单
     */
    private Long parentId;

    private Integer permissionType;


}
