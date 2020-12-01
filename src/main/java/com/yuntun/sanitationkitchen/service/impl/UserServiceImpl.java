package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.UserMapper;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.dto.UserGetDto;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.service.*;
import com.yuntun.sanitationkitchen.util.EptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台管理系统用户表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    IUserRoleService iUserRoleService;

    @Autowired
    IRoleService iRoleService;

    @Autowired
    IRolePermissionService iRolePermissionService;

    @Autowired
    IPermissionService iPermissionService;

    @Override
    public List<Permission> getUserPermissionList(Long userId) {
        //查询用户角色关联表
        List<UserRole> userRoleList;
        try {
            userRoleList = iUserRoleService.list(
                    new QueryWrapper<UserRole>().eq("userId", userId)
            );
        } catch (Exception e) {
            throw new ServiceException(UserCode.LIST_USER_PERMISSION_ERROR);
        }
        if (userRoleList.size() <= 0){
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoleList.parallelStream()
                .map(UserRole::getRoleId).collect(Collectors.toList());

        //查询角色权限关联表
        List<RolePermission> rolePermissionList;
        try {
            rolePermissionList=iRolePermissionService.list(
                    new QueryWrapper<RolePermission>().in("role_id", roleIds)
            );
        } catch (Exception e) {
            throw new ServiceException(UserCode.LIST_USER_PERMISSION_ERROR);
        }
        if (rolePermissionList.size() <= 0){
            return new ArrayList<>();
        }
        List<Long> permissionIds = rolePermissionList.parallelStream()
                .map(RolePermission::getPermissionId).collect(Collectors.toList());

        //查询权限列表
        List<Permission> permissionList;
        try {
            permissionList = iPermissionService.list(new QueryWrapper<Permission>().in("uid", permissionIds));
        } catch (Exception e) {
            throw new ServiceException(UserCode.LIST_USER_PERMISSION_ERROR);
        }

        return permissionList;
    }

    @Override
    public List<Role> getUserRoleList(Long userId) {
        //查询用户角色关联表
        List<UserRole> userRoleList;
        try {
            userRoleList = iUserRoleService.list(
                    new QueryWrapper<UserRole>().eq("userId", userId)
            );
        } catch (Exception e) {
            throw new ServiceException(UserCode.LIST_USER_ROLE_ERROR);
        }
        if (userRoleList.size() <= 0){
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoleList.parallelStream()
                .map(UserRole::getRoleId).collect(Collectors.toList());

        List<Role> roleList;
        try {
            roleList = iRoleService.list(new QueryWrapper<Role>().in("uid", roleIds));
        } catch (Exception e) {
            throw new ServiceException(UserCode.LIST_USER_ROLE_ERROR);
        }
        return roleList;
    }

    @Override
    public User getDetailById(UserGetDto dto) {
        User user;
        if (
                EptUtil.isEmpty(dto.getPhone())
                        && EptUtil.isEmpty(dto.getUsername())
                        && EptUtil.isEmpty(dto.getUid())
        ) {
            throw new ServiceException("PARAM_ERROR", "查询条件不能为空");
        }
        try {
            user = baseMapper.selectOne(
                    new QueryWrapper<User>()
                            .eq(EptUtil.isNotEmpty(dto.getUid()), "uid", dto.getUid())
                            .eq(EptUtil.isNotEmpty(dto.getUsername()), "username", dto.getUsername())
                            .eq(EptUtil.isNotEmpty(dto.getPhone()), "phone", dto.getPhone())
            );
        } catch (Exception e) {
            throw new ServiceException(UserCode.DETAIL_SYSUSER_FAILURE);
        }
        if (user == null) {
            throw new ServiceException(UserCode.USER_DOES_NOT_EXIST);
        }
        return user;
    }
}
