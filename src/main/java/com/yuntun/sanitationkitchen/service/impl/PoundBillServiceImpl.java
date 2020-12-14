package com.yuntun.sanitationkitchen.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.PoundBillMapper;
import com.yuntun.sanitationkitchen.mapper.SanitationOfficeMapper;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.model.code.code40000.PoundBillCode;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.PoundBillVo;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;
import com.yuntun.sanitationkitchen.model.vo.TicketMachineVo;
import com.yuntun.sanitationkitchen.properties.PoundBillProperties;
import com.yuntun.sanitationkitchen.service.IPoundBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ExcelUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.excel.ExportExcelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Autowired
    private PoundBillProperties poundBillProperties;

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
                        .eq(EptUtil.isNotEmpty(poundBillDto.getVehicleId()), "vehicle_id", poundBillDto.getVehicleId())
//                        // 净重
//                        .like(EptUtil.isNotEmpty(poundBillDto.getNetWeight()), "net_weight", poundBillDto.getNetWeight())
//                        // 毛重
//                        .like(EptUtil.isNotEmpty(poundBillDto.getGrossWeight()), "gross_weight", poundBillDto.getGrossWeight())
//                        // 皮重
//                        .like(EptUtil.isNotEmpty(poundBillDto.getTare()), "tare", poundBillDto.getTare())
                        .eq(EptUtil.isNotEmpty(poundBillDto.getSanitationOfficeId()), "sanitation_office_id", poundBillDto.getSanitationOfficeId())
                        .gt(EptUtil.isNotEmpty(poundBillDto.getBeginTime()), "create_time", poundBillDto.getBeginTime())
                        .le(EptUtil.isNotEmpty(poundBillDto.getEndTime()), "create_time", poundBillDto.getEndTime())
                        .orderByDesc("create_time")
        );
        List<PoundBillVo> poundBillVoList = ListUtil.listMap(PoundBillVo.class, iPage.getRecords());

        RowData<PoundBillVo> poundBillVoRowData = new RowData<PoundBillVo>()
                .setRows(poundBillVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return poundBillVoRowData;
    }

    @Override
    public void exportPoundBill(PoundBillDto poundBillDto, HttpServletResponse response) {
        String[] columns = null;
        List<PoundBill> poundBillList = poundBillMapper.selectList(new QueryWrapper<PoundBill>()
                .select(columns)
                .like(EptUtil.isNotEmpty(poundBillDto.getSerialCode()), "serial_code", poundBillDto.getSerialCode())
                .eq(EptUtil.isNotEmpty(poundBillDto.getVehicleId()), "vehicle_id", poundBillDto.getVehicleId())
                .eq(EptUtil.isNotEmpty(poundBillDto.getSanitationOfficeId()), "sanitation_office_id", poundBillDto.getSanitationOfficeId())
                .gt(EptUtil.isNotEmpty(poundBillDto.getBeginTime()), "create_time", poundBillDto.getBeginTime())
                .le(EptUtil.isNotEmpty(poundBillDto.getEndTime()), "create_time", poundBillDto.getEndTime())
                .orderByDesc("create_time"));

        List<PoundBillVo> poundBillVoList = ListUtil.listMap(PoundBillVo.class, poundBillList);
        poundBillVoList = poundBillVoList.stream().map(poundBillVo -> {
            SanitationOffice sanitationOffice = sanitationOfficeMapper.selectOne(new QueryWrapper<SanitationOffice>().select("name").eq("uid", poundBillVo.getSanitationOfficeId()));
            poundBillVo.setSanitationOfficeName(sanitationOffice.getName());
            return poundBillVo;
        }).collect(Collectors.toList());

//        String[] headers = {"ID", "唯一ID", "流水号", "环卫ID", "车牌号", "车辆ID", "垃圾箱ID", "垃圾箱编号", "毛重", "皮重", "净重", "创建人", "创建时间", "禁用状态", "禁用人", "禁用时间",
//        "修改人", "修改时间", "删除状态", "删除人" , "删除时间"};
//        ExportExcelWrapper.exportExcel(poundBillDto.getFileName(), poundBillDto.getTitle(), headers, poundBillVoList, response, poundBillDto.getVersion());

        try {
            ExcelUtil.excelExport(response, poundBillProperties.getFileName(), poundBillProperties.getSheetName(), poundBillVoList, poundBillProperties.getHeaders(), poundBillProperties.getColumns());
        } catch (Exception e) {
            log.error("export PoundBill err,{}",e);
            throw new ServiceException(PoundBillCode.EXPORT_EXCEL);
        }
    }
}
