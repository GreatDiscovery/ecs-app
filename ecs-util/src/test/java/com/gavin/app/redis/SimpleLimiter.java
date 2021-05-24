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
    final String script = "local key = KEYS[1]\n" +
            "local limit = tonumber(ARGV[1])\n" +
            "local curentLimit = tonumber(redis.call('get', key) or \"0\")\n" +
            "if curentLimit + 1 > limit then\n" +
            "  return 0\n" +
            "else\n" +
            "  redis.call(\"INCRBY\", key, 1)\n" +
            "  if (curentLimit == 0) then\n" +
            "    redis.call(\"EXPIRE\", key, 60)\n" +
            "  end \n" +
            "  return 1\n" +
            "end";
    private String sha = "";

    public void init() {
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

    public static void main(String[] args) {
        client.del("key1");
        final SimpleLimiter simpleLimiter = new SimpleLimiter();
        simpleLimiter.init();
        while (true) {
            simpleLimiter.acquire();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
