package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.mapper.PermissionMapper;
import com.yuntun.sanitationkitchen.service.IPermissionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {


}
