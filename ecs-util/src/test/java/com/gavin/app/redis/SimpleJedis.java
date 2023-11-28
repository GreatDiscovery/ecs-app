package com.gavin.app.redis;

import redis.clients.jedis.Jedis;

public class SimpleJedis {
    public static void main(String[] args) {
        Jedis client = new Jedis("192.168.143.120", 12345);
        String result = client.get("k1");
        System.out.println(result);
    }
}
