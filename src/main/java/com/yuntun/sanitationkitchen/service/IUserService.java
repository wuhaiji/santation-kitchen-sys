package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.entity.Permission;
import com.yuntun.sanitationkitchen.entity.Role;
import com.yuntun.sanitationkitchen.entity.User;
import com.yuntun.sanitationkitchen.model.dto.UserGetDto;

import java.util.List;

/**
 * <p>
 * 后台管理系统用户表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
public interface IUserService extends IService<User> {

    /**
     * 获取用户的权限列表
     * @param userId
     * @return
     */
    List<Permission> getUserPermissionList(Long userId);

    /**
     * 获取用户的角色列表
     * @param userId
     * @return
     */
    List<Role> getUserRoleList(Long userId);

    /**
     * 获取用户详细信息
     * @return
     */
    User getDetailById(UserGetDto dto);
}
