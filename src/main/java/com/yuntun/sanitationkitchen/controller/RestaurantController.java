package com.yuntun.sanitationkitchen.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.RestaurantCode;
import com.yuntun.sanitationkitchen.model.entity.Restaurant;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.service.IRestaurantService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 餐馆表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-10
 */
@RestController
@RequestMapping("/restaurant")
@Slf4j
public class RestaurantController {

    @Autowired
    IRestaurantService iRestaurantService;


    @GetMapping("/list")
    @Limit("restaurant:query")
    public Result<Object> list(RestaurantListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Restaurant> iPage = iRestaurantService.page(
                new Page<Restaurant>().setSize(dto.getPageSize()).setCurrent(dto.getPageNo()),
                new QueryWrapper<Restaurant>()
                        .likeRight(EptUtil.isNotEmpty(dto.getName()),"name",dto.getName())
                        .orderByDesc("create_time")

        );

        List<RestaurantListVo> vos = ListUtil.listMap(RestaurantListVo.class, iPage.getRecords());
        RowData<RestaurantListVo> data = new RowData<RestaurantListVo>().setRows(vos).setTotal(iPage.getTotal()).setTotalPages(iPage.getTotal());

        return Result.ok(data);
    }

    @GetMapping("/options")
    @Limit("restaurant:query")
    public Result<Object> options() {
        List<Restaurant> list = iRestaurantService.list();
        List<OptionsVo> optionsVos = list
                .parallelStream()
                .map(i -> new OptionsVo().setLabel(i.getName()).setValue(i.getUid()))
                .collect(Collectors.toList());
        return Result.ok(optionsVos);
    }

    @GetMapping("/get/{uid}")
    @Limit("restaurant:query")
    public Result<Object> get(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "参数");
        Restaurant byId = iRestaurantService.getOne(new QueryWrapper<Restaurant>().eq("uid", uid));
        if (EptUtil.isEmpty(byId)) {
            log.error("Restaurant->get->查询餐馆详情失败,uid:{}", uid);
            return Result.error(RestaurantCode.ID_NOT_EXIST);
        }
        RestaurantGetVo vo = new RestaurantGetVo();
        BeanUtils.copyProperties(byId, vo);

        return Result.ok(vo);
    }

    @PostMapping("/save")
    @Limit("restaurant:save")
    public Result<Object> save(RestaurantSaveDto dto) {

        ErrorUtil.isStringLengthOutOfRange(dto.getName(), 2, 16, "名称不能为空");

        Restaurant restaurant = new Restaurant().setCreator(UserIdHolder.get()).setUid(SnowflakeUtil.getUnionId());
        BeanUtils.copyProperties(dto, restaurant);

        boolean save = iRestaurantService.save(restaurant);
        if (!save) {
            log.error("Restaurant->save->保存失败,dto:{}", JSON.toJSONString(dto));
            return Result.error(RestaurantCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/update")
    @Limit("restaurant:update")
    public Result<Object> update(RestaurantUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "餐馆uid不能为空");

        Restaurant entity = new Restaurant().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(dto, entity);
        boolean save = iRestaurantService.update(entity,
                new QueryWrapper<Restaurant>().eq("uid", dto.getUid())
        );
        if (!save) {
            log.error("Restaurant->update->修改餐馆失败,dto:{}", JSON.toJSONString(dto));
            return Result.error(RestaurantCode.UPDATE_ERROR);
        }
        return Result.ok();

    }

    @PostMapping("/delete/{uid}")
    @Limit("restaurant:delete")
    public Result<Object> delete(@PathVariable("uid") Long uid) {

        ErrorUtil.isObjectNull(uid, "uid");

        Restaurant entity = iRestaurantService.getOne(new QueryWrapper<Restaurant>().eq("uid", uid));
        if (entity == null) {
            log.error("Restaurant->delete->uid不存在,uid:{}", uid);
            throw new ServiceException(RestaurantCode.ID_NOT_EXIST);
        }

        boolean b = iRestaurantService.remove(new QueryWrapper<Restaurant>().eq("uid", uid));

        if (!b) {
            log.error("Restaurant->delete->删除餐馆失败,uid:{}", uid);
            return Result.error(RestaurantCode.DELETE_ERROR);
        }
        return Result.ok();

    }

    @PostMapping("/delete/batch")
    @Limit("restaurant:delete")
    public Result<Object> deleteBatch(@RequestParam("ids") List<Long> ids) {
        ErrorUtil.isCollectionEmpty(ids, "ids");
        boolean b = iRestaurantService.remove(new QueryWrapper<Restaurant>().in("uid", ids));
        if (b)
            return Result.ok();
        return Result.error(RestaurantCode.DELETE_ERROR);

    }


    @Data
    @Accessors(chain = true)
    public static class RestaurantListDto {
        Integer pageSize;
        Integer pageNo;
        private Integer id;
        /**
         * uuid
         */
        private Long uid;
        /**
         * 餐馆名称
         */
        private String name;
        /**
         * 负责人
         */
        private String managerName;
        /**
         * 电话
         */
        private String phone;
        /**
         * 餐馆地址
         */
        private String address;
        /**
         * 创建人id
         */
        private Long creator;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;
        /**
         * 禁用状态
         */
        private Integer disabled;
        /**
         * 禁用人id
         */
        private Long disabledBy;
        /**
         * 禁用时间
         */
        private LocalDateTime disabledTime;
        /**
         * 修改者id
         */
        private Long updator;
        /**
         * 修改时间
         */
        private LocalDateTime updateTime;
        /**
         * 删除状态
         */
        private Integer deleted;
        /**
         * 删除人
         */
        private Long deletedBy;
        /**
         * 删除时间
         */
        private LocalDateTime deletedTime;
    }


    @Data
    @Accessors(chain = true)
    public static class RestaurantListVo {
        /**
         * uuid
         */
        private Long uid;
        /**
         * 餐馆名称
         */
        private String name;
        /**
         * 负责人
         */
        private String managerName;
        /**
         * 电话
         */
        private String phone;
        /**
         * 餐馆地址
         */
        private String address;
        /**
         * 创建人id
         */
        private Long creator;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;
        /**
         * 禁用状态
         */
        private Integer disabled;
        /**
         * 禁用人id
         */
        private Long disabledBy;
        /**
         * 禁用时间
         */
        private LocalDateTime disabledTime;
        /**
         * 修改者id
         */
        private Long updator;
        /**
         * 修改时间
         */
        private LocalDateTime updateTime;
        /**
         * 删除状态
         */
        private Integer deleted;
        /**
         * 删除人
         */
        private Long deletedBy;
        /**
         * 删除时间
         */
        private LocalDateTime deletedTime;
    }

    @Data
    @Accessors(chain = true)
    public static class RestaurantGetVo {
        /**
         * uuid
         */
        private Long uid;
        /**
         * 餐馆名称
         */
        private String name;
        /**
         * 负责人
         */
        private String managerName;
        /**
         * 电话
         */
        private String phone;
        /**
         * 餐馆地址
         */
        private String address;
        /**
         * 创建人id
         */
        private Long creator;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;
        /**
         * 禁用状态
         */
        private Integer disabled;
        /**
         * 禁用人id
         */
        private Long disabledBy;
        /**
         * 禁用时间
         */
        private LocalDateTime disabledTime;
        /**
         * 修改者id
         */
        private Long updator;
        /**
         * 修改时间
         */
        private LocalDateTime updateTime;
        /**
         * 删除状态
         */
        private Integer deleted;
        /**
         * 删除人
         */
        private Long deletedBy;
        /**
         * 删除时间
         */
        private LocalDateTime deletedTime;
    }


    @Data
    @Accessors(chain = true)
    public static class RestaurantSaveDto {
        /**
         * uuid
         */
        private Long uid;
        /**
         * 餐馆名称
         */
        private String name;
        /**
         * 负责人
         */
        private String managerName;
        /**
         * 电话
         */
        private String phone;
        /**
         * 餐馆地址
         */
        private String address;

    }

    @Data
    @Accessors(chain = true)
    public static class RestaurantUpdateDto {
        /**
         * uuid
         */
        private Long uid;
        /**
         * 餐馆名称
         */
        private String name;
        /**
         * 负责人
         */
        private String managerName;
        /**
         * 电话
         */
        private String phone;
        /**
         * 餐馆地址
         */
        private String address;

    }
}

