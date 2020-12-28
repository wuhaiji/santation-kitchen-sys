package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.mapper.DriverMapper;
import com.yuntun.sanitationkitchen.model.entity.Driver;
import com.yuntun.sanitationkitchen.service.IDriverService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地磅配置表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-28
 */
@Service
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements IDriverService {

}
