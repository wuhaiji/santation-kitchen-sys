package com.yuntun.sanitationkitchen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuntun.sanitationkitchen.model.entity.Restaurant;
import org.apache.ibatis.annotations.Mapper;

/**
* <p>
    * 餐馆 Mapper 接口
    * </p>
*
* @author whj
* @since 2020-11-30
*/
@Mapper
public interface RestaurantMapper extends BaseMapper<Restaurant> {
}
