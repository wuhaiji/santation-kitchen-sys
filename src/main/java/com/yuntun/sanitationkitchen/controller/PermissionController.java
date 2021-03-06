package com.yuntun.sanitationkitchen.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code20000.PermissionCode;
import com.yuntun.sanitationkitchen.model.code.code40000.VehicleTypeCode;
import com.yuntun.sanitationkitchen.model.dto.*;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.model.entity.VehicleType;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.PermissionListVo;
import com.yuntun.sanitationkitchen.model.vo.PermissionOptionsVo;
import com.yuntun.sanitationkitchen.model.vo.TreeNodeVo;
import com.yuntun.sanitationkitchen.service.IPermissionService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Limit("system:permission:query")
    @GetMapping("/list")
    public Result<Object> list(PermissionListPageDto dto) {

        ErrorUtil.PageParamError(dto.getPageSize(), dto.getPageNo());

        IPage<Permission> iPage = iPermissionService.page(
                new Page<Permission>()
                        .setSize(dto.getPageSize())
                        .setCurrent(dto.getPageNo()),
                new QueryWrapper<Permission>()
                        .like(EptUtil.isNotEmpty(dto.getPermissionName()),"permission_name",dto.getPermissionName())
                        .orderByDesc("create_time")

        );

        List<Permission> records = iPage.getRecords();
        List<PermissionListVo> permissionListVos = ListUtil.listMap(PermissionListVo.class, records);
        RowData<PermissionListVo> data = new RowData<PermissionListVo>()
                .setRows(permissionListVos)
                .setTotal(iPage.getTotal())
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }

    @Limit("system:permission:query")
    @GetMapping("/options/tree")
    public Result<Object> listTree() {

        //先查出目录
        List<Permission> catalogs = iPermissionService.list(new QueryWrapper<Permission>().eq("permission_type", 0));
        //再查出菜单
        List<Permission> menus = iPermissionService.list(new QueryWrapper<Permission>().eq("permission_type", 1));
        //再查出按钮
        List<Permission> buttons = iPermissionService.list(new QueryWrapper<Permission>().eq("permission_type", 2));

        //组装数据
        Map<Long, List<TreeNodeVo>> buttonsMapGroup = buttons.parallelStream()
                .map(
                        i -> new TreeNodeVo()
                                .setLabel(i.getPermissionName())
                                .setValue(i.getUid())
                                .setParentId(i.getParentId())
                ).collect(Collectors.groupingBy(TreeNodeVo::getParentId));

        Map<Long, List<TreeNodeVo>> menusMapGroup = menus.parallelStream()
                .map(
                        i -> {
                            TreeNodeVo treeNodeVo = new TreeNodeVo()
                                    .setLabel(i.getPermissionName())
                                    .setValue(i.getUid())
                                    .setParentId(i.getParentId());
                            List<TreeNodeVo> treeNodeVos = buttonsMapGroup.get(i.getUid());
                            if (treeNodeVos != null) {
                                treeNodeVo.setChildren(treeNodeVos);
                            }
                            return treeNodeVo;
                        }
                )
                .collect(Collectors.groupingBy(TreeNodeVo::getParentId));


        ArrayList<TreeNodeVo> catalogNodes = new ArrayList<>();

        for (Permission catalog : catalogs) {
            TreeNodeVo catalogNode = new TreeNodeVo();
            catalogNode.setLabel(catalog.getPermissionName());
            catalogNode.setValue(catalog.getUid());
            List<TreeNodeVo> menuNodes = menusMapGroup.get(catalog.getUid());
            catalogNode.setChildren(menuNodes);
            catalogNodes.add(catalogNode);
        }

        TreeNodeVo treeNodeVo = new TreeNodeVo();
        treeNodeVo.setLabel("主目录");
        treeNodeVo.setValue(0L);
        treeNodeVo.setChildren(catalogNodes);
        return Result.ok(new ArrayList<TreeNodeVo>() {{
            add(treeNodeVo);
        }});

    }

    @Limit("system:permission:query")
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
            throw new ServiceException(PermissionCode.OPTIONS_ERROR);
        }
        List<Object> collect = permissionList.parallelStream().map(
                i -> new PermissionOptionsVo()
                        .setParentId(i.getParentId())
                        .setPermissionName(i.getPermissionName())
                        .setPermissionTag(i.getPermissionTag())
        ).collect(Collectors.toList());
        return Result.ok(collect);

    }

    @Limit("system:permission:query")
    @GetMapping("/get/{uid}")
    public Result<Object> get(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        Permission Permission = iPermissionService.getOne(new QueryWrapper<Permission>().eq("uid", uid));
        if (EptUtil.isNotEmpty(Permission))
            return Result.ok(Permission);
        return Result.error(PermissionCode.GET_ERROR);
    }

    @Limit("system:permission:save")
    @PostMapping("/save")
    public Result<Object> save(PermissionSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getParentId(), "父级id");
        ErrorUtil.isObjectNull(dto.getPermissionName(), "权限名称");
        ErrorUtil.isObjectNull(dto.getPermissionTag(), "权限标识");


        checkRepeatedValue(dto);

        Permission permission = new Permission()
                .setUid(SnowflakeUtil.getUnionId())
                .setPermissionName(dto.getPermissionName())
                .setPermissionTag(dto.getPermissionTag())
                .setPermissionType(dto.getPermissionType())
                .setParentId(dto.getParentId())
                .setCreator(UserIdHolder.get());

        boolean save = iPermissionService.save(permission);
        if (save)
            return Result.ok();
        return Result.error(PermissionCode.SAVE_ERROR);

    }

    @Limit("system:permission:update")
    @PostMapping("/update")
    public Result<Object> update(PermissionUpdateDto dto) {

        ErrorUtil.isObjectNull(dto.getUid(), "权限uid不能为空");

        Permission permission = new Permission().setUpdator(UserIdHolder.get());

        BeanUtils.copyProperties(dto, permission);
        boolean save = iPermissionService.update(
                permission,
                new QueryWrapper<Permission>().eq("uid", dto.getUid())
        );
        if (!save) {
            log.error("permission->update->修改权限失败,PermissionUpdateDto:{}", JSON.toJSONString(dto));
            return Result.error(PermissionCode.UPDATE_ERROR);
        }
        return Result.ok();

    }


    private void checkRepeatedValue(PermissionSaveDto dto) {
        //检查数据库是否存在同名权限
        List<Permission> permissionsOne = iPermissionService.list(
                new QueryWrapper<Permission>().eq("permission_name", dto.getPermissionName())
        );
        if (permissionsOne.size() > 0) {
            throw new ServiceException(PermissionCode.NAME_ALREADY_EXISTS_ERROR);
        }

        //检查数据库是否存在同标识权限
        List<Permission> permissionsTag = iPermissionService.list(
                new QueryWrapper<Permission>().eq("permission_tag", dto.getPermissionTag())
        );
        if (permissionsTag.size() > 0) {
            throw new ServiceException(PermissionCode.TAG_ALREADY_EXISTS_ERROR);
        }
    }
}
