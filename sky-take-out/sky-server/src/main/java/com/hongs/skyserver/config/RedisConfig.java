package com.hongs.skyserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongs.skycommon.json.JacksonBaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfig {

    private static final String PROJECT_PREFIX = "sky_take_out";
    private final GenericJackson2JsonRedisSerializer jacksonSerializer;

    /**
     * 构造函数：初始化序列化器
     * 在 Config 类实例化时执行一次，配置好所有的序列化规则
     */
    public RedisConfig() {
        log.info("RedisConfig: 初始化序列化器...");

        // 获取基础 ObjectMapper (复用 sky-common 中的时间格式配置)
        ObjectMapper mapper = JacksonBaseConfig.createObjectMapper();

        // 创建 GenericJackson2JsonRedisSerializer
        this.jacksonSerializer = new GenericJackson2JsonRedisSerializer(mapper);
    }

    /**
     * RedisTemplate 配置
     * 用于代码中手动操作 Redis (如: redisTemplate.opsForValue().set(...))
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        log.info("RedisConfig: 开始配置 RedisTemplate...");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Key 序列化：使用 String
        // 原因：Key 通常是字符串，使用 String 序列化后在 Redis 客户端(RDM)中可读性强，方便调试
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Value 序列化：使用 JSON (复用成员变量)
        // 原因：Value 通常是对象，需要转为 JSON 存储。复用上面的 jacksonSerializer 确保能处理日期和多态类型。
        template.setValueSerializer(this.jacksonSerializer);
        template.setHashValueSerializer(this.jacksonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * RedisCacheConfiguration 配置
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        log.info("RedisConfig: 开始配置 RedisCacheConfiguration...");

        return RedisCacheConfiguration.defaultCacheConfig()
                // Key 使用 String 序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // Value 使用 JSON 序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(this.jacksonSerializer))
                // 不缓存 null 值，防止缓存穿透/击穿时的误判
                .disableCachingNullValues()
                // 设置默认过期时间 (例如 1 小时)，防止缓存无限堆积
                .entryTtl(Duration.ofHours(1))
                // 统一前缀格式: sky_take_out:模块名:维度:
                .computePrefixWith(name -> PROJECT_PREFIX + ":" + name + ":");
    }
}
