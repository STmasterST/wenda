package com.nowcoder.controller;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 用户登录注册的Controller
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    UserService userService;

    @Autowired
    LoginTicketDAO loginTicketDAO;


    //用户注册
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.POST})
    public String register(Model model,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "username")String username) {

        try {
            Map<String, String> map = userService.register(username, password);
            //如果Map中有msg信息,说明用户注册失败
            if (map.containsKey("msg")) {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

            //Map中没有msg信息,说明用户注册成功,让用户再次登录
            return "login";
        }catch(Exception e){
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }

    //用户注册html表单路径
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String index(Model model, @RequestParam(value = "next",required = false)String next) {
        model.addAttribute("next",next);
        return "login";
    }

    //用户登录
    @RequestMapping(path = {"/login/"}, method = {RequestMethod.POST})
    public String login(Model model,
                        @RequestParam(value = "password") String password,
                        @RequestParam(value = "username")String username,
                        @RequestParam(value = "next",required = false)String next,
                        @RequestParam(value = "rememberme",defaultValue = "false") boolean rememberme,
                        HttpServletResponse response) {

        try {
            Map<String, String> map = userService.login(username, password);
            //如果Map中包含ticket,则表明用户登录成功
            if (map.containsKey("ticket")) {
                //此处Cookie没有通过setmaxAge()方法设置有效期,默认就是-1,当浏览器关闭时,Cookie失效
                Cookie cookie = new Cookie("ticket",map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                //登录之后,自动跳转要访问的页面
                if(StringUtils.isNotBlank(next)){
                    return "redirect:" + next;
                }
                return "redirect:/";//用户的登录成功,重定向到首页(/index路径)
            }else{
                //登录失败,跳转到登录页面
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        }catch(Exception e){
            logger.error("登录异常" + e.getMessage());
            return "login";
        }
    }


    //用户退出登录
    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket")String ticket) {
        //用户退出登录只需要将ticket的status设置为1,使ticket失效即可
        userService.logout(ticket);
        //用户退出,返回首页
        //重定向到首页,又是一个http请求,会调用拦截器,此时ticket被置为失效
        //所以User为null,根据User的不同,渲染出来的模板不同,此时首页处的用户信息部分就变为(登录/注册)
        return "redirect:/";
    }


}
