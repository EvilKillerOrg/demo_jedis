package com.ek.guide;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * 事务--锁 (watch)
 * 
 * @author ek
 *
 */
public class TestTransaction2 {
   
   public static void main(String[] args) throws InterruptedException {
      TestTransaction2 ttx = new TestTransaction2();
      boolean retValue =  ttx.transMethods();
      System.out.println("main retValue ................"+retValue);
   }
   
   
   /*
    * watch命令 就是标记一个键,如果标记了一个键;
    * 在提交事务之前如果该键被别人修改过,那事务就会失败, 这种情况通常可以在程序中再重新尝试一次;
    * 首先标记了键 balance ,然后检查余额是否足够,不足就取消标记,并不做扣减;
    * 足够的话,就启动事务进行更新操作;
    * 如果在此期间键balance被其他人修改,那在提交事务(执行EXEC)时就会报错;
    * 程序中通常可以捕获这类错误再重新执行一次,直到成功.
    */
   public boolean transMethods() throws InterruptedException {
      Jedis jedis = new Jedis("127.0.0.1", 6379);
      int balance; // 可用余额 (原始额度100)
      int debt; // 欠额
      int atmToSubtract = 10; // 实刷金额 (实际中这个数字来自表单)

      jedis.watch("balance"); // 监控balance
     // Thread.sleep(7000);  //模拟网络延时7秒 , 去后台操作一下把balance 设置成小于10的数字   
      balance = Integer.parseInt(jedis.get("balance")); // 取值
      if (balance < atmToSubtract) { // 刷卡金额大于余额
         jedis.unwatch(); // 放弃监控
         System.out.println("操作失败"); // 给出提示
         return false; // 程序返回
      } else {
         System.out.println("************开始事务!");
         Transaction tx = jedis.multi(); // 开始事务
         tx.decrBy("balance", atmToSubtract); // 余额减去刷卡金额
         tx.incrBy("debt", atmToSubtract); // 欠额加上刷卡金额
         tx.exec(); // 提交事务
         System.out.println("************提交事务!");

         System.out.println("*******************余额: " + jedis.get("balance"));
         System.out.println("*******************欠款:　" + jedis.get("debt"));
         return true; // 程序结束
      }
   }
}
