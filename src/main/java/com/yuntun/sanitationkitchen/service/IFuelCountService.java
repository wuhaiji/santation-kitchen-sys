package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.dto.FuelDeviceDto;
import com.yuntun.sanitationkitchen.model.entity.FuelDevice;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.FuelDeviceVo;

/**
 * <p>
 * 油耗监测设备表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
public interface IFuelCountService extends IService<FuelDevice> {


    RowData<FuelDeviceVo> findFuelDeviceServiceList(FuelDeviceDto fuelDeviceDto);

}
