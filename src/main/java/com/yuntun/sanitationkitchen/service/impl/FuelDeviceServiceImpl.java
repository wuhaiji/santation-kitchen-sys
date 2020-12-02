package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code20000.RoleCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleTypeCode;
import com.yuntun.sanitationkitchen.model.dto.FuelDeviceDto;
import com.yuntun.sanitationkitchen.model.entity.FuelDevice;
import com.yuntun.sanitationkitchen.mapper.FuelDeviceMapper;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.FuelDeviceVo;
import com.yuntun.sanitationkitchen.service.IFuelDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 油耗监测设备表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class FuelDeviceServiceImpl extends ServiceImpl<FuelDeviceMapper, FuelDevice> implements IFuelDeviceService {

    @Autowired
    private FuelDeviceMapper fuelDeviceMapper;

    @Override
    public RowData<FuelDeviceVo> findFuelDeviceServiceList(FuelDeviceDto fuelDeviceDto) {
        IPage<FuelDevice> iPage = fuelDeviceMapper.selectPage(
                new Page<FuelDevice>()
                        .setSize(fuelDeviceDto.getPageSize())
                        .setCurrent(fuelDeviceDto.getPageNo()),
                new QueryWrapper<FuelDevice>()
                        .eq(EptUtil.isNotEmpty(fuelDeviceDto.getDeviceName()), "device_name", fuelDeviceDto.getDeviceName())
                        .eq(EptUtil.isNotEmpty(fuelDeviceDto.getBrand()), "brand", fuelDeviceDto.getBrand())
                        .orderByDesc("create_time")
        );
        List<FuelDeviceVo> fuelDeviceVoList = ListUtil.listMap(FuelDeviceVo.class, iPage.getRecords());

        RowData<FuelDeviceVo> fuelDeviceVoRowData = new RowData<FuelDeviceVo>()
                .setRows(fuelDeviceVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return fuelDeviceVoRowData;
    }

    @Override
    public FuelDeviceVo findFuelDeviceServiceByUid(Long uid) {
        FuelDeviceVo fuelDeviceVo = new FuelDeviceVo();
        FuelDevice fuelDevice = fuelDeviceMapper.selectOne(new QueryWrapper<FuelDevice>().eq("uid", uid));
        if(fuelDevice == null) {
            return null;
        }
        BeanUtils.copyProperties(fuelDevice, fuelDeviceVo);
        return fuelDeviceVo;
    }

    @Override
    public Boolean insertFuelDevice(FuelDeviceDto fuelDeviceDto) {
        FuelDevice fuelDevice = new FuelDevice();
        BeanUtils.copyProperties(fuelDeviceDto, fuelDevice);
        fuelDevice.setUid(SnowflakeUtil.getUnionId());

        int save = fuelDeviceMapper.insert(fuelDevice);
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean updateFuelDevice(FuelDeviceDto fuelDeviceDto) {
        FuelDevice fuelDevice = new FuelDevice().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(fuelDeviceDto, fuelDevice);
        Integer save = fuelDeviceMapper.update(fuelDevice,
                new QueryWrapper<FuelDevice>().eq("uid", fuelDeviceDto.getUid())
        );
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean deleteFuelDevice(Long uid) {
        FuelDevice fuelDevice = fuelDeviceMapper.selectOne(new QueryWrapper<FuelDevice>().eq("uid", uid));
        if (fuelDevice == null) {
            log.error("删除油耗异常->uid不存在");
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        Integer result = fuelDeviceMapper.delete(new QueryWrapper<FuelDevice>().eq("uid", uid));
        if (result > 0)
            return true;
        else
            return false;
    }


}
