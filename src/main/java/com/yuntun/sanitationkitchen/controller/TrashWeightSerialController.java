package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.PoundBillCode;
import com.yuntun.sanitationkitchen.model.dto.BasePageDto;
import com.yuntun.sanitationkitchen.model.dto.TrashWeightSerialDto;
import com.yuntun.sanitationkitchen.model.entity.TrashWeightSerial;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.PoundBillVo;
import com.yuntun.sanitationkitchen.properties.TrashCanSerialProperties;
import com.yuntun.sanitationkitchen.service.ITrashWeightSerialService;
import com.yuntun.sanitationkitchen.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 垃圾桶称重流水
 * @author tang
 * @since 2020/12/15
 */
@Slf4j
@RestController
@RequestMapping("/trash/weight")
public class TrashWeightSerialController {

    @Autowired
    private ITrashWeightSerialService trashWeightSerialService;

    @Autowired
    private TrashCanSerialProperties trashCanSerialProperties;


    LambdaQueryWrapper<TrashWeightSerial> buildCondition(TrashWeightSerialDto dto){
        LambdaQueryWrapper<TrashWeightSerial> q=new LambdaQueryWrapper<>();
        q.like(!StringUtils.isBlank(dto.getCode()),TrashWeightSerial::getFacilityCode,dto.getCode())
                .like(!StringUtils.isBlank(dto.getRestaurantName()),TrashWeightSerial::getRestaurantName,dto.getRestaurantName())
                .like(!StringUtils.isBlank(dto.getRfid()),TrashWeightSerial::getTrashCanRfid,dto.getRfid())
                .orderByDesc(TrashWeightSerial::getCreateTime);
        return q;
    }


    @RequestMapping("/page")
    public Result page(TrashWeightSerialDto dto){
        IPage<TrashWeightSerial> page=new Page<>();
        page.setSize(dto.getPageSize());
        page.setCurrent(dto.getPageNo());
        trashWeightSerialService.page(page,buildCondition(dto));

        RowData<TrashWeightSerial> pageBean = new RowData<TrashWeightSerial>()
                .setRows(page.getRecords())
                .setTotal(page.getTotal());

        return Result.ok(pageBean);
    }

    @RequestMapping("/export")
    public void export(TrashWeightSerialDto dto, HttpServletResponse response){

        try {
            ExcelUtil.excelExport(
                    response,
                    trashCanSerialProperties.getFileName(),
                    trashCanSerialProperties.getSheetName(),
                    trashWeightSerialService.list(buildCondition(dto)),
                    trashCanSerialProperties.getHeaders(),
                    trashCanSerialProperties.getColumns());
        } catch (Exception e) {
            log.error("export PoundBill err,{}",e);
            throw new ServiceException(PoundBillCode.EXPORT_EXCEL);
        }

    }

}