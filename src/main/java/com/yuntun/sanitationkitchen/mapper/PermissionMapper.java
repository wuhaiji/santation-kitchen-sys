package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

/**
* <p>
    * 权限表 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-11-30
*/
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

}
