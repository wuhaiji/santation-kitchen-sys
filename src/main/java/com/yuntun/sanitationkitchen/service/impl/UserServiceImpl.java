package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.UserMapper;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.dto.UserGetDto;
import com.yuntun.sanitationkitchen.model.dto.UserListDto;
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
    IUserService iUserService;

    @Autowired
    IRolePermissionService iRolePermissionService;

    @Autowired
    IPermissionService iPermissionService;

    @Override
    public List<Permission> getUserPermissionList(Long userId) {
        //查询用户表
        User user = iUserService.getOne(new QueryWrapper<User>().eq("uid", userId));
        //查询角色类型
        if (user == null){
            throw new ServiceException(UserCode.USER_DOES_NOT_EXIST);
        }
        Role role = iRoleService.getOne(new LambdaQueryWrapper<Role>().eq(EptUtil.isNotEmpty(user), Role::getUid, user.getRoleId()));
        if (role.getRoleType().equals(Role.SUPER_ADMIN)) {
            return new ArrayList<Permission>() {{
                add(new Permission().setPermissionTag("allPermission"));
            }};
        }
        //查询角色权限关联表
        List<RolePermission> rolePermissionList;
        try {
            rolePermissionList = iRolePermissionService.list(
                    new QueryWrapper<RolePermission>().eq("role_id", user.getRoleId())
            );
        } catch (Exception e) {
            throw new ServiceException(UserCode.LIST_USER_PERMISSION_ERROR);
        }
        if (rolePermissionList.size() <= 0) {
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

        List<UserRole> userRoleList = iUserRoleService.list(new QueryWrapper<UserRole>().eq("user_id", userId));
        if (userRoleList.size() <= 0) {
            return new ArrayList<>();
        }
        List<Long> roleIds = userRoleList.parallelStream()
                .map(UserRole::getRoleId).collect(Collectors.toList());

        return iRoleService.list(new QueryWrapper<Role>().in("uid", roleIds));
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

    @Override
    public IPage<User> listPage(UserListDto dto) {

        IPage<User> iPage = this.page(
                new Page<User>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<User>()
                        .likeRight(EptUtil.isNotEmpty(dto.getUsername()), "username", dto.getUsername())
                        .eq(EptUtil.isNotEmpty(dto.getPhone()), "phone", dto.getPhone())
                        .eq(EptUtil.isNotEmpty(dto.getRoleId()), "role_id", dto.getRoleId())
                        .eq(EptUtil.isNotEmpty(dto.getSanitationOfficeId()), "sanitation_office_id", dto.getSanitationOfficeId())
                        .eq(EptUtil.isNotEmpty(dto.getDisabled()), "disabled", dto.getDisabled())
                        .orderByDesc("create_time")
        );


        return iPage;

    }


}
