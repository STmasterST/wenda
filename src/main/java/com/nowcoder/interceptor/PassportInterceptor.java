package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 拦截器
 * 用户身份的验证
 * 实现的是在login_ticket的有效期内,用户不需要登录,登录首页,直接登录
 */
@Component
public class PassportInterceptor implements HandlerInterceptor{

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private HostHolder hostHolder;


    //在请求之前做拦截
    //在所有http请求之前拦截,做判断,判断用户身份
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
       String ticket = null;
       if(httpServletRequest.getCookies() != null){
           for(Cookie cookie : httpServletRequest.getCookies()){
               //找到对应的名为 ticket 的 Cookie
               if(cookie.getName().equals("ticket")){
                   ticket = cookie.getValue();//将ticket的value取出来
                   break;
               }
           }
       }

       //找到对应的ticket
       if(ticket != null){
           LoginTicket loginTicket = loginTicketDAO.selectByTicket(ticket);
           if(loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
               //ticket不合法
               return true;
           }

           User user = userDAO.selectById(loginTicket.getUserId());//通过login_ticket关联的User查询出来
           hostHolder.setUser(user);//将用户存放在ThreadLocal中,保证后台可以直接访问
       }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //在渲染之前
        //如果User为null,根据User的不同来渲染不同的页面
        //将User放在Veolcity渲染的上下文
        if(modelAndView != null && hostHolder.getUser() != null){
            modelAndView.addObject("user",hostHolder.getUser());//将User对象放在ModelAndView中
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();//结束之前清除ThreadLocal中的User,防止内存泄漏
    }
}
