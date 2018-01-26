package com.ek.guide;

import redis.clients.jedis.Jedis;

/**
 * 测试主从复制
 * 测试的时候注意了  内存数据库太快了 这里一执行有可能返回的时null 但是redis里已经有了, 稍等一下再执行一次
 * 
 * @author ek
 *
 */
public class TestMS {
   public static void main(String[] args) {
      Jedis jedis_M = new Jedis("127.0.0.1", 6379);
      Jedis jedis_S = new Jedis("127.0.0.1", 6380);
      //配置主从关系,配从不配主
      jedis_S.slaveof("127.0.0.1", 6379); //这个一般时不会在程序里面配置的
      
      //读写分离 Master写,slave读 , 这个读写分离的操作时在程序里面要来操作的
      
      jedis_M.set("k1", "v1");
      
      jedis_S.get("k1");
      
   }

}
