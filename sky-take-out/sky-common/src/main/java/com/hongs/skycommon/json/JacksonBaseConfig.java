package com.hongs.skycommon.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.hongs.skycommon.constant.DefaultConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 基础 ObjectMapper
 * 用途：
 * - MVC JSON
 * - Feign
 * - 日志
 * - 作为 Redis / MQ ObjectMapper 的“母本”
 */
@Configuration
public class JacksonBaseConfig {
    /**
     * 1. 提供一个公共的静态工厂方法，供外部（如 Util 类）直接获取配置好的 ObjectMapper
     * 避免了 Util 类无法注入 Bean 的问题，同时保证配置一致性。
     */
    public static ObjectMapper createObjectMapper() {

        ObjectMapper mapper = JsonMapper.builder()
                // Java 8 时间支持（Instant / LocalDateTime 等）
                .addModule(new JavaTimeModule())
                // 时间不使用时间戳
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 忽略未知字段
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();

        // 创建一个模块，用于注册自定义的序列化器和反序列化器
        // ------------------ 反序列化配置 (Input: JSON字符串 -> Java对象) ------------------
        // 当前端传来 "2025-12-01 12:00:00" 时，自动解析为 LocalDateTime 对象
        // ------------------ 序列化配置 (Output: Java对象 -> JSON字符串) ------------------
        // 当后端返回 LocalDateTime 对象时，自动转为 "yyyy-MM-dd HH:mm:ss" 格式的字符串
        SimpleModule timeModule = new SimpleModule()
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(
                        DateTimeFormatter.ofPattern(DefaultConstant.DEFAULT_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(
                        DateTimeFormatter.ofPattern(DefaultConstant.DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(
                        DateTimeFormatter.ofPattern(DefaultConstant.DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(
                        DateTimeFormatter.ofPattern(DefaultConstant.DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(
                        DateTimeFormatter.ofPattern(DefaultConstant.DEFAULT_TIME_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(
                        DateTimeFormatter.ofPattern(DefaultConstant.DEFAULT_TIME_FORMAT)));
        mapper.registerModule(timeModule);
        return mapper;
    }

    /**
     * 2. Spring 容器内的 Bean，直接调用上面的静态方法
     * 用途：MVC 全局转换、Feign 调用等
     */
    @Bean
    public ObjectMapper objectMapper() {
        return createObjectMapper();
    }
}
