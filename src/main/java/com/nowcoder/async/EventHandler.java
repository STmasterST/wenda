package com.nowcoder.async;

import java.util.List;

/**
 * 处理队列中Event
 */
public interface EventHandler {

    void doHandle(EventModel model);

    //注册要处理的EventType,当发生这些类型的Event时调用doHandle()方法进行处理
    List<EventType> getSupportEventTypes();
}
