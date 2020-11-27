package com.yuntun.sanitationkitchen.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.entity.TbSkUser;
import com.yuntun.sanitationkitchen.mapper.TbSkUserMapper;
import com.yuntun.sanitationkitchen.service.ITbSkUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 后台管理系统用户表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-11-26
 */
@Service
public class TbSkUserServiceImpl extends ServiceImpl<TbSkUserMapper, TbSkUser> implements ITbSkUserService {

}
