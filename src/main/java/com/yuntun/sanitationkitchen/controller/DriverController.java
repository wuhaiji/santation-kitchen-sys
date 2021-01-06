package com.yuntun.sanitationkitchen.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code40000.DriverCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleCode;
import com.yuntun.sanitationkitchen.model.dto.VehicleListDto;
import com.yuntun.sanitationkitchen.model.entity.Driver;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.OptionsVo;
import com.yuntun.sanitationkitchen.service.IDriverService;
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
 * 地磅配置表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-12-28
 */
@RestController
@RequestMapping("/driver")
@Slf4j
public class DriverController {

    @Autowired
    IDriverService iDriverService;

    @Limit("vehicle:driver:query")
    @GetMapping("/list")
    public Result<Object> list(VehicleListDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        Page<Driver> iPage = iDriverService.page(
                new Page<Driver>().setSize(dto.getPageSize()).setCurrent(dto.getPageNo()),
                new QueryWrapper<Driver>().orderByDesc("create_time")

        );

        List<DriverListVo> vos = ListUtil.listMap(DriverListVo.class, iPage.getRecords());
        RowData<DriverListVo> data = new RowData<DriverListVo>().setRows(vos).setTotal(iPage.getTotal()).setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }
    @Limit("vehicle:driver:query")
    @GetMapping("/options")
    public Result<Object> options() {
        List<Driver> list = iDriverService.list();
        List<OptionsVo> optionsVos = list
                .parallelStream()
                .map(i -> new OptionsVo().setLabel(i.getName()).setValue(i.getUid()))
                .collect(Collectors.toList());
        return Result.ok(optionsVos);
    }
    @Limit("vehicle:driver:query")
    @GetMapping("/get/{uid}")
    public Result<Object> get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        Driver byId = iDriverService.getOne(new QueryWrapper<Driver>().eq("uid", uid));
        if (EptUtil.isEmpty(byId)) {
            log.error("vehicle->get->查询司机详情失败,uid:{}", uid);
            return Result.error(DriverCode.ID_NOT_EXIST);
        }
        DriverGetVo DriverGetVo = new DriverGetVo();
        BeanUtils.copyProperties(byId, DriverGetVo);

        return Result.ok(DriverGetVo);
    }

    @Limit("vehicle:driver:save")
    @PostMapping("/save")
    public Result<Object> save(DriverSaveDto dto) {

        ErrorUtil.isStringLengthOutOfRange(dto.getName(), 2, 30, "名称");
        ErrorUtil.isStringLengthOutOfRange(dto.getPhone(), 2, 30, "电话");
        ErrorUtil.isStringLengthOutOfRange(dto.getRfid(), 2, 30, "rfid");

        //查询数据库中是否存在该手机号
        List<Driver> listPhone = iDriverService.list(new LambdaQueryWrapper<Driver>().eq(Driver::getPhone, dto.getPhone()));
        if (listPhone.size() > 0) {
            throw new ServiceException(DriverCode.PHONE_ALREADY_EXISTS);
        }

        //查询数据库中是否存在该手机号
        List<Driver> listRfid = iDriverService.list(new LambdaQueryWrapper<Driver>().eq(Driver::getRfid, dto.getRfid()));
        if (listRfid.size() > 0) {
            throw new ServiceException(DriverCode.RFID_ALREADY_EXISTS);
        }

        Driver Driver = new Driver().setCreator(UserIdHolder.get()).setUid(SnowflakeUtil.getUnionId());
        BeanUtils.copyProperties(dto, Driver);
        boolean save = iDriverService.save(Driver);
        if (!save) {
            log.error("Driver->save->保存失败,DriverSaveDto:{}", JSON.toJSONString(dto));
            return Result.error(DriverCode.SAVE_ERROR);
        }
        return Result.ok();
    }

    @Limit("vehicle:driver:update")
    @PostMapping("/update")
    public Result<Object> update(DriverUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "车辆类型uid不能为空");

        Driver Driver = new Driver().setUpdator(UserIdHolder.get());
        BeanUtils.copyProperties(dto, Driver);
        boolean save = iDriverService.update(Driver,
                new QueryWrapper<Driver>().eq("uid", dto.getUid())
        );
        if (!save) {
            log.error("vehicle->update->修改车辆类型失败,VehicleUpdateDto:{}", JSON.toJSONString(dto));
            return Result.error(DriverCode.UPDATE_ERROR);
        }
        return Result.ok();

    }

    @Limit("vehicle:driver:delete")
    @PostMapping("/delete/{uid}")
    public Result<Object> delete(@PathVariable("uid") Long uid) {

        Driver Driver = iDriverService.getOne(new QueryWrapper<Driver>().eq("uid", uid));
        if (Driver == null) {
            log.error("Driver->delete->uid不存在,uid:{}", uid);
            throw new ServiceException(VehicleCode.ID_NOT_EXIST);
        }

        boolean b = iDriverService.remove(new QueryWrapper<Driver>().eq("uid", uid));

        if (!b) {
            log.error("Driver->delete->删除司机失败,uid:{}", uid);
            return Result.error(DriverCode.DELETE_ERROR);
        }
        return Result.ok();

    }

    @Limit("vehicle:driver:delete")
    @PostMapping("/delete/batch")
    public Result<Object> deleteBatch(@RequestParam(value = "ids", required = false) List<Long> ids) {
        ErrorUtil.isCollectionEmpty(ids, "ids");
        boolean b = iDriverService.remove(new QueryWrapper<Driver>().in("uid", ids));
        if (b)
            return Result.ok();
        return Result.error(VehicleCode.DELETE_ERROR);

    }

    @Data
    @Accessors(chain = true)
    public static class DriverListDto {
        Integer pageSize;
        Integer pageNo;
        private Integer id;
        /**
         * uuid
         */
        private Long uid;
        /**
         * 工牌号（rfid）
         */
        private String rfid;
        /**
         * 司机名称
         */
        private String name;
        /**
         * 电话
         */
        private String phone;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;


    }

    @Data
    @Accessors(chain = true)
    public static class DriverListVo {
        /**
         * uuid
         */
        private Long uid;
        /**
         * 工牌号（rfid）
         */
        private String rfid;
        /**
         * 司机名称
         */
        private String name;
        /**
         * 电话
         */
        private String phone;
        /**
         * 创建时间
         */
        private LocalDateTime createTime;

    }

    @Data
    @Accessors(chain = true)
    public static class DriverGetVo {
        /**
         * uuid
         */
        private Long uid;
        /**
         * 工牌号（rfid）
         */
        private String rfid;
        /**
         * 司机名称
         */
        private String name;
        /**
         * 电话
         */
        private String phone;

        /**
         * 创建时间
         */
        private LocalDateTime createTime;

    }

    @Data
    @Accessors(chain = true)
    public static class DriverSaveDto {
        /**
         * 工牌号（rfid）
         */
        private String rfid;
        /**
         * 司机名称
         */
        private String name;
        /**
         * 电话
         */
        private String phone;
    }

    @Data
    @Accessors(chain = true)
    public static class DriverUpdateDto {
        /**
         * uuid
         */
        private Long uid;
        /**
         * 工牌号（rfid）
         */
        private String rfid;
        /**
         * 司机名称
         */
        private String name;
        /**
         * 电话
         */
        private String phone;

    }

}

