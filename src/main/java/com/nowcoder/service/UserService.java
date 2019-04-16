package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by nowcoder.
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }


    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }

    //用户注册
    public Map<String,String> register(String username,String password){
        Map<String,String> map = new HashMap<>();
        //判断用户名是否为空""
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        //判断用户密码是否为空
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        //判断用户名是否唯一,用户名不能已存在
        User user = userDAO.selectByName(username);
        if(user != null){//用户不为null,则用户名已被注册
            map.put("msg","用户名已经被注册");
            return map;
        }
        //用户名没有被注册过,则可以进行注册
        user = new User();
        user.setName(username);
        //为每一个注册的用户随机生成一个salt,加salt可以保证md5密码的安全性
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        //将用户密码+salt值进行md5加密,存储在数据库中
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);//向数据库添加用户
        return map;
    }


    //用户登录
    public Map<String,String> login(String username,String password){
        Map<String,String> map = new HashMap<>();
        //判断用户名是否为空""
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        //判断用户密码是否为空
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        //判断用户名是否唯一,用户名不能已存在
        User user = userDAO.selectByName(username);
        if(user == null){//用户为null,用户不存在
            map.put("msg","用户名不存在");
            return map;
        }

        //判断用户输入的密码(加salt)之后,是否与数据库中存储的密码相等
       if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
       }
       //用户登录成功,向数据库中记录一个login_ticket,用来保存用户的登录状态
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    //向数据库中添加login_ticket,用来保存用户的状态
    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime((long)(3600 * 24 * 100 * 1000) + now.getTime());//设置有效期为100天
        loginTicket.setExpired(now);//设置有效期为100天
        loginTicket.setStatus(0);//将登录状态设置为0,表示有效,登出的话将status设置为1
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));//使用UUID来生成ticket
        loginTicketDAO.addTicket(loginTicket);//向数据库中添加一个login_ticket
        return loginTicket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);//将ticket设置为无效
    }

}
