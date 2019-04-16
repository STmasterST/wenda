package com.nowcoder.controller;


import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.FollowService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 关注Controller
 * 关注用户, 关注问题
 * 两个底层调用Redis的方法是一样的,不同的是传入的参数不同
 * EntityType和EntityId不一样
 */
@Controller
public class FollowController {

    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    //关注的对象是User
    @RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String follow(@RequestParam("userId") int userId) {
        //如果当前用户没有登录
        //返回999
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }
        //关注一个用户
        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setEntityId(userId)
                .setEntityType(EntityType.ENTITY_USER).setEntityOwnerId(userId));
        return WendaUtil.getJSONString(ret ? 0 : 1, String.valueOf(followService.getFollowerCount(hostHolder.getUser().getId(), EntityType.ENTITY_USER)));
    }

    //获取自己关注了哪些人


}
