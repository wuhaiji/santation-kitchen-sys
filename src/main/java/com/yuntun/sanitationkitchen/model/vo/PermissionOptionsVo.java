package com.yuntun.sanitationkitchen.model.vo;

import com.yuntun.sanitationkitchen.util.AuthUtil;
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
public class PermissionOptionsVo {
    Long uid;
    Long parentId;
    String permissionName;
    String permissionTag;
}
