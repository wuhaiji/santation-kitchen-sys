package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.RestaurantMapper;
import com.yuntun.sanitationkitchen.model.entity.Restaurant;
import com.yuntun.sanitationkitchen.service.IRestaurantService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 餐馆表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-10
 */
@Service
public class RestaurantServiceImpl extends ServiceImpl<RestaurantMapper, Restaurant> implements IRestaurantService {

}
