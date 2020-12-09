package com.yuntun.sanitationkitchen.auth;

import com.alibaba.fastjson.JSON;
import com.yuntun.sanitationkitchen.constant.UserConstant;
import com.yuntun.sanitationkitchen.exception.ServiceException;
import com.yuntun.sanitationkitchen.model.code.ResultCode;
import com.yuntun.sanitationkitchen.model.code.code20000.UserCode;
import com.yuntun.sanitationkitchen.model.response.Result;
import com.yuntun.sanitationkitchen.util.EptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 后台管理系统校验登录拦截器
 *
 * @author whj
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);
    public static final String OPTIONS = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(OPTIONS.equals(httpServletRequest.getMethod())){
            return true;
        }
        //首先从请求头中获取jwt串，与页面约定好存放jwt值的请求头属性名为user-token
        String jwtToken = httpServletRequest.getHeader(UserConstant.TOKEN_HEADER_KEY);
        log.debug("[sys登录校验拦截器]-Token:{}", jwtToken);
        //判断jwt是否有效
        if (EptUtil.isEmpty(jwtToken)) {
            log.info("[sys登录校验拦截器]-未登录");
            returnJSON(httpServletResponse,UserCode.NOT_LOGGED_IN);
            return false;
        }
        //校验jwt是否有效,有效则返回json信息，无效则返回空
        AuthUtil.TokenInfo tokenInfo = AuthUtil.validateToken(jwtToken);
        //retJSON为空则说明jwt超时或非法
        if (tokenInfo==null) {
            log.info("[sys登录校验拦截器]-JWT非法或已超时，重新登录");
            returnJSON(httpServletResponse,UserCode.TOKEN_TIME_OUT);
            return false;
        }
        Long userId = tokenInfo.getUserId();
        //将客户Id设置到threadLocal中,方便以后使用
        UserIdHolder.set(userId);
        return true;
    }


    private void returnJSON(HttpServletResponse httpServletResponse, ResultCode resultCode) throws IOException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.error(resultCode)));
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
