package com.yuntun.sanitationkitchen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.mapper.RestaurantMapper;
import com.yuntun.sanitationkitchen.mapper.TrashCanMapper;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.TrashCanDto;
import com.yuntun.sanitationkitchen.model.entity.*;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.SelectOptionVo;
import com.yuntun.sanitationkitchen.model.vo.TrashCanVo;
import com.yuntun.sanitationkitchen.service.ITrashCanService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Override
    public SelectOptionVo selectTrashCanOption() {
        SelectOptionVo selectOptionVo = new SelectOptionVo();

        // 餐馆uid、餐馆名
        List<RestaurantValue> restaurantValueList = restaurantMapper.selectList(new QueryWrapper<Restaurant>().
                select("uid", "name")).stream().map(restaurant -> {
            RestaurantValue restaurantValue = new RestaurantValue();
            restaurantValue.setRestaurantId(restaurant.getUid());
            restaurantValue.setRestaurantName(restaurant.getName());
            return restaurantValue;
        }).collect(Collectors.toList());
        selectOptionVo.setRestaurantList(restaurantValueList);
        return selectOptionVo;
    }

    @Override
    public RowData<TrashCanVo> findTrashCanList(TrashCanDto trashCanDto) {
        List<TrashCanVo> trashCanVoList;
        IPage<TrashCan> iPage = trashCanMapper.selectPage(
                new Page<TrashCan>()
                        .setSize(Optional.ofNullable(trashCanDto.getPageSize()).orElse(10))
                        .setCurrent(Optional.ofNullable(trashCanDto.getPageNo()).orElse(1)),
                new QueryWrapper<TrashCan>()
                        .like(EptUtil.isNotEmpty(trashCanDto.getFacilityCode()), "facility_code", trashCanDto.getFacilityCode())
                        .eq(EptUtil.isNotEmpty(trashCanDto.getFacilityType()), "facility_type", trashCanDto.getFacilityType())
                        .like(EptUtil.isNotEmpty(trashCanDto.getAddress()), "address", trashCanDto.getAddress())
                        .eq(EptUtil.isNotEmpty(trashCanDto.getRestaurantId()), "restaurant_id", trashCanDto.getRestaurantId())
                        .orderByDesc("create_time")
        );

        trashCanVoList = iPage.getRecords().stream().map(trashCan -> {
            TrashCanVo trashCanVo = new TrashCanVo();
            BeanUtils.copyProperties(trashCan, trashCanVo);
            double res = (double)trashCan.getReserve()/trashCan.getCapacity()*100;
            trashCanVo.setPercent(Math.ceil(res)+"%");
            return trashCanVo;
        }).collect(Collectors.toList());
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
        TrashCan trashCan = new TrashCan().setCreator(UserIdHolder.get());
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
    public Boolean deleteTrashCan(List<Long> uids) {
        uids.forEach(uid -> {
            TrashCan trashCan = trashCanMapper.selectOne(new QueryWrapper<TrashCan>().eq("uid", uid));
            if (trashCan == null) {
                log.error("删除垃圾桶异常->uid不存在");
                throw new ServiceException(VehicleCode.ID_NOT_EXIST);
            }
        });

        TrashCan trashCan = new TrashCan().setDeletedBy(UserIdHolder.get()).setDeleted(1);
        Integer result = trashCanMapper.update(trashCan, new QueryWrapper<TrashCan>().in("uid", uids));
        Integer result2 = trashCanMapper.delete(new QueryWrapper<TrashCan>().in("uid", uids));
        if (result > 0 && result2 > 0)
            return true;
        else
            return false;
    }
}
