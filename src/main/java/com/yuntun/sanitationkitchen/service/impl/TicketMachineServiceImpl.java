package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.TicketMachineMapper;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.TicketMachineDto;
import com.yuntun.sanitationkitchen.model.entity.TicketMachine;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.TicketMachineVo;
import com.yuntun.sanitationkitchen.service.ITicketMachineService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public RowData<TicketMachineVo> findTicketMachineList(TicketMachineDto ticketMachineDto) {
        IPage<TicketMachine> iPage = ticketMachineMapper.selectPage(
                new Page<TicketMachine>()
                        .setSize(ticketMachineDto.getPageSize())
                        .setCurrent(ticketMachineDto.getPageNo()),
                new QueryWrapper<TicketMachine>()
                        .eq(EptUtil.isNotEmpty(ticketMachineDto.getSanitationOfficeId()), "sanitation_office_id", ticketMachineDto.getSanitationOfficeId())
                        .eq(EptUtil.isNotEmpty(ticketMachineDto.getStatus()), "status", ticketMachineDto.getStatus())
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
        TicketMachine ticketMachine = new TicketMachine();
        BeanUtils.copyProperties(ticketMachineDto, ticketMachine);
        ticketMachine.setUid(SnowflakeUtil.getUnionId());

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
    public Boolean deleteTicketMachine(Long uid) {
        TicketMachine ticketMachine = ticketMachineMapper.selectOne(new QueryWrapper<TicketMachine>().eq("uid", uid));
        if (ticketMachine == null) {
            log.error("删除小票机异常->uid不存在");
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        Integer result = ticketMachineMapper.delete(new QueryWrapper<TicketMachine>().eq("uid", uid));
        if (result > 0)
            return true;
        else
            return false;
    }
}
