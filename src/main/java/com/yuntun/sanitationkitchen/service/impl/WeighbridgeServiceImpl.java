package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.SanitationOfficeMapper;
import com.yuntun.sanitationkitchen.mapper.TicketMachineMapper;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.*;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.mapper.WeighbridgeMapper;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;
import com.yuntun.sanitationkitchen.model.vo.TicketMachineVo;
import com.yuntun.sanitationkitchen.model.vo.TrashCanVo;
import com.yuntun.sanitationkitchen.model.vo.WeighbridgeVo;
import com.yuntun.sanitationkitchen.service.IWeighbridgeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 地磅表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class WeighbridgeServiceImpl extends ServiceImpl<WeighbridgeMapper, Weighbridge> implements IWeighbridgeService {

    @Autowired
    private WeighbridgeMapper weighbridgeMapper;

    @Autowired
    private SanitationOfficeMapper sanitationOfficeMapper;

    @Autowired
    private TicketMachineMapper ticketMachineMapper;

    @Override
    public RowData<WeighbridgeVo> findWeighbridgeList(WeighbridgeDto dto) {
        IPage<Weighbridge> iPage = weighbridgeMapper.selectPage(
                new Page<Weighbridge>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<Weighbridge>()
                        .like(EptUtil.isNotEmpty(dto.getDeviceCode()), "device_code", dto.getDeviceCode())
                        .like(EptUtil.isNotEmpty(dto.getDeviceName()), "device_name", dto.getDeviceName())
                        .like(EptUtil.isNotEmpty(dto.getBrand()), "brand", dto.getBrand())
                        .like(EptUtil.isNotEmpty(dto.getModel()), "model", dto.getModel())
                        .eq(EptUtil.isNotEmpty(dto.getSanitationOfficeId()), "sanitation_office_id", dto.getSanitationOfficeId())
                        .orderByDesc("create_time")
        );
        List<WeighbridgeVo> weighbridgeVoList = ListUtil.listMap(WeighbridgeVo.class, iPage.getRecords());

        RowData<WeighbridgeVo> weighbridgeVoRowData = new RowData<WeighbridgeVo>()
                .setRows(weighbridgeVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return weighbridgeVoRowData;
    }

    @Override
    public SelectOptionVo selectWeighbridgeOption() {
        SelectOptionVo selectOptionVo = new SelectOptionVo();

        // 所属单位
        List<SanitationOfficeValue> sanitationOfficeList = sanitationOfficeMapper.selectSanitationOfficeOption();
        selectOptionVo.setSanitationOfficeList(sanitationOfficeList);
        return selectOptionVo;
    }

    @Override
    public WeighbridgeVo findWeighbridgeByUid(Long uid) {
        WeighbridgeVo weighbridgeVo = new WeighbridgeVo();
        Weighbridge weighbridge = weighbridgeMapper.selectOne(new QueryWrapper<Weighbridge>().eq("uid", uid));
        if(weighbridge == null) {
            return null;
        }
        BeanUtils.copyProperties(weighbridge, weighbridgeVo);
        return weighbridgeVo;
    }

    @Override
    public Boolean insertWeighbridge(WeighbridgeDto dto) {
        Weighbridge weighbridge = new Weighbridge().setCreator(UserIdHolder.get());
        BeanUtils.copyProperties(dto, weighbridge);
        weighbridge.setUid(SnowflakeUtil.getUnionId());

        int save = weighbridgeMapper.insert(weighbridge);
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean updateWeighbridge(WeighbridgeDto dto) {
        Weighbridge weighbridge = new Weighbridge().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(dto, weighbridge);
        Integer save = weighbridgeMapper.update(weighbridge,
                new QueryWrapper<Weighbridge>().eq("uid", dto.getUid())
        );
        if (save > 0)
            return true;
        else
            return false;
    }

    @Transactional
    @Override
    public Boolean deleteWeighbridge(List<Long> uids) {
        uids.forEach(uid -> {
            // 1.判断地磅是否存在
            Weighbridge weighbridge = weighbridgeMapper.selectOne(new QueryWrapper<Weighbridge>().eq("uid", uid));
            if (weighbridge == null && weighbridge.getNetDeviceCode() == null) {
                log.error("删除地磅异常,必要数据存在空值");
                throw new ServiceException(VehicleCode.ID_NOT_EXIST);
            } else {
                // 2.判断地磅是否可以删除（有无绑定小票机设备）
                Integer count = ticketMachineMapper.selectCount(new QueryWrapper<TicketMachine>().eq("unique_code", weighbridge.getNetDeviceCode()));
                if (count != null && count > 0) {
                    log.error("不能删除，已绑定了小票机的地磅");
                    throw new ServiceException("不能删除，已绑定了小票机的地磅");
                }
            }
        });

        Weighbridge weighbridge = new Weighbridge().setDeletedBy(UserIdHolder.get());
        Integer result = weighbridgeMapper.update(weighbridge, new QueryWrapper<Weighbridge>().in("uid", uids));
        Integer result2 = weighbridgeMapper.delete(new QueryWrapper<Weighbridge>().in("uid", uids));
        if (result > 0 && result2 > 0)
            return true;
        else
            return false;
    }
}
