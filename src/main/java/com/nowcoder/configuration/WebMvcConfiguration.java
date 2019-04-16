package com.nowcoder.configuration;

import com.nowcoder.interceptor.LoginRequredInterceptor;
import com.nowcoder.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WebMvcConfiguration  extends WebMvcConfigurerAdapter{

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequredInterceptor loginRequredInterceptor;

    /**
     * 注册两个拦截器
     * 实现未登录跳转or实现自动登录
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        //下面的拦截器要在第一个拦截器之后调用
        //否则的话,hostHolder永远为null

        //这个拦截器有拦截要求,当访问/user/路径的时候才进行拦截
        registry.addInterceptor(loginRequredInterceptor).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
