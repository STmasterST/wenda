package com.nowcoder.async;

import java.util.HashMap;
import java.util.Map;

/**
 * 事件模型
 */
public class EventModel {

    private EventType type;
    private int actorId;//触发者

    //事件类型,事件Id
    //触发的对象
    private int entityType;
    private int entityId;

    //被触发者
    private int entityOwnerId;

    //保留事件的各种信息
    private Map<String,String> exts = new HashMap<>();

    public EventModel(){}

    public EventModel(EventType type){
        this.type = type;
    }

    public EventModel setExt(String key,String value){
        exts.put(key, value);
        return this;

    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

}
