package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* <p>
    * 后台管理系统用户表 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-11-30
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
