package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.controller.HomeController;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来处理队列中的Event
 * Consumer是将队列中所有Event分发给不同的Handler
 *
 * EventConsumer是一个异步的线程, 也可以使用线程池来实现
 * 这是一个多线程的应用
 *
 * 做成异步的线程,即使处理Event的部分挂了,也不会影响其他正常的业务
 * 如果是做成同步的, 当EventConsumer挂了就会报错, 就是将不同的业务独立开来
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware{

    //当从队列中取出Event时,根据EventType的不同,找到能处理的一匹EventHandler,让这些Handler一个个进行处理
    private Map<EventType,List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);




    @Override
    public void afterPropertiesSet() throws Exception {
        //找出所有的EventHandler实现类,处理事件的Handler
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if(beans != null){
            for(Map.Entry<String,EventHandler> entry : beans.entrySet()){
                //获取这些Handler所支持的EventTypes
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                //遍历一个Handler所支持的EventType
                for(EventType type : eventTypes){
                    //将这些Handler所支持的EventType添加进去
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    //将一个EventType被支持的Handler添加进Map集合
                    config.get(type).add(entry.getValue());
                }
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    //从list对象中取出并移除最后一个元素
                    //将timeout设置为0,表示如果获取不到元素,线程就一直阻塞在这个地方
                    //将timeout设置为一个整数,超出这个时间获取不到元素就会自动返回
                    List<String> events = jedisAdapter.brpop(0,key);
                    for(String message : events){
                        //第一次返回键值会返回相应的key值
                        if(message.equals(key)){
                            continue;
                        }

                        //将EventModel对象反序列化
                       EventModel eventModel = JSON.parseObject(message,EventModel.class);
                       if(!config.containsKey(eventModel.getType())){
                           logger.error("不能识别的事件");
                            continue;
                       }

                       //将一个EventType的所有Handler进行执行这个EventModel
                       for(EventHandler handler : config.get(eventModel.getType())){
                           handler.doHandle(eventModel);
                       }

                    }
                }
            }
        });
        thread.start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
