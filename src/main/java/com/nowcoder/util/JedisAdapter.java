package com.nowcoder.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);


    private static JedisPool pool;


   static {
       pool = new JedisPool("redis://localhost:6379/10");
   }


    public long sadd(String key,String value){
        Jedis jedis = null;
        try{

            jedis = pool.getResource();
            return jedis.sadd(key,value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key,String value){
        Jedis jedis = null;
        try{

            jedis = pool.getResource();
            return jedis.srem(key,value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = null;
        try{

            jedis = pool.getResource();
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key,String value){
        Jedis jedis = null;
        try{

            jedis = pool.getResource();
            return jedis.sismember(key,value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //获取Redis一个连接
    public Jedis getJedis(){
       return pool.getResource();
    }

    public Transaction multi(Jedis jedis){
       try{
           return jedis.multi();
       }catch (Exception e){
           logger.error("发生异常" + e.getMessage());
       }
       return null;
    }

    public List<Object> exec(Transaction tx,Jedis jedis){
       try{
           return tx.exec();
       }catch (Exception e){
           logger.error("发生异常" + e.getMessage());
       }finally {
           if(tx != null){
               try {
                   tx.close();
               } catch (IOException e) {
                   logger.error("发生异常" + e.getMessage());
               }
           }
           if(jedis != null){
               jedis.close();
           }
       }
       return null;
    }

    //score表示这个value在zset中的顺序
    public long zadd(String key,double score,String value){
       Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zadd(key,score,value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    //zrange表示获取一个范围内的值
    //获取下标从start到end的值
    public Set<String> zrange(String key, int start, int end){
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            return jedis.zrange(key,start,end);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }


    //返回当前key所拥有的value
    public long zcard(String key){
        Jedis jedis = null;
        try{

            jedis = pool.getResource();
            return jedis.zcard(key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key,String member){
        Jedis jedis = null;
        try{

            jedis = pool.getResource();
            return jedis.zscore(key,member);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if(jedis != null){
                jedis.close();
            }
        }
        return null;
    }




}
