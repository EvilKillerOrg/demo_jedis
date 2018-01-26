package com.ek.guide;

import java.util.Set;

import redis.clients.jedis.Jedis;

public class TestAPI {

   public static void main(String[] args) {
      Jedis jedis = new Jedis("127.0.0.1", 6379);
      
      //取keys
     Set<String> sets =  jedis.keys("*");
     System.out.println(sets.size());
      
      // 设值String
      jedis.set("k1", "v11");
       jedis.set("k2", "v22");
       jedis.set("k3", "v33");
       
       //取值String
       System.out.println(jedis.get("k3"));
       
       
   }

}
