package com.yuntun.sanitationkitchen.service.impl;

import static com.yuntun.sanitationkitchen.util.FormatDateUtils.getDay2;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.PoundBillMapper;
import com.yuntun.sanitationkitchen.mapper.SanitationOfficeMapper;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.mapper.VehicleMapper;
import com.yuntun.sanitationkitchen.model.code.code40000.PoundBillCode;
import com.yuntun.sanitationkitchen.model.dto.PoundBillDto;
import com.yuntun.sanitationkitchen.model.entity.PoundBill;
import com.yuntun.sanitationkitchen.model.entity.PoundBillStatistic;
import com.yuntun.sanitationkitchen.model.entity.SanitationOfficeValue;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.model.entity.TrashCanValue;
import com.yuntun.sanitationkitchen.model.entity.Vehicle;
import com.yuntun.sanitationkitchen.model.entity.VehicleValue;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.PoundBillVo;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;
import com.yuntun.sanitationkitchen.properties.PoundBillProperties;
import com.yuntun.sanitationkitchen.service.IPoundBillService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ExcelUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  public RowData<PoundBill> pagePoundBill(PoundBillDto dto) {

    IPage<PoundBill> page = new Page<>();
    page.setCurrent(dto.getPageNo());
    page.setSize(dto.getPageSize());
    LambdaQueryWrapper<PoundBill> q = new LambdaQueryWrapper<>();
    q.eq(!StringUtils.isBlank(dto.getSerialCode()), PoundBill::getSerialCode, dto.getSerialCode())
        .eq(dto.getVehicleId() != null, PoundBill::getVehicleId, dto.getVehicleId())
        .eq(dto.getSanitationOfficeId() != null, PoundBill::getSanitationOfficeId, dto.getSanitationOfficeId())
        .like(EptUtil.isNotEmpty(dto.getDriverName()), PoundBill::getDriverName, dto.getDriverName())
        .gt(dto.getBeginTime() != null, PoundBill::getCreateTime, dto.getBeginTime())
        .lt(dto.getEndTime() != null, PoundBill::getCreateTime, dto.getEndTime());
    this.page(page, q);

    RowData<PoundBill> pageBean = new RowData<>();
    pageBean.setTotal(page.getTotal())
        .setRows(page.getRecords());
    return pageBean;

  }

  @Override
  public Integer countCurrentWeight(PoundBillDto dto) {
    Integer total = poundBillMapper.countCurrentWeight(dto);
    if (total != null) {
      return total;
    } else {
      return 0;
    }
  }

  @Override
  public PoundBillStatistic getPoundDateTotal(PoundBillDto dto) {
    PoundBillStatistic poundBillStatistic = poundBillMapper.getPoundDateTotal(dto);
    if (poundBillStatistic != null) {
      return poundBillStatistic;
    } else {
      return null;
    }
  }

  @Override
  public void exportPoundBill(PoundBillDto dto, HttpServletResponse response) {
    LambdaQueryWrapper<PoundBill> q = new LambdaQueryWrapper<>();
    q.eq(!StringUtils.isBlank(dto.getSerialCode()), PoundBill::getSerialCode, dto.getSerialCode())
        .eq(dto.getVehicleId() != null, PoundBill::getVehicleId, dto.getVehicleId())
        .eq(dto.getSanitationOfficeId() != null, PoundBill::getSanitationOfficeId, dto.getSanitationOfficeId())
        .gt(dto.getBeginTime() != null, PoundBill::getCreateTime, dto.getBeginTime())
        .lt(dto.getEndTime() != null, PoundBill::getCreateTime, dto.getEndTime());
    List<PoundBill> list = this.list(q);

    try {
      ExcelUtil.excelExport(response, poundBillProperties.getFileName(), poundBillProperties.getSheetName(), list, poundBillProperties.getHeaders(), poundBillProperties.getColumns());
    } catch (Exception e) {
      log.error("export PoundBill err,{}", e);
      throw new ServiceException(PoundBillCode.EXPORT_EXCEL);
    }
  }

  @Override
  public List<PoundBillStatistic> getWeekWeightList(PoundBillDto dto) {

    List<LocalDate> dateList = dto.getDateList();
    System.out.println("dateList====" + dateList.toString());
    List<PoundBillStatistic> list = new ArrayList<>();
    for (int i = 0; i < dateList.size(); i++) {
      PoundBillStatistic poundBillStatistic;
      LocalDate date = dateList.get(i);
      System.out.println("date====" + date.toString());
      PoundBillDto poundBillDto = new PoundBillDto();
      poundBillDto.setBeginTime(date);
      poundBillDto.setEndTime(getDay2(date.toString()));
      poundBillStatistic = poundBillMapper.getPoundDateTotal(poundBillDto);
      if (poundBillStatistic != null) {
        list.add(poundBillStatistic);
      }
    }
    return list;
  }

  @Override
  public List<PoundBillStatistic> getCurrentMonthPoundTotal() {
    return poundBillMapper.getCurrentMonthPoundTotal();
  }

  @Override
  public List<PoundBillStatistic> getCurrentYearPoundTotal() {
    return poundBillMapper.getCurrentYearPoundTotal();
  }

}
