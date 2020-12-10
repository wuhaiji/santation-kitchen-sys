package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.RolePermissionMapper;
import com.yuntun.sanitationkitchen.model.entity.RolePermission;
import com.yuntun.sanitationkitchen.service.IRolePermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements IRolePermissionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<RolePermission> collect,Long roleId) {
        //先删除所有的存在的权限
        baseMapper.delete(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        Boolean aBoolean = baseMapper.insertBatch(collect);
        return aBoolean;
    }
}
