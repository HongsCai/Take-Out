package com.hongs.skyserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate() {
        // 存储数据
        redisTemplate.opsForValue().set("address", "上海");
        // 获取数据
        Object address = redisTemplate.opsForValue().get("address");
        System.out.println(address);
        redisTemplate.delete("address");
    }
}
