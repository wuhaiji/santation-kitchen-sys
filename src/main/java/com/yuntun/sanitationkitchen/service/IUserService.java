package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.dto.UserListDto;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.model.entity.Role;
import com.yuntun.sanitationkitchen.model.entity.User;
import com.yuntun.sanitationkitchen.model.dto.UserGetDto;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.UserListVo;

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

    /**
     * 分页查询用户
     * @param dto
     * @return
     */
    IPage<User> listPage(UserListDto dto);
}
