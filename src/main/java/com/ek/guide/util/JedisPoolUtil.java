package com.ek.guide.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis连接池 
 * 单例的池 
 * 双端检索的单例模式: 就是在单例上再加一层检索 保证线程不会被篡改 
 * DoubleCheckingLock
 * 实际使用的时候还要注意到并发的问题! 
 * @author ek
 */

/**
 * 注意版本不同这两个方法的名字改了
 * maxActive  ==  maxTotal
 * maxWait == maxWaitMillis
 * @author ek
 *
 */
public class JedisPoolUtil {
   private static volatile JedisPool jedisPool = null;

   private JedisPoolUtil() {
   } // 首先把构造方法私有化(就不能实例化这个类了)

   /* 对外提供获得实例的方法 */
   public static JedisPool getJedisPoolInstance() {

      if (null == jedisPool) { // 第一次判断
         synchronized (JedisPoolUtil.class) { // 加一个同步块
            if (null == jedisPool) { // 再来判断一次, 才去new
               
               JedisPoolConfig poolConfig = new JedisPoolConfig(); //配置连接池
               poolConfig.setMaxTotal(1000);  //一个pool中可以分配多少个jedis实例
               poolConfig.setMaxIdle(32);  //就是上面配置了1000个实例 ,这里的32就是说1000剩余32了就要提醒了
               poolConfig.setMaxWaitMillis(100*1000); //100秒 表示Borrow一个jedis实例时最大等待时间,超时抛JedisConnectionException
               poolConfig.setTestOnBorrow(true); //获得一个jedis实例时是否检查连接可用性
               
               jedisPool = new JedisPool(poolConfig, "127.0.0.1", 6379);
            }
         }
      }
      return jedisPool;
   }
   
   
   
   /* 释放实例的方法 (释放连接池的中的哪一个实例)*/
   public static void release(JedisPool jedisPool , Jedis jedis){
      if(null != jedis){
       //  jedisPool.returnResourceObject(jedis);  returnResourceObject 方法废弃了
         jedisPool.close();
      }
   }
}
