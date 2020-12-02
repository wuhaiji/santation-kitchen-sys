package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.model.entity.RolePermission;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code20000.RolePermissionCode;
import com.yuntun.sanitationkitchen.model.dto.RolePermissionSaveDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IRolePermissionService;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */
@RestController
@RequestMapping("/rolePermission")
public class RolePermissionController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IRolePermissionService iRolePermissionService;


    @PostMapping("/save")
    @Limit("rolePermission:save")
    public Result<Object> save(RolePermissionSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getPermissionId(), "权限id");
        ErrorUtil.isObjectNull(dto.getRoleId(), "角色id");

        RolePermission rolePermission = new RolePermission().setRoleId(dto.getRoleId()).setPermissionId(dto.getPermissionId());

        try {

            boolean save = iRolePermissionService.save(rolePermission);
            if (save)
                return Result.ok();
            return Result.error(RolePermissionCode.SAVE_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(RolePermissionCode.SAVE_ERROR);
        }

    }

    @PostMapping("/delete/{id}")
    @Limit("role:delete")
    public Result<Object> delete(@PathVariable("id") Long id) {
        ErrorUtil.isObjectNull(id, "id");
        try {
            boolean b = iRolePermissionService.remove(new QueryWrapper<RolePermission>().eq("uid",id));
            if (b)
                return Result.ok();
            return Result.error(RolePermissionCode.DELETE_ERROR);
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new ServiceException(RolePermissionCode.DELETE_ERROR);
        }
    }


}
