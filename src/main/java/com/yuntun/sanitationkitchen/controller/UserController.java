package com.yuntun.sanitationkitchen.controller;


import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuntun.sanitationkitchen.auth.Limit;
import com.yuntun.sanitationkitchen.auth.UserIdHolder;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.code.code20000.RoleCode;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.dto.UserListDto;
import com.yuntun.sanitationkitchen.model.dto.UserSaveDto;
import com.yuntun.sanitationkitchen.model.dto.UserUpdateDto;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.model.entity.Role;
import com.yuntun.sanitationkitchen.model.entity.SanitationOffice;
import com.yuntun.sanitationkitchen.model.entity.User;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.model.response.RowData;
import com.yuntun.sanitationkitchen.model.vo.UserGetVo;
import com.yuntun.sanitationkitchen.model.vo.UserListVo;
import com.yuntun.sanitationkitchen.service.IRoleService;
import com.yuntun.sanitationkitchen.service.ISanitationOfficeService;
import com.yuntun.sanitationkitchen.service.IUserService;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.ErrorUtil;
import com.yuntun.sanitationkitchen.util.ListUtil;
import com.yuntun.sanitationkitchen.util.SnowflakeUtil;
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
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Autowired
    IUserService iUserService;
    @Autowired
    IRoleService iRoleService;

    @Autowired
    ISanitationOfficeService iSanitationOfficeService;

    @Limit("user:list")
    @GetMapping("/list")
    public Result<Object> list(UserListDto dto) {

        ErrorUtil.isNumberValueLt(dto.getPageSize(), 0, "pageSize");
        ErrorUtil.isNumberValueLt(dto.getPageNo(), 0, "pageNo");

        IPage<User> iPage = iUserService.listPage(dto);

        List<User> records = iPage.getRecords();
        List<UserListVo> userListVos = ListUtil.listMap(UserListVo.class, records);

        RowData<UserListVo> data = new RowData<UserListVo>()
                .setRows(userListVos)
                .setTotal(iPage.getTotal())
                .setTotalPages(iPage.getTotal());

        return Result.ok(data);
    }

    @GetMapping("/get/{uid}")
    @Limit("user:get")
    public Result<Object> detail(@PathVariable("uid") String uid) {
        ErrorUtil.isObjectNull(uid, "参数");
        User user = iUserService.getOne(new QueryWrapper<User>().eq("uid", uid));
        if (EptUtil.isEmpty(user)) {
            throw new ServiceException(UserCode.USER_DOES_NOT_EXIST);
        }
        UserGetVo userGetVo = new UserGetVo();
        BeanUtils.copyProperties(user, userGetVo);
        return Result.ok(userGetVo);
    }

    @PostMapping("/save")
    @Limit("user:save")
    public Result<Object> save(UserSaveDto dto) {

        ErrorUtil.isStringEmpty(dto.getPhone(), "电话");
        ErrorUtil.isStringLengthOutOfRange(dto.getUsername(), 2, 10, "用户名");
        ErrorUtil.isStringLengthOutOfRange(dto.getPassword(), 6, 16, "密码");
        ErrorUtil.isObjectNull(dto.getRoleId(), "角色id");

        //查询
        String password = dto.getPassword();
        dto.setPassword(BCrypt.hashpw(password));


        checkRepeatedValue(dto.getUsername(), dto.getPhone());

        //查询角色名称，冗余到用户表
        Role role = iRoleService.getOne(new QueryWrapper<Role>().eq("uid", dto.getRoleId()));
        if (role == null) {
            throw new ServiceException(RoleCode.ROLE_NOT_EXIST);
        }

        //查询机构名称，冗余到用户表
        SanitationOffice sanitationOffice = iSanitationOfficeService.getOne(
                new QueryWrapper<SanitationOffice>()
                        .eq("uid", dto.getSanitationOfficeId())
        );

        if (sanitationOffice == null) {
            throw new ServiceException(CommonCode.PARAMS_ERROR);
        }

        User user = new User()
                .setUid(SnowflakeUtil.getUnionId())
                .setCreator(UserIdHolder.get())
                .setRoleName(role.getRoleName())
                .setRoleId(role.getUid())
                .setSanitationOfficeId(sanitationOffice.getUid())
                .setSanitationOfficeName(sanitationOffice.getName());

        BeanUtils.copyProperties(dto, user);

        boolean save = iUserService.save(user);
        if (save)
            return Result.ok();
        return Result.error(UserCode.ADD_SYSUSER_FAILURE);

    }

    @PostMapping("/update")
    @Limit("user:update")
    public Result<Object> update(UserUpdateDto dto, String publickey) {

        ErrorUtil.isObjectNull(dto.getUid(), "角色id");
        ErrorUtil.isStringEmpty(publickey, "公钥");

        //先判断是否修改了名字
        User oldUser = iUserService.getOne(
                new QueryWrapper<User>().eq("uid", dto.getUid())
        );


        List<User> listName = iUserService.list(
                new QueryWrapper<User>().eq("username", dto.getUsername())
        );

        //检查数据库中是否有同名用户
        if (oldUser.getUsername().equals(dto.getUsername())) {
            if (listName.size() > 1) {
                throw new ServiceException(UserCode.USERNAME_ALREADY_EXISTS);
            }
        } else {
            if (listName.size() > 0) {
                throw new ServiceException(UserCode.USERNAME_ALREADY_EXISTS);
            }
        }

        //检查数据库中是否有同电话用户
        List<User> listPhone = iUserService.list(
                new QueryWrapper<User>().eq("phone", dto.getPhone())
        );
        if (oldUser.getPhone().equals(dto.getPhone())) {
            if (listPhone.size() > 1) {
                throw new ServiceException(UserCode.PHONE_NUMBER_ALREADY_EXISTS);
            }
        } else {
            if (listName.size() > 0) {
                throw new ServiceException(UserCode.USERNAME_ALREADY_EXISTS);
            }
        }


        String passwordDecrypt = LoginController.getPasswordDecrypt(dto.getPassword(), publickey);

        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(BCrypt.hashpw(passwordDecrypt));
        boolean save = iUserService.update(user, new QueryWrapper<User>().eq("uid", dto.getUid()));
        if (save)
            return Result.ok();
        return Result.error(UserCode.UPDATE_SYSUSER_FAILURE);

    }

    private void checkRepeatedValue(String username, String phone) {
        //检查数据库中是否有同名用户
        List<User> listName = iUserService.list(
                new QueryWrapper<User>().eq("username", username)
        );
        if (listName.size() > 0) {
            throw new ServiceException(UserCode.USERNAME_ALREADY_EXISTS);
        }

        //检查数据库中是否有同电话用户
        List<User> listPhone = iUserService.list(
                new QueryWrapper<User>().eq("phone", phone)
        );
        if (listPhone.size() > 0) {
            throw new ServiceException(UserCode.PHONE_NUMBER_ALREADY_EXISTS);
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
        boolean save = iUserService.update(targetUser, new QueryWrapper<User>().eq("uid", targetUser.getUid()));
        if (save)
            return Result.ok();
        return Result.error(UserCode.PASSWORD_UPDATE_ERROR);

    }

    @PostMapping("/delete/{uid}")
    @Limit("user:delete")
    public Result<Object> delete(@PathVariable("uid") Long uid) {
        ErrorUtil.isObjectNull(uid, "信息id");
        User user = iUserService.getOne(new QueryWrapper<User>().eq("uid", uid));
        if (user == null) {
            throw new ServiceException(UserCode.USER_DOES_NOT_EXIST);
        }

        boolean b = iUserService.remove(new QueryWrapper<User>().eq("uid", uid));
        if (b)
            return Result.ok();
        return Result.error(UserCode.DELETE_SYSUSER_FAILURE);

    }

    @PostMapping("/disable/{uid}/{disabled}")
    @Limit("user:disable")
    public Result<Object> disable(@PathVariable("uid") Long uid, @PathVariable Integer disabled) {

        ErrorUtil.isObjectNull(uid, "角色id");
        ErrorUtil.isNumberOutOfRange(disabled, 0, 1, "禁用状态");

        QueryWrapper<User> query = new QueryWrapper<User>().eq("uid", uid);
        User user = iUserService.getOne(query);
        if (user == null) {
            log.error("禁用用户->用户id不存在");
            throw new ServiceException(UserCode.USER_DOES_NOT_EXIST);
        }
        user.setDisabled(disabled);
        user.setDisabledBy(UserIdHolder.get());
        boolean b = iUserService.update(user, query);
        if (b)
            return Result.ok();
        return Result.error(UserCode.DISABLED_FAILED);

    }

    @GetMapping("/list/permission/{uid}")
    @Limit("user:listPermission")
    public Result<Object> listPermission(@PathVariable Long uid) {

        ErrorUtil.isObjectNull(uid, "用户id");

        List<Permission> userPermissionList = iUserService.getUserPermissionList(uid);

        return Result.ok(userPermissionList);
    }

}
