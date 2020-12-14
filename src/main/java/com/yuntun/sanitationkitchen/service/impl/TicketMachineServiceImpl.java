package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.SanitationOfficeMapper;
import com.yuntun.sanitationkitchen.mapper.TicketMachineMapper;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.TicketMachineDto;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;
import com.yuntun.sanitationkitchen.model.vo.TicketMachineVo;
import com.yuntun.sanitationkitchen.service.ITicketMachineService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 小票机 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class TicketMachineServiceImpl extends ServiceImpl<TicketMachineMapper, TicketMachine> implements ITicketMachineService {

    @Autowired
    private TicketMachineMapper ticketMachineMapper;

    @Autowired
    private SanitationOfficeMapper sanitationOfficeMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public SelectOptionVo selectTicketMachineOption() {
        SelectOptionVo selectOptionVo = new SelectOptionVo();
        // 1.查询设备品牌
//        List<String> brandList = ticketMachineMapper.selectList(new QueryWrapper<TicketMachine>().select("brand")).stream()
//                .map(TicketMachine::getBrand).distinct().collect(Collectors.toList());
//        selectOptionVo.setBrandList(brandList);

        // 2.查询设备型号
//        List<String> modelList = ticketMachineMapper.selectList(new QueryWrapper<TicketMachine>().select("model")).stream().
//                map(TicketMachine::getModel).distinct().collect(Collectors.toList());
//        selectOptionVo.setModelList(modelList);

        // 3.车辆
        List<VehicleValue> vehicleList = vehicleMapper.selectList(new QueryWrapper<Vehicle>().
                select("uid", "number_plate")).stream().map(vehicle -> {
            VehicleValue vehicleValue = new VehicleValue();
            vehicleValue.setVehicleId(vehicle.getUid());
            vehicleValue.setVehicleNumber(vehicle.getNumberPlate());
            return vehicleValue;
        }).collect(Collectors.toList());
        selectOptionVo.setVehicleList(vehicleList);

        // 4.查询环卫机构
        List<SanitationOfficeValue> sanitationOfficeList = sanitationOfficeMapper.selectList(new QueryWrapper<SanitationOffice>().
                select("uid", "name")).stream().map(sanitationOffice -> {
                    SanitationOfficeValue sanitationOfficeValue = new SanitationOfficeValue();
                    sanitationOfficeValue.setSanitationOfficeId(sanitationOffice.getUid());
                    sanitationOfficeValue.setSanitationOfficeName(sanitationOffice.getName());
                    return sanitationOfficeValue;
                }).collect(Collectors.toList());
//        List<SanitationOfficeValue> sanitationOfficeList = sanitationOfficeMapper.selectSanitationOfficeOption();
        selectOptionVo.setSanitationOfficeList(sanitationOfficeList);
        return selectOptionVo;
    }

    @Override
    public RowData<TicketMachineVo> findTicketMachineList(TicketMachineDto ticketMachineDto) {
        IPage<TicketMachine> iPage = ticketMachineMapper.selectPage(
                new Page<TicketMachine>()
                        .setSize(ticketMachineDto.getPageSize())
                        .setCurrent(ticketMachineDto.getPageNo()),
                new QueryWrapper<TicketMachine>()
                        .like(EptUtil.isNotEmpty(ticketMachineDto.getDeviceCode()), "device_code", ticketMachineDto.getDeviceCode())
                        .like(EptUtil.isNotEmpty(ticketMachineDto.getDeviceName()), "device_name", ticketMachineDto.getDeviceName())
                        .like(EptUtil.isNotEmpty(ticketMachineDto.getBrand()), "brand", ticketMachineDto.getBrand())
                        .like(EptUtil.isNotEmpty(ticketMachineDto.getModel()), "model", ticketMachineDto.getModel())
                        .eq(EptUtil.isNotEmpty(ticketMachineDto.getVehicleId()), "vehicle_id", ticketMachineDto.getVehicleId())
                        .eq(EptUtil.isNotEmpty(ticketMachineDto.getSanitationOfficeId()), "sanitation_office_id", ticketMachineDto.getSanitationOfficeId())
                        .orderByDesc("create_time")
        );
        List<TicketMachineVo> ticketMachineVoList = ListUtil.listMap(TicketMachineVo.class, iPage.getRecords());

        RowData<TicketMachineVo> ticketMachineVoRowData = new RowData<TicketMachineVo>()
                .setRows(ticketMachineVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return ticketMachineVoRowData;
    }

    @Override
    public TicketMachineVo findTicketMachineByUid(Long uid) {
        TicketMachineVo ticketMachineVo = new TicketMachineVo();
        TicketMachine ticketMachine = ticketMachineMapper.selectOne(new QueryWrapper<TicketMachine>().eq("uid", uid));
        if(ticketMachine == null) {
            return null;
        }
        BeanUtils.copyProperties(ticketMachine, ticketMachineVo);
        return ticketMachineVo;
    }

    @Override
    public Boolean insertTicketMachine(TicketMachineDto ticketMachineDto) {
        TicketMachine ticketMachine = new TicketMachine().setCreator(UserIdHolder.get());
        BeanUtils.copyProperties(ticketMachineDto, ticketMachine);
        ticketMachine.setUid(SnowflakeUtil.getUnionId());

//        String sanitationOfficeName = sanitationOfficeMapper.selectOne(
//                new QueryWrapper<SanitationOffice>().select("name").eq(EptUtil.isNotEmpty(ticketMachineDto.getSanitationOfficeId()),
//                        "uid", ticketMachineDto.getSanitationOfficeId())).getName();
//        ticketMachine.setSanitationOfficeName(sanitationOfficeName);

//        String numberPlate = vehicleMapper.selectOne(new QueryWrapper<Vehicle>().select("number_plate").eq(EptUtil.isNotEmpty(ticketMachineDto.getVehicleId()),
//                "uid", ticketMachineDto.getVehicleId())).getNumberPlate();
//        ticketMachine.setVehicleNumber(numberPlate);
        int save = ticketMachineMapper.insert(ticketMachine);
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean updateTicketMachine(TicketMachineDto ticketMachineDto) {
        TicketMachine ticketMachine = new TicketMachine().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(ticketMachineDto, ticketMachine);
        Integer save = ticketMachineMapper.update(ticketMachine,
                new QueryWrapper<TicketMachine>().eq("uid", ticketMachineDto.getUid())
        );
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean deleteTicketMachine(List<Long> uids) {
        uids.forEach(uid -> {
            TicketMachine ticketMachine = ticketMachineMapper.selectOne(new QueryWrapper<TicketMachine>().eq("uid", uid));
            if (ticketMachine == null) {
                log.error("删除小票机异常->uid不存在");
                throw new ServiceException(VehicleCode.ID_NOT_EXIST);
            }
        });

        TicketMachine ticketMachine = new TicketMachine().setDeletedBy(UserIdHolder.get());
        Integer result = ticketMachineMapper.update(ticketMachine, new QueryWrapper<TicketMachine>().in("uid", uids));
        Integer result2 = ticketMachineMapper.delete(new QueryWrapper<TicketMachine>().in("uid", uids));
        if (result > 0 && result2 > 0)
            return true;
        else
            return false;
    }
}
