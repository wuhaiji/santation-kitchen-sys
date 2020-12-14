package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.*;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.SanitationOfficeDto;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.SanitationOfficeVo;
import com.yuntun.sanitationkitchen.service.ISanitationOfficeService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台管理系统用户表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class SanitationOfficeServiceImpl extends ServiceImpl<SanitationOfficeMapper, SanitationOffice> implements ISanitationOfficeService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeighbridgeMapper weighbridgeMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private TicketMachineMapper ticketMachineMapper;



    @Override
    public RowData<SanitationOfficeVo> findSanitationOfficeServiceList(SanitationOfficeDto sanitationOfficeDto) {
        IPage<SanitationOffice> iPage = baseMapper.selectPage(
                new Page<SanitationOffice>()
                        .setSize(sanitationOfficeDto.getPageSize())
                        .setCurrent(sanitationOfficeDto.getPageNo()),
                new QueryWrapper<SanitationOffice>()
                        .like(EptUtil.isNotEmpty(sanitationOfficeDto.getName()), "name", sanitationOfficeDto.getName())
                        .eq(EptUtil.isNotEmpty(sanitationOfficeDto.getManagerId()), "manager_id", sanitationOfficeDto.getManagerId())
                        .orderByDesc("create_time")
        );

        List<SanitationOffice> records = iPage.getRecords();


        List<Long> userIds = records.parallelStream().map(SanitationOffice::getManagerId).collect(Collectors.toList());

        List<User> users;
        if(EptUtil.isNotEmpty(userIds)){
            users = userMapper.selectList(new QueryWrapper<User>().in(EptUtil.isNotEmpty(userIds),"uid", userIds));
        }else{
            users=new ArrayList<>();
        }

        Map<Long, User> userMap = users.parallelStream().collect(Collectors.toMap(User::getUid, i -> i));
        List<SanitationOfficeVo> sanitationOfficeVoList = records.parallelStream()
                .map(i -> {
                    SanitationOfficeVo sanitationOfficeVo = new SanitationOfficeVo();
                    BeanUtils.copyProperties(i, sanitationOfficeVo);
                    User user = userMap.get(i.getManagerId());
                    if (user != null) {
                        sanitationOfficeVo.setManagerName(user.getUsername());
                        sanitationOfficeVo.setManagerPhone(user.getPhone());
                    }
                    return sanitationOfficeVo;
                })
                .collect(Collectors.toList());

        RowData<SanitationOfficeVo> sanitationOfficeVoRowData = new RowData<SanitationOfficeVo>()
                .setRows(sanitationOfficeVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return sanitationOfficeVoRowData;
    }

    @Override
    public SanitationOfficeVo findSanitationOfficeServiceByUid(Long uid) {
        SanitationOfficeVo sanitationOfficeVo = new SanitationOfficeVo();
        SanitationOffice sanitationOffice = baseMapper.selectOne(new QueryWrapper<SanitationOffice>().eq("uid", uid));
        if (sanitationOffice == null) {
            return null;
        }
        BeanUtils.copyProperties(sanitationOffice, sanitationOfficeVo);
        return sanitationOfficeVo;
    }

    @Override
    public Boolean insertSanitationOffice(SanitationOfficeDto sanitationOfficeDto) {
        SanitationOffice sanitationOffice = new SanitationOffice().setCreator(UserIdHolder.get());
        BeanUtils.copyProperties(sanitationOfficeDto, sanitationOffice);
        sanitationOffice.setUid(SnowflakeUtil.getUnionId());

        int save = baseMapper.insert(sanitationOffice);
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean updateSanitationOffice(SanitationOfficeDto sanitationOfficeDto) {
        SanitationOffice sanitationOffice = new SanitationOffice().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(sanitationOfficeDto, sanitationOffice);
        Integer save = baseMapper.update(sanitationOffice,
                new QueryWrapper<SanitationOffice>().eq("uid", sanitationOfficeDto.getUid())
        );
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override

    public Boolean deleteSanitationOffice(List<Long> uids) {
        if (uids!=null &&uids.size()!=0){
            for (Long officeId : uids) {
                //是否还有用户
                List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                        .eq(User::getSanitationOfficeId, officeId));
                if (EptUtil.isNotEmpty(users)){
                    log.error("还有用户是属于该单位，删除异常");
                    throw new ServiceException(UserCode.USER_NOT_NULL_ERROR);
                }
                //地磅 车辆 小票机  摄像头
                List<Weighbridge> weighbridges = weighbridgeMapper.selectList(new LambdaQueryWrapper<Weighbridge>()
                        .eq(Weighbridge::getSanitationOfficeId, officeId));
                if (EptUtil.isNotEmpty(weighbridges)){
                    log.error("还有地磅是属于该单位，删除异常");
                    throw new ServiceException(UserCode.WIGHT_BRIGHT_NOT_NULL_ERROR);
                }
                List<Vehicle> vehicles = vehicleMapper.selectList(new LambdaQueryWrapper<Vehicle>()
                        .eq(Vehicle::getSanitationOfficeId, officeId));
                if (EptUtil.isNotEmpty(vehicles)){
                    log.error("还有车辆是属于该单位，删除异常");
                    throw new ServiceException(UserCode. VEHICLE_NOT_NULL_ERROR);
                }
                List<TicketMachine> ticketMachines = ticketMachineMapper.selectList(new LambdaQueryWrapper<TicketMachine>()
                        .eq(TicketMachine::getSanitationOfficeId, officeId));
                if (EptUtil.isNotEmpty(ticketMachines)){
                    log.error("还有小票机是属于该单位，删除异常");
                    throw new ServiceException(UserCode. TICKET_MACHINE_NOT_NULL_ERROR);
                }
            }
        }


        int result2 = baseMapper.delete(new QueryWrapper<SanitationOffice>().in("uid", uids));
        if (result2 > 0)
            return true;
        else
            return false;
    }
}
