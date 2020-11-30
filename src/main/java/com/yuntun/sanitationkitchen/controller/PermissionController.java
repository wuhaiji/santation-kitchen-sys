package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.aop.Limit;
import com.yuntun.sanitationkitchen.entity.Permission;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code20000.PermissionCode;
import com.yuntun.sanitationkitchen.model.dto.PermissionListPageDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.service.IPermissionService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Limit("permission:listPage")
    @GetMapping("/list/page")
    public Result<Object> listPage(PermissionListPageDto dto) {

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
            throw new ServiceException(PermissionCode.LIST_PAGE_PERMISSION_BY_USERID_ERROR);
        }

        RowData<Permission> data = new RowData<Permission>()
                .setRows(iPage.getRecords())
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }

    @GetMapping("/get/{id}")
    @Limit("Permission:get")
    public Result<Object> get(@PathVariable("id") Long id) {
        ErrorUtil.isObjectNull(id, "参数");
        try {
            Permission Permission = iPermissionService.getById(id);
            if (EptUtil.isNotEmpty(Permission))
                return Result.ok(Permission);
            return Result.error(PermissionCode.GET_PERMISSION_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(PermissionCode.GET_PERMISSION_ERROR);
        }

    }


}
