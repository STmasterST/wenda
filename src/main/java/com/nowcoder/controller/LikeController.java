package com.nowcoder.controller;


import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.LikeService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;


    //对某个评论的点赞功能
    @RequestMapping(path = {"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);//用户未登录
        }

        //实现异步化
        //当LikeController这个请求结束之后,但是EventConsumer启动之后,会执行InitlizingBean方法
        //会另外开启一个线程去执行这个事件
        eventProducer.fireEvent(new EventModel(EventType.LIKE));

        long likeCount = likeService.like(hostHolder.getUser().getId(),
                EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }


    @RequestMapping(path = {"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return WendaUtil.getJSONString(999);//用户未登录
        }

        long dislikeCount = likeService.disLike(hostHolder.getUser().getId(),
                EntityType.ENTITY_COMMENT,commentId);
        return WendaUtil.getJSONString(0,String.valueOf(dislikeCount));
    }




}
