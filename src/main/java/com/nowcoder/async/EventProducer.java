package com.nowcoder.async;


import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 事件的入口
 * 由EventProducer将Event保存到队列中
 * 为什么使用Redis来保存EventModel???
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    //将事件保存到队列中
    //就是保存到Redis中
    public boolean fireEvent(EventModel eventModel){
        try{
            //先将EventModel事件模型转化成JSON
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);//将事件序列化之后存储到redis中
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
