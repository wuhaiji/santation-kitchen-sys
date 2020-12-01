package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.aop.Limit;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.interceptor.UserIdHolder;
import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.code.code20000.PermissionCode;
import com.yuntun.sanitationkitchen.model.code.code20000.RoleCode;
import com.yuntun.sanitationkitchen.model.dto.RoleListPageDto;
import com.yuntun.sanitationkitchen.model.dto.RoleSaveDto;
import com.yuntun.sanitationkitchen.model.dto.RoleUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.model.entity.Role;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.RoleOptionsVo;
import com.yuntun.sanitationkitchen.service.IRoleService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IRoleService iRoleService;

    @Limit("role:list")
    @GetMapping("/list")
    public Result<Object> list(RoleListPageDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Role> iPage;
        try {
            iPage = iRoleService.page(
                    new Page<Role>()
                            .setSize(dto.getPageSize())
                            .setCurrent(dto.getPageNo()),
                    new QueryWrapper<Role>()
                            .eq(EptUtil.isNotEmpty(dto.getRoleName()), "role_name", dto.getRoleName())
                            .orderByDesc("create_time")

            );
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new ServiceException(CommonCode.SERVER_ERROR);
        }

        RowData<Role> data = new RowData<Role>()
                .setRows(iPage.getRecords())
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }

    @Limit("role:options")
    @GetMapping("/options")
    public Result<Object> options() {
        List<Role> list;
        try {
            list = iRoleService.list();
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new ServiceException(RoleCode.OPTIONS_ERROR);
        }
        List<Object> collect = list.parallelStream().map(
                i -> {
                    RoleOptionsVo roleOptionsVo = new RoleOptionsVo();
                    BeanUtils.copyProperties(i, roleOptionsVo);
                    return roleOptionsVo;
                }
        ).collect(Collectors.toList());
        return Result.ok(collect);
    }

    @GetMapping("/get/{id}")
    @Limit("role:get")
    public Result<Object> get(@PathVariable("id") Long id) {
        ErrorUtil.isObjectNull(id, "参数");
        try {
            Role byId = iRoleService.getById(id);
            if (EptUtil.isNotEmpty(byId))
                return Result.ok(byId);
            return Result.error(RoleCode.GET_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(RoleCode.GET_ERROR);
        }

    }

    @PostMapping("/save")
    @Limit("role:save")
    public Result<Object> save(RoleSaveDto dto) {

        ErrorUtil.isStringLengthOutOfRange(dto.getRoleName(), 2, 30, "角色名称");
        ErrorUtil.isObjectNull(dto.getRoleType(), "角色类型");
        checkRepeatedValue(dto.getRoleName());


        Role role = new Role()
                .setRoleType(dto.getRoleType())
                .setRoleName(dto.getRoleName())
                .setUid(SnowflakeUtil.getUnionId())
                .setCreator(UserIdHolder.get());

        try {
            boolean save = iRoleService.save(role);
            if (save)
                return Result.ok();
            return Result.error(RoleCode.ADD_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(RoleCode.ADD_ERROR);
        }

    }

    private void checkRepeatedValue(String roleName) {
        //检查数据库是否存在同名权限
        List<Role> listName = iRoleService.list(
                new QueryWrapper<Role>().eq("permission_name", roleName)
        );
        if (listName.size() > 0) {
            throw new ServiceException(RoleCode.NAME_ALREADY_EXISTS_ERROR);
        }
    }

    @PostMapping("/update")
    @Limit("role:update")
    public Result<Object> update(RoleUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getRoleId(), "角色id");

        //检查数据库是否存在同名权限
        checkRepeatedValue(dto.getRoleName());

        Role role = new Role().setRoleName(dto.getRoleName()).setRoleType(true);
        try {
            boolean save = iRoleService.update(role,
                    new QueryWrapper<Role>().eq("uid", dto.getRoleId())
            );
            if (save)
                return Result.ok();
            return Result.error(RoleCode.UPDATE_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(RoleCode.UPDATE_ERROR);
        }

    }

    @PostMapping("/delete/{id}")
    @Limit("role:delete")
    public Result<Object> delete(@PathVariable("id") Long id) {
        ErrorUtil.isObjectNull(id, "id");
        try {
            boolean b = iRoleService.removeById(id);
            if (b)
                return Result.ok();
            return Result.error(RoleCode.DELETE_ERROR);
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new ServiceException(RoleCode.DELETE_ERROR);
        }
    }

    @PostMapping("/disable/{uid}/{disabled}")
    @Limit("role:disable")
    public Result<Object> disable(@PathVariable("uid") Long uid, @PathVariable Integer disabled) {

        ErrorUtil.isObjectNull(uid, "角色id");
        ErrorUtil.isNumberOutOfRange(disabled, 0, 1, "禁用状态");

        QueryWrapper<Role> query = new QueryWrapper<Role>().eq("uid", uid);
        Role role = iRoleService.getOne(query);
        if (role == null) {
            log.error("禁用角色->角色id不存在");
            throw new ServiceException(RoleCode.ROLE_NOT_EXIST);
        }
        role.setDisabled(disabled);
        role.setDisabledBy(UserIdHolder.get());
        boolean b = iRoleService.update(role, query);
        if (b)
            return Result.ok();
        return Result.error(RoleCode.DELETE_ERROR);

    }
}
