package com.gavin.app.redis;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author: Gavin
 * @date: 2020/11/30 22:27
 * @description:
 */
public class DistributedLock {
    public static final long lockWaitTimeOut = 200;

    public static boolean lock(Jedis client, String lockKey, String lockValue, long timeout) throws InterruptedException {
        long deadLine = System.currentTimeMillis() + lockWaitTimeOut;
        while (true) {
            // fixme jedis有问题
//             boolean locked = client.set(lockKey, lockValue, timeout, TimeUnit.MILLISECONDS, false);
//             if (locked) {
////                 return true;
////             }
            if (deadLine - System.currentTimeMillis() < 0) {
                return false;
            }
            Thread.sleep(100);
        }
    }

    public static boolean unlock(Jedis client, String lockKey, String lockValue) {
        String script =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";
        //原生api
        //Object result = jedis.eval(script, Collections.singletonList(lock_key),Collections.singletonList(id));
        String sha = client.scriptLoad(script);
        List<String> keys = Collections.singletonList(lockKey);
        List<String> args = Collections.singletonList(lockValue);
        Object result = null;
        try {
            result = client.evalsha(sha, keys, args);
        } catch (Exception ex) {
            sha = client.scriptLoad(script);
            result = client.evalsha(sha, keys, args);
        }
        if ("1".equals(result.toString())) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Jedis client = new Jedis("localhost", 3396);

    }
}
