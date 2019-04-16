package com.nowcoder.service;

import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 实现关注功能
     * 关注需要做俩件事情(1)将关注对象放在关注列表
     * (2)将自己放在关注的实体的粉丝列表中
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean follow(int userId,int entityType,int entityId){
        //粉丝key
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        //关注的对象
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        //开启一个事务
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followerKey,date.getTime(), String.valueOf(userId));
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2 && (long) ret.get(0) > 0 && (long)ret.get(1) > 0;
    }

    //取消关注
    public boolean unfollow(int userId,int entityType,int entityId){
        //粉丝key
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        //关注的对象
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        //开启一个事务
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey, String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2 && (long) ret.get(0) > 0 && (long)ret.get(1) > 0;
    }

    //获取所有粉丝
    public List<Integer> getFollowers(int entityType, int entityId, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey,0,count));
    }

    //将获取到的粉丝的id转换成int型
    private List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for(String str : idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    public Set<String> getFollowers(int entityType, int entityId,int offset, int count){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zrange(followerKey,offset,count);
    }


    //获取自己的关注者
    public List<Integer> getFollowees(int entityType, int entityId, int count){
        String followerKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followerKey,0,count));
    }

    public List<Integer> getFollowees(int entityType, int entityId, int offset, int count){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrange(followeeKey,offset,count));
    }


    //获取自己有多少个粉丝
    public long getFollowerCount(int entityId,int entityType){
        String followerKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }


    //判断这个用户是不是这个实体的粉丝
    //如果这个用户存在于Redis中, 则返回的分值不为null
    public boolean isFollower(int userId,int entityType,int entityId ){
        String followerKey = RedisKeyUtil.getFolloweeKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey,String.valueOf(userId)) != null;
    }


}
