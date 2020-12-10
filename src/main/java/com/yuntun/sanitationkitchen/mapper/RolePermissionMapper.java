package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.model.entity.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* <p>
    * 用户角色表 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-11-30
*/
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    Boolean insertBatch(List<RolePermission> list);
}
