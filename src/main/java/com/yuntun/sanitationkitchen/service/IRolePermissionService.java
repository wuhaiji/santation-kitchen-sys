package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.entity.RolePermission;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
public interface IRolePermissionService extends IService<RolePermission> {

    /**
     * 批量保存角色权限
     * @param collect
     * @param roleId
     * @return
     */
    boolean allotPermission(List<RolePermission> collect, Long roleId);


}
