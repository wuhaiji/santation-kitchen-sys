package com.yuntun.sanitationkitchen.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/12/8
 */
@Data
@Accessors(chain = true)
public class RoleAllotPermissionDto {

    Long RoleId;

    List<Long> permissionIds;
}
