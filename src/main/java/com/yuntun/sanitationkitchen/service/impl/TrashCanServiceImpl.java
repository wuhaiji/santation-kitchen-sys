package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.TrashCanDto;
import com.yuntun.sanitationkitchen.model.entity.FuelDevice;
import com.yuntun.sanitationkitchen.model.entity.TrashCan;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.FuelDeviceVo;
import com.yuntun.sanitationkitchen.model.vo.TrashCanVo;
import com.yuntun.sanitationkitchen.service.ITrashCanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 垃圾桶表 服务实现类
 * </p>
 *
 * @author whj
 * @since 2020-12-01
 */
@Service
public class TrashCanServiceImpl extends ServiceImpl<TrashCanMapper, TrashCan> implements ITrashCanService {

    @Autowired
    private TrashCanMapper trashCanMapper;

    @Override
    public RowData<TrashCanVo> findTrashCanList(TrashCanDto trashCanDto) {
        IPage<TrashCan> iPage = trashCanMapper.selectPage(
                new Page<TrashCan>()
                        .setSize(trashCanDto.getPageSize())
                        .setCurrent(trashCanDto.getPageNo()),
                new QueryWrapper<TrashCan>()
                        .eq(EptUtil.isNotEmpty(trashCanDto.getFacilityType()), "facility_type", trashCanDto.getFacilityType())
                        .orderByDesc("create_time")
        );
        List<TrashCanVo> trashCanVoList = ListUtil.listMap(TrashCanVo.class, iPage.getRecords());

        RowData<TrashCanVo> trashCanVoRowData = new RowData<TrashCanVo>()
                .setRows(trashCanVoList)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getPages());
        return trashCanVoRowData;
    }

    @Override
    public TrashCanVo findTrashCanByUid(Long uid) {
        TrashCanVo trashCanVo = new TrashCanVo();
        TrashCan trashCan = trashCanMapper.selectOne(new QueryWrapper<TrashCan>().eq("uid", uid));
        if(trashCan == null) {
            return null;
        }
        BeanUtils.copyProperties(trashCan, trashCanVo);
        return trashCanVo;
    }

    @Override
    public Boolean insertTrashCan(TrashCanDto trashCanDto) {
        TrashCan trashCan = new TrashCan();
        BeanUtils.copyProperties(trashCanDto, trashCan);
        trashCan.setUid(SnowflakeUtil.getUnionId());

        int save = trashCanMapper.insert(trashCan);
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean updateTrashCan(TrashCanDto trashCanDto) {
        TrashCan trashCan = new TrashCan().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(trashCanDto, trashCan);
        Integer save = trashCanMapper.update(trashCan,
                new QueryWrapper<TrashCan>().eq("uid", trashCanDto.getUid())
        );
        if (save > 0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean deleteTrashCan(Long uid) {
        TrashCan trashCan = trashCanMapper.selectOne(new QueryWrapper<TrashCan>().eq("uid", uid));
        if (trashCan == null) {
            log.error("删除垃圾桶异常->uid不存在");
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        Integer result = trashCanMapper.delete(new QueryWrapper<TrashCan>().eq("uid", uid));
        if (result > 0)
            return true;
        else
            return false;
    }
}
