package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.mapper.PoundBillMapper;
import com.yuntun.sanitationkitchen.mapper.SanitationOfficeMapper;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.PoundBillVo;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;
import com.yuntun.sanitationkitchen.model.vo.TicketMachineVo;
import com.yuntun.sanitationkitchen.service.IPoundBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 地磅配置表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-02
 */
@Service
public class PoundBillServiceImpl extends ServiceImpl<PoundBillMapper, PoundBill> implements IPoundBillService {

    @Autowired
    private SanitationOfficeMapper sanitationOfficeMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private TrashCanMapper trashCanMapper;

    @Autowired
    private PoundBillMapper poundBillMapper;

    @Override
    public SelectOptionVo selectPoundBillOption() {
        SelectOptionVo selectOptionVo = new SelectOptionVo();
        // 1.查询环卫机构
        List<SanitationOfficeValue> sanitationOfficeList = sanitationOfficeMapper.selectSanitationOfficeOption();
        selectOptionVo.setSanitationOfficeList(sanitationOfficeList);

        // 2.查询车辆
        List<VehicleValue> vehicleList = vehicleMapper.selectList(new QueryWrapper<Vehicle>().
                select("uid", "number_plate")).stream().map(vehicle -> {
            VehicleValue vehicleValue = new VehicleValue();
            vehicleValue.setVehicleId(vehicle.getUid());
            vehicleValue.setVehicleNumber(vehicle.getNumberPlate());
            return vehicleValue;
        }).collect(Collectors.toList());
        selectOptionVo.setVehicleList(vehicleList);

        // 3.查询垃圾桶
        List<TrashCanValue> trashCanList = trashCanMapper.selectList(new QueryWrapper<TrashCan>().
                select("uid", "facility_code")).stream().map(trashCan -> {
            TrashCanValue trashCanValue = new TrashCanValue();
            trashCanValue.setTrashCanId(trashCan.getUid());
            trashCanValue.setTrashCanCode(trashCan.getFacilityCode());
            return trashCanValue;
        }).collect(Collectors.toList());
        selectOptionVo.setTrashCanList(trashCanList);
        return selectOptionVo;
    }

    @Override
    public RowData<PoundBillVo> findPoundBillList(PoundBillDto poundBillDto) {
        IPage<PoundBill> iPage = poundBillMapper.selectPage(
                new Page<PoundBill>()
                        .setSize(poundBillDto.getPageSize())
                        .setCurrent(poundBillDto.getPageNo()),
                new QueryWrapper<PoundBill>()
                        .like(EptUtil.isNotEmpty(poundBillDto.getSerialCode()), "serial_code", poundBillDto.getSerialCode())
                        .like(EptUtil.isNotEmpty(poundBillDto.getNumberPlate()), "number_plate", poundBillDto.getNumberPlate())
                        // 净重
                        .like(EptUtil.isNotEmpty(poundBillDto.getNetWeight()), "net_weight", poundBillDto.getNetWeight())
                        // 毛重
                        .like(EptUtil.isNotEmpty(poundBillDto.getGrossWeight()), "gross_weight", poundBillDto.getGrossWeight())
                        // 皮重
                        .like(EptUtil.isNotEmpty(poundBillDto.getTare()), "tare", poundBillDto.getTare())
                        .eq(EptUtil.isNotEmpty(poundBillDto.getSanitationOfficeId()), "sanitation_office_id", poundBillDto.getSanitationOfficeId())
                        .orderByDesc("create_time")
        );
        List<PoundBillVo> poundBillVoList = ListUtil.listMap(PoundBillVo.class, iPage.getRecords());

        RowData<PoundBillVo> poundBillVoRowData = new RowData<PoundBillVo>()
                .setRows(poundBillVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return poundBillVoRowData;
    }
}
