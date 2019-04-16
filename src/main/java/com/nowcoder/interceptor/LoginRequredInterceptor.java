package com.nowcoder.interceptor;

import com.nowcoder.model.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginRequredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //如果user为null,说明用户没有登录
        //跳转到登录页面
        if(hostHolder.getUser() == null){
//            将当前访问的页面的路径当做参数传递过去,登陆之后就可以直接跳到这次访问的页面
            httpServletResponse.sendRedirect("/reglogin?next=" + httpServletRequest.getRequestURI());
//            System.out.println(httpServletRequest.getRequestURI());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
