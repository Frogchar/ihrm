package com.ihrm.system;

import com.ihrm.common.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @author songc
 * @version 1.0
 * @date 2020/6/19 13:34
 * @description 拦截类
 * @Email songchao_ss@163.com
 */
//@Configuration
public class SystemConfig extends WebMvcConfigurationSupport {

    @Resource
    JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                /**
                 *  哪些需要拦截
                 */
                .addPathPatterns("/**")
                /**
                 * 哪些不需要拦截
                 */
                .excludePathPatterns("/sys/login","/sys/register/**");

    }
}
