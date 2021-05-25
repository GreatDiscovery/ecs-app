package com.gavin.app.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;
import java.util.List;

/**
 * 简单限流，利用expire
 *
 * @author: Gavin
 * @date: 2021/5/24 21:00
 * @description:
 */
public class SimpleLimiter {
    static Jedis client = new Jedis("localhost", 6379);
    /// 改脚本也可以实现
//    final String script = "local key = KEYS[1]\n" +
//            "local limit = tonumber(ARGV[1])\n" +
//            "local curentLimit = tonumber(redis.call('get', key) or \"0\")\n" +
//            "if curentLimit + 1 > limit then\n" +
//            "  return 0\n" +
//            "else\n" +
//            "  redis.call(\"INCRBY\", key, 1)\n" +
//            "  if (curentLimit == 0) then\n" +
//            "    redis.call(\"EXPIRE\", key, 60)\n" +
//            "  end \n" +
//            "  return 1\n" +
//            "end";
    final String script = "local key = KEYS[1]\n" +
            "local limit = tonumber(ARGV[1])\n" +
            "local curentLimit = redis.call(\"GET\", key)\n" +
            "\n" +
            "if curentLimit ~= false then\n" +
            "  curentLimit = tonumber(curentLimit)\n" +
            "  if curentLimit >= limit then\n" +
            "    return 0\n" +
            "  else\n" +
            "    redis.call(\"INCR\", key)\n" +
            "\treturn 1\n" +
            "  end\n" +
            "end\n" +
            "\n" +
            "local flag = redis.call(\"SETEX\", key, 60, 1)\n" +
            "return 1";
    private String sha = "";

    public void init() {
        client.del("key1");
        sha = client.scriptLoad(script);
    }

    public boolean acquire() {
        String sha = client.scriptLoad(script);
        List<String> keys = Collections.singletonList("key1");
        List<String> args = Collections.singletonList("100");
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

    public static void main(String[] args) throws InterruptedException {
        SimpleLimiter simpleLimiter = new SimpleLimiter();
        simpleLimiter.init();
        for (int i = 0; i < 500; i++) {
            System.out.println(i + "" + simpleLimiter.acquire());
        }
        Thread.sleep(60000);
        for (int i = 0; i < 500; i++) {
            System.out.println(i + "" + simpleLimiter.acquire());
        }
//        while (true) {
//            simpleLimiter.acquire();
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
