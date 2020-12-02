package com.yuntun.sanitationkitchen.service;

import com.yuntun.sanitationkitchen.model.dto.SanitationOfficeDto;
import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.SanitationOfficeVo;

import java.util.List;

/**
 * <p>
 * 后台管理系统用户表 服务类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
public interface ISanitationOfficeService extends IService<SanitationOffice> {

    RowData<SanitationOfficeVo> findSanitationOfficeServiceList(SanitationOfficeDto sanitationOfficeDto);

    SanitationOfficeVo findSanitationOfficeServiceByUid(Long uid);

    Boolean insertSanitationOffice(SanitationOfficeDto sanitationOfficeDto);

    Boolean updateSanitationOffice(SanitationOfficeDto sanitationOfficeDto);

    Boolean deleteSanitationOffice(Long uid);
}
