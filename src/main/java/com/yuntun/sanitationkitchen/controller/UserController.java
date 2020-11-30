package com.yuntun.sanitationkitchen.controller;


import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuntun.sanitationkitchen.aop.Limit;
import com.yuntun.sanitationkitchen.constant.UserConstant;
import com.yuntun.sanitationkitchen.entity.Permission;
import com.yuntun.sanitationkitchen.entity.User;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.dto.UserUpdateDto;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.service.IUserService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 后台管理系统用户表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-11-30
 */

/**
 * <p>
 * 后台管理系统用户表 前端控制器
 * </p>
 *
 * @author whj
 * @since 2020-11-05
 */
@RestController
@RequestMapping("/User")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    public static final int FIVE_MINUTE = 300;

    @Autowired
    IUserService iUserService;

    @Limit("user:listPage")
    @GetMapping("/list/page")
    public Result<RowData<User>> list(Integer pageSize, Integer pageNo, User User) {

        ErrorUtil.isNumberValueLt(pageSize, 0, "pageSize");
        ErrorUtil.isNumberValueLt(pageNo, 0, "pageNo");

        IPage<User> iPage;
        try {
            iPage = iUserService.page(
                    new Page<User>()
                            .setSize(pageSize)
                            .setCurrent(pageNo),
                    new QueryWrapper<User>()
                            .eq(EptUtil.isNotEmpty(User.getUsername()), "User_name", User.getUsername())
                            .eq(EptUtil.isNotEmpty(User.getPhone()), "create_time", User.getPhone())
                            .orderByDesc("id")
            );
        } catch (Exception e) {
            throw new ServiceException(UserCode.LIST_SYSUSER_FAILURE);
        }

        RowData<User> data = new RowData<User>()
                .setRows(iPage.getRecords())
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());
        return Result.ok(data);
    }

    @GetMapping("/get/{id}")
    @Limit("user:get")
    public Result<Object> detail(@PathVariable("id") String id) {
        ErrorUtil.isObjectNull(id, "参数");
        try {
            User User = iUserService.getById(id);
            if (EptUtil.isNotEmpty(User))
                return Result.ok(User);
            return Result.error(UserCode.DETAIL_SYSUSER_FAILURE);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserCode.DETAIL_SYSUSER_FAILURE);
        }

    }

    @PostMapping("/save")
    @Limit("user:save")
    public Result<Object> save(User User) {

        ErrorUtil.isStringEmpty(User.getPhone(), "电话");
        ErrorUtil.isStringLengthOutOfRange(User.getUsername(), 2, 10, "用户名");
        ErrorUtil.isStringLengthOutOfRange(User.getPassword(), 6, 16, "密码");
        String password = User.getPassword();
        User.setPassword(SecureUtil.md5(password));

        long uid = IdUtil.getSnowflake(1, 1).nextId();
        User.setUid(uid);
        //校验用户名是否重复
        List<User> UserList = iUserService.list(
                new QueryWrapper<User>()
                        .eq("username", User.getUsername())
        );
        if (UserList.size() > 0) {
            throw new ServiceException(UserCode.USERNAME_ALREADY_EXISTS);
        }

        //校验手机号是否重复
        List<User> phoneUserList = iUserService.list(
                new QueryWrapper<User>()
                        .eq("phone", User.getPhone())
        );
        if (phoneUserList.size() > 0) {
            throw new ServiceException(UserCode.PHONE_NUMBER_ALREADY_EXISTS);
        }

        try {
            boolean save = iUserService.save(User);
            if (save)
                return Result.ok();
            return Result.error(UserCode.ADD_SYSUSER_FAILURE);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserCode.ADD_SYSUSER_FAILURE);
        }

    }

    @PostMapping("/update")
    @Limit("user:update")
    public Result<Object> update(UserUpdateDto UserDto, String publickey) {

        ErrorUtil.isObjectNull(UserDto.getUid(), "角色id");
        ErrorUtil.isStringEmpty(publickey, "公钥");

        String passwordDecrypt = LoginController.getPasswordDecrypt(UserDto.getPassword(), publickey);

        User User = new User();
        BeanUtils.copyProperties(UserDto, User);
        User.setPassword(BCrypt.hashpw(passwordDecrypt));
        try {
            boolean save = iUserService.updateById(User);
            if (save)
                return Result.ok();
            return Result.error(UserCode.UPDATE_SYSUSER_FAILURE);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserCode.UPDATE_SYSUSER_FAILURE);
        }

    }

    @PostMapping("/password/update")
    @Limit("user:update")
    public Result<Object> update(String username, String oldPassword, String newPassword, String publickey) {

        ErrorUtil.isStringEmpty(username, "用户名");

        ErrorUtil.isStringEmpty(publickey, "公钥");

        //先查询用户是否存在
        User targetUser = iUserService.getOne(new QueryWrapper<User>().eq("username", username));
        if (targetUser == null) {
            log.error("用户不存在");
            throw new ServiceException(UserCode.LOGIN_FAILED_USERNAME_INCORRECT);
        }

        //判断旧密码是否正确
        String passwordDecrypt = LoginController.getPasswordDecrypt(oldPassword, publickey);
        if (!BCrypt.checkpw(passwordDecrypt, targetUser.getPassword())) {
            log.error("密码不正确");
            throw new ServiceException(UserCode.LOGIN_FAILED_PASSWORD_INCORRECT);
        }

        //解密新密码
        String newPasswordDecrypt = LoginController.getPasswordDecrypt(newPassword, publickey);
        ErrorUtil.isStringLengthOutOfRange(newPasswordDecrypt, 6, 16, "新密码");
        String newPasswordMd5 = BCrypt.hashpw(newPasswordDecrypt);
        targetUser.setPassword(newPasswordMd5);
        try {
            boolean save = iUserService.updateById(targetUser);
            if (save)
                return Result.ok();
            return Result.error(UserCode.PASSWORD_UPDATE_ERROR);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserCode.UPDATE_SYSUSER_FAILURE);
        }
    }

    @PostMapping("/delete/{id}")
    @Limit("user:delete")
    public Result<Object> delete(@PathVariable("id") Integer id) {
        ErrorUtil.isObjectNull(id, "信息id");
        try {
            boolean b = iUserService.removeById(id);
            if (b)
                return Result.ok();
            return Result.error(UserCode.DELETE_SYSUSER_FAILURE);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserCode.DELETE_SYSUSER_FAILURE);
        }
    }


    @PostMapping("/disabled/{id}")
    @Limit("user:delete")
    public Result<Object> disable(@PathVariable("id") Integer id) {
        ErrorUtil.isObjectNull(id, "用户id");
        try {
            User User = new User().setDisabled(UserConstant.IS_DISABLED).setId(id);
            boolean b = iUserService.updateById(User);
            if (b)
                return Result.ok();
            return Result.error(UserCode.DISABLED_FAILED);
        } catch (Exception e) {
            log.error("异常:", e);
            throw new ServiceException(UserCode.DISABLED_FAILED);
        }
    }

    @GetMapping("/list/permission/{userId}")
    @Limit("user:listPermission")
    public Result<Object> listPermission(@PathVariable Long userId) {

        ErrorUtil.isObjectNull(userId, "用户id");

        List<Permission> userPermissionList = iUserService.getUserPermissionList(userId);

        return Result.ok(userPermissionList);
    }




}
