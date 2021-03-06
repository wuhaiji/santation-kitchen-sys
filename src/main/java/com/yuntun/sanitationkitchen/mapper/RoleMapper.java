package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.model.entity.Role;
import com.yuntun.sanitationkitchen.model.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* <p>
    * 角色表 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-11-30
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {


}
