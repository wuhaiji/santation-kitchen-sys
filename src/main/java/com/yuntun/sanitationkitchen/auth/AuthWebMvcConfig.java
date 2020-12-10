package com.yuntun.sanitationkitchen.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author whj
 * @since 2020/11/6
 */
@Configuration
public class AuthWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    ApiInterceptor apiInterceptor;

    @Autowired
    LoginInterceptor loginInterceptor;

    @Autowired
    PermissionInterceptor permissionInterceptor;

    public static final List<String> SYS_LOGIN_WHITE_LIST = new ArrayList<>();

    static {
        SYS_LOGIN_WHITE_LIST.add("/error/**");
        //不拦截open请求
        SYS_LOGIN_WHITE_LIST.add("/open/**");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器添加的顺序就是拦截的顺序
        registry.addInterceptor(apiInterceptor)
                .addPathPatterns("/**")
        ;

       registry.addInterceptor(loginInterceptor)
               .excludePathPatterns(SYS_LOGIN_WHITE_LIST)
               .addPathPatterns("/**")
       ;
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(SYS_LOGIN_WHITE_LIST)
                .addPathPatterns("/**")
        ;

        // registry.addInterceptor(permissionInterceptor)
        //         .excludePathPatterns(SYS_LOGIN_WHITE_LIST)
        //         .addPathPatterns("/**")
        // ;
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

}
