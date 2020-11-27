package com.yuntun.sanitationkitchen.interceptor;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.yuntun.sanitationkitchen.constant.JwtConstant;
import com.yuntun.sanitationkitchen.constant.SysUserConstant;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.UserCode;
import com.yuntun.sanitationkitchen.util.EptUtil;
import com.yuntun.sanitationkitchen.util.JwtHelper;
import com.yuntun.sanitationkitchen.util.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台管理系统校验登录拦截器
 *
 * @author whj
 */
@Component
public class SysLoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SysLoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //首先从请求头中获取jwt串，与页面约定好存放jwt值的请求头属性名为user-token
        String jwtToken = httpServletRequest.getHeader(JwtConstant.JWT_TOKEN_HEADER_KEY);
        log.debug("[sys登录校验拦截器]-jwtToken:{}", jwtToken);
        //判断jwt是否有效
        if (EptUtil.isEmpty(jwtToken)) {
            log.info("[sys登录校验拦截器]-未登录");
            throw new ServiceException(UserCode.NOT_LOGGED_IN);
        }

        //判断token在redis中是否过期
        if (RedisUtils.getString(SysUserConstant.USER_TOKEN_REDIS_KEY + SecureUtil.md5(jwtToken)) == null) {
            throw new ServiceException(UserCode.LOGIN_FAILED_TIME_OUT);
        }
        //校验jwt是否有效,有效则返回json信息，无效则返回空
        JSONObject retJson = JwtHelper.validateLogin(jwtToken);
        //retJSON为空则说明jwt超时或非法
        if (EptUtil.isEmpty(retJson)) {
            log.info("[sys登录校验拦截器]-JWT非法或已超时，重新登录");
            throw new ServiceException(UserCode.LOGIN_FAILED_TIME_OUT);
        }
        //校验浏览器客户端信息
        String userAgent = httpServletRequest.getHeader(JwtConstant.USER_AGENT_HEADER_KEY);
        String userAgentInJSON = retJson.getString(JwtConstant.USER_AGENT);
        if (!userAgent.equals(userAgentInJSON)) {
            log.info("[sys登录校验拦截器]-客户端浏览器信息与JWT中存的浏览器信息不一致。当前浏览器信息:{}", userAgent);
            throw new ServiceException(UserCode.USER_AGENT_EXCEPTION);
        }
        int userId = Integer.parseInt(retJson.getString(JwtConstant.USER_ID));
        //将客户Id设置到threadLocal中,方便以后使用
        UserIdHolder.set(userId);
        // 重置redis中token过期时间
        RedisUtils.expireSeconds(
                SysUserConstant.USER_TOKEN_REDIS_KEY + SecureUtil.md5(jwtToken),
                SysUserConstant.USER_TOKEN_REDIS_EXPIRE
        );
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //清除threadLocal，保证内存不会泄露
        UserIdHolder.clear();
    }
}
