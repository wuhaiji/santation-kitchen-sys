package com.yuntun.sanitationkitchen.auth;

import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.model.entity.Role;
import com.yuntun.sanitationkitchen.service.IRoleService;
import com.yuntun.sanitationkitchen.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限拦截器
 * </p>
 *
 * @author whj
 * @since 2020/11/9
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    public static final String SYS_USER_PERMISSION_LIST = "SYS_USER_PERMISSION_LIST_";

    private static final Logger log = LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    public static final String SYS_USER_ROLE_TYPE = "SYS_USER_ROLE_TYPE_";

    @Autowired
    IUserService iUserService;

    @Autowired
    IRoleService iRoleService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        Long userId = UserIdHolder.get();

        if (userId == null) {
            log.error("PermissionInterceptor->会话中找不到用户id");
            return false;
        }

        //超级管理员不用拦截权限
        List<Role> userRoleList = iUserService.getUserRoleList(userId);
        for (Role role : userRoleList) {
            if (role.getRoleType()) {
                return true;
            }
        }


        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            Limit limit = ((HandlerMethod) handler).getMethodAnnotation(Limit.class);
            if (limit != null) {
                List<Permission> permissionList = iUserService.getUserPermissionList(userId);
                List<String> permissionTagList = permissionList.parallelStream()
                        .map(Permission::getPermissionTag).collect(Collectors.toList());
                if (!permissionTagList.containsAll(Arrays.asList(limit.value()))) {
                    throw new ServiceException(CommonCode.PERMISSION_OPERATION);
                }

            }
        }
        return false;
    }

}
