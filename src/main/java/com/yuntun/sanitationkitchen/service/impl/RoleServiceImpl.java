package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.model.entity.Role;
import com.yuntun.sanitationkitchen.mapper.RoleMapper;
import com.yuntun.sanitationkitchen.service.IRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
