package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.SanitationOfficeMapper;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.SanitationOfficeDto;
import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.SanitationOfficeVo;
import com.yuntun.sanitationkitchen.service.ISanitationOfficeService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private SanitationOfficeMapper sanitationOfficeMapper;

    @Override
    public RowData<SanitationOfficeVo> findSanitationOfficeServiceList(SanitationOfficeDto sanitationOfficeDto) {
        IPage<SanitationOffice> iPage = sanitationOfficeMapper.selectPage(
                new Page<SanitationOffice>()
                        .setSize(sanitationOfficeDto.getPageSize())
                        .setCurrent(sanitationOfficeDto.getPageNo()),
                new QueryWrapper<SanitationOffice>()
                        .eq(EptUtil.isNotEmpty(sanitationOfficeDto.getName()), "name", sanitationOfficeDto.getName())
                        .eq(EptUtil.isNotEmpty(sanitationOfficeDto.getManagerId()), "manager_id", sanitationOfficeDto.getManagerId())
                        .orderByDesc("create_time")
        );
        List<SanitationOfficeVo> sanitationOfficeVoList = ListUtil.listMap(SanitationOfficeVo.class, iPage.getRecords());

        RowData<SanitationOfficeVo> sanitationOfficeVoRowData = new RowData<SanitationOfficeVo>()
                .setRows(sanitationOfficeVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return sanitationOfficeVoRowData;
    }

    @Override
    public SanitationOfficeVo findSanitationOfficeServiceByUid(Long uid) {
        SanitationOfficeVo sanitationOfficeVo = new SanitationOfficeVo();
        SanitationOffice sanitationOffice = sanitationOfficeMapper.selectOne(new QueryWrapper<SanitationOffice>().eq("uid", uid));
        if(sanitationOffice == null) {
            return null;
        }
        BeanUtils.copyProperties(sanitationOffice, sanitationOfficeVo);
        return sanitationOfficeVo;
    }

    @Override
    public Boolean insertSanitationOffice(SanitationOfficeDto sanitationOfficeDto) {
        SanitationOffice sanitationOffice = new SanitationOffice();
        BeanUtils.copyProperties(sanitationOfficeDto, sanitationOffice);
        sanitationOffice.setUid(SnowflakeUtil.getUnionId());

        int save = sanitationOfficeMapper.insert(sanitationOffice);
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean updateSanitationOffice(SanitationOfficeDto sanitationOfficeDto) {
        SanitationOffice sanitationOffice = new SanitationOffice().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(sanitationOfficeDto, sanitationOffice);
        Integer save = sanitationOfficeMapper.update(sanitationOffice,
                new QueryWrapper<SanitationOffice>().eq("uid", sanitationOfficeDto.getUid())
        );
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean deleteSanitationOffice(Long uid) {
        SanitationOffice sanitationOffice = sanitationOfficeMapper.selectOne(new QueryWrapper<SanitationOffice>().eq("uid", uid));
        if (sanitationOffice == null) {
            log.error("删除后台管理系统用户表异常->uid不存在");
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        Integer result = sanitationOfficeMapper.delete(new QueryWrapper<SanitationOffice>().eq("uid", uid));
        if (result > 0)
            return true;
        else
            return false;
    }
}
