package com.yuntun.sanitationkitchen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.aop.Limit;
import com.yuntun.sanitationkitchen.entity.UserRole;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code20000.RoleCode;
import com.yuntun.sanitationkitchen.model.code.code20000.UserRoleCode;
import com.yuntun.sanitationkitchen.model.dto.UserRoleSaveDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.service.IUserRoleService;
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
@RequestMapping("/userRole")
public class UserRoleController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IUserRoleService iUserRoleService;


    @PostMapping("/save")
    @Limit("UserRole:save")
    public Result<Object> save(UserRoleSaveDto dto) {

        ErrorUtil.isObjectNull(dto.getUserId(), "用户id");
        ErrorUtil.isObjectNull(dto.getRoleId(), "角色id");

        UserRole UserRole = new UserRole().setRoleId(dto.getRoleId()).setUserId(dto.getUserId());

        try {

            boolean save = iUserRoleService.save(UserRole);
            if (save)
                return Result.ok();
            return Result.error(UserRoleCode.SAVE_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserRoleCode.SAVE_ERROR);
        }

    }

    @PostMapping("/delete/{id}")
    @Limit("role:delete")
    public Result<Object> delete(@PathVariable("id") Long id) {
        ErrorUtil.isObjectNull(id, "id");
        try {
            boolean b = iUserRoleService.remove(new QueryWrapper<UserRole>().eq("uid",id));
            if (b)
                return Result.ok();
            return Result.error(UserRoleCode.DELETE_ERROR);
        } catch (Exception e) {
            log.error("Exception:", e);
            throw new ServiceException(UserRoleCode.DELETE_ERROR);
        }
    }
}
