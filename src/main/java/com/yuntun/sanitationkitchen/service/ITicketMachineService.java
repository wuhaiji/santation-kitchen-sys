package com.yuntun.sanitationkitchen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.dto.TicketMachineDto;
import com.yuntun.sanitationkitchen.model.entity.TicketMachine;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.TicketMachineVo;

/**
 * <p>
 * 小票机 服务类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
public interface ITicketMachineService extends IService<TicketMachine> {

    RowData<TicketMachineVo> findTicketMachineList(TicketMachineDto ticketMachineDto);

    TicketMachineVo findTicketMachineByUid(Long uid);

    Boolean insertTicketMachine(TicketMachineDto ticketMachineDto);

    Boolean updateTicketMachine(TicketMachineDto ticketMachineDto);

    Boolean deleteTicketMachine(Long uid);
}
