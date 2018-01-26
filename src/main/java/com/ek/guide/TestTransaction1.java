package com.ek.guide;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
/**
 * 日常事务操作
 * @author ek
 */
public class TestTransaction1 {
   public static void main(String[] args) {
      Jedis jedis = new Jedis("127.0.0.1", 6379);
      // 事务分三步 开启 入队 提交
      Transaction tx = jedis.multi();
      tx.set("k4", "v4");
      tx.set("k5", "v5");
     //放弃  tx.discard();
      tx.exec();
   }
}
