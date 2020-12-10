package com.yuntun.sanitationkitchen.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yuntun.sanitationkitchen.model.code.code10000.CommonCode;
import com.yuntun.sanitationkitchen.model.entity.Permission;
import com.yuntun.sanitationkitchen.model.entity.Role;
import com.yuntun.sanitationkitchen.model.entity.User;
import com.yuntun.sanitationkitchen.service.IRoleService;
import com.yuntun.sanitationkitchen.service.IUserService;
import com.yuntun.sanitationkitchen.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            return true;
        }
        Long userId = UserIdHolder.get();

        if (userId == null) {
            log.error("PermissionInterceptor->会话中找不到用户id");
            return false;
        }

        //超级管理员不用拦截权限

        try {
            User user = iUserService.getOne(new QueryWrapper<User>().eq("uid", userId));
            if (user != null) {
                Role role = iRoleService.getOne(new QueryWrapper<Role>().eq("uid", user.getRoleId()));
                if (role != null && role.getRoleType()) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("Exception:", e);
            ServletUtil.returnJSON(httpServletResponse, CommonCode.PERMISSION_ERROR);
        }


        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            Limit limit = ((HandlerMethod) handler).getMethodAnnotation(Limit.class);
            if (limit != null) {
                List<Permission> permissionList = new ArrayList<>();
                String[] value = limit.value();
                try {
                    permissionList = iUserService.getUserPermissionList(userId);
                } catch (Exception e) {
                    log.error("Exception:", e);
                    ServletUtil.returnJSON(httpServletResponse, CommonCode.PERMISSION_ERROR);
                }

                List<String> permissionTagList = permissionList
                        .parallelStream()
                        .map(Permission::getPermissionTag)
                        .collect(Collectors.toList());
                boolean b = permissionTagList.containsAll(Arrays.asList(value));
                if (!b) {
                    ServletUtil.returnJSON(httpServletResponse, CommonCode.PERMISSION_OPERATION);
                }
                return true;

            } else {
                //没有添加@Limit注解就表示该接口不用权限限制
                return true;
            }

        }
        return false;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

    }
}
