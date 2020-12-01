package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.aop.Limit;
import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.interceptor.UserIdHolder;
import com.yuntun.sanitationkitchen.model.code.code20000.PermissionCode;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.dto.PermissionListPageDto;
import com.yuntun.sanitationkitchen.model.dto.PermissionOptionsDto;
import com.yuntun.sanitationkitchen.model.dto.PermissionSaveDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.PermissionListVo;
import com.yuntun.sanitationkitchen.model.vo.PermissionOptionsVo;
import com.yuntun.sanitationkitchen.service.IPermissionService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());


    @Autowired
    IPermissionService iPermissionService;

    @Limit("permission:list")
    @GetMapping("/list")
    public Result<Object> list(PermissionListPageDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Permission> iPage;
        try {
            iPage = iPermissionService.page(
                    new Page<Permission>()
                            .setSize(dto.getPageSize())
                            .setCurrent(dto.getPageNo()),
                    new QueryWrapper<Permission>()
                            .orderByDesc("create_time")

            );
        } catch (Exception e) {
            log.error("Exception:",e);
            throw new ServiceException(CommonCode.SERVER_ERROR);
        }

        List<Permission> records = iPage.getRecords();
        List<PermissionListVo> collect = records.parallelStream().map(
                i -> new PermissionListVo()
                        .setParentId(i.getParentId())
                        .setCreateTime(i.getCreateTime())
                        .setCreator(i.getCreator())
                        .setPermissionName(i.getPermissionName())
                        .setPermissionTag(i.getPermissionTag())
        ).collect(Collectors.toList());
        RowData<PermissionListVo> data = new RowData<PermissionListVo>()
                .setRows(collect)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }

    @Limit("permission:options")
    @GetMapping("/options")
    public Result<Object> options(PermissionOptionsDto dto) {

        ErrorUtil.isObjectNull(dto.getParentId(), "父级id不能为空");
        List<Permission> permissionList;
        try {
            permissionList = iPermissionService.list(
                    new QueryWrapper<Permission>()
                            .eq("parent_id", dto.getParentId())
            );
        } catch (Exception e) {
            throw new ServiceException(PermissionCode.LIST_PAGE_PERMISSION_BY_USERID_ERROR);
        }
        List<Object> collect = permissionList.parallelStream().map(
                i -> new PermissionOptionsVo()
                        .setParentId(i.getParentId())
                        .setPermissionName(i.getPermissionName())
                        .setPermissionTag(i.getPermissionTag())
        ).collect(Collectors.toList());
        return Result.ok(collect);

    }

    @GetMapping("/get/{id}")
    @Limit("Permission:get")
    public Result<Object> get(@PathVariable("id") Long id) {
        ErrorUtil.isObjectNull(id, "参数");
        try {
            Permission Permission = iPermissionService.getById(id);
            if (EptUtil.isNotEmpty(Permission))
                return Result.ok(Permission);
            return Result.error(PermissionCode.GET_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(PermissionCode.GET_ERROR);
        }

    }

    @PostMapping("/save")
    @Limit("permission:save")
    public Result<Object> save(PermissionSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getParentId(), "父级id");
        ErrorUtil.isObjectNull(dto.getPermissionName(), "权限名称");
        ErrorUtil.isObjectNull(dto.getPermissionTag(), "权限标识");

        Permission permission = new Permission()
                .setUid(SnowflakeUtil.getUnionId())
                .setPermissionName(dto.getPermissionName())
                .setPermissionTag(dto.getPermissionTag())
                .setParentId(dto.getParentId())
                .setCreator(UserIdHolder.get());
        try {
            boolean save = iPermissionService.save(permission);
            if (save)
                return Result.ok();
            return Result.error(PermissionCode.SAVE_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserCode.ADD_SYSUSER_FAILURE);
        }
    }
}
