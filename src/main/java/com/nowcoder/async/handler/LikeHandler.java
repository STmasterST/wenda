package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 当收到点赞的时候,向对方发出一个私信
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    //向用户发送一条Message消息
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);//系统向用户发送的消息
        message.setToId(model.getEntityOwnerId());//向谁发送
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());//获取触发点赞事件的用户

        message.setContent("用户" + user.getName() +"赞了你的评论");
        messageService.addMessage(message);


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);//只关注LIKE的事件(只关心点赞的事件)
    }
}
