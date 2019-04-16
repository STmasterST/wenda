package com.nowcoder.util;

//Redis生成Key的工具
//保证key不重复
public class RedisKeyUtil {

    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENTQUEUE";
    //粉丝
    private static String  BIZ_FOLLOWER= "FOLLOWER";
    //关注对象
    private static String  BIZ_FOLLOWEE= "FOLLOWEE";

    public static String BIZ_TIMELINE = "TIMELINE";

    public static String getLikeKey(int entityType,int entityId){
        return BIZ_LIKE  + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType,int entityId){
        return BIZ_DISLIKE  + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }


    //粉丝key
    public static String getFollowerKey(int entityType,int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT +String.valueOf(entityId);
    }

    //某一个用户关注某一类实体的key
    public static String getFolloweeKey(int userId,int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(entityType) + SPLIT +String.valueOf(entityType);
    }
}
