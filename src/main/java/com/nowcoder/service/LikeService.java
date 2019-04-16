package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * Redis实现赞踩功能
 * 可以给一个评论点赞,也可以给一个问题点赞
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    //返回点赞人数
    public long getLikeCount(int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        //如果这个用户已经点过赞了
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))){
            return 1;//这位用户已经在点赞set中
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId)) ? -1 : 0;

    }

    //点赞
    public long like(int userId,int entityType,int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));//点赞

        //从踩里面删掉
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));

        return jedisAdapter.scard(likeKey);//返回总共有多少人点赞
    }


    //踩功能实现
    public long disLike(int userId,int entityType,int entityId){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));//点赞

        //从点赞里面删掉
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey,String.valueOf(userId));

        return jedisAdapter.scard(disLikeKey);//返回总共有多少人踩
    }



}
