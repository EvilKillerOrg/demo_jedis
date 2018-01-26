package com.ek.guide.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 测试连接池
 * 
 * @author ek
 */
public class TestPool {
   public static void main(String[] args) {
      JedisPool  jedisPool = JedisPoolUtil.getJedisPoolInstance(); //得到连接池实例
      Jedis jedis = null;
      
      try {
         
         jedis = jedisPool.getResource(); //从连接池得到Jedis实例
         jedis.set("vv", "anan");
         
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         JedisPoolUtil.release(jedisPool, jedis); //用完了释放
      }
   }
}
