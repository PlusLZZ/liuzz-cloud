package com.liuzz.cloud.common.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 *
 * @author liuzz
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class RedisUtil implements ApplicationContextAware {

    public static RedisTemplate redisTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        redisTemplate = applicationContext.getBean(RedisTemplate.class);
    }

    /**
     * key相关操作
     */
    public static class KeyOps {

        /**
         * 删除key
         */
        public static boolean delete(String key) {
            return redisTemplate.delete(key);
        }

        /**
         * 删除key
         */
        public static boolean delete(String... keys) {
            redisTemplate.delete(Arrays.asList(keys));
            return true;
        }

        /**
         * 删除key
         */
        public static boolean delete(Collection<String> keys) {
            redisTemplate.delete(keys);
            return true;
        }

        /**
         * 是否存在对应的key
         */
        public static boolean hasKey(String key) {
            return redisTemplate.hasKey(key);
        }

        /**
         * 给key设置过期事件
         */
        public static boolean expire(String key, long timeout, TimeUnit unit) {
            return redisTemplate.expire(key, timeout, unit);
        }

        /**
         * 匹配对应的key
         */
        public static Set<String> keys(String pattern) {
            return redisTemplate.keys(pattern);
        }

        /**
         * 移除过期时间
         */
        public static boolean persist(String key) {
            return redisTemplate.persist(key);
        }

        /**
         * 获取对应key的过期时间
         */
        public static long getExpire(String key) {
            return getExpire(key, TimeUnit.SECONDS);
        }

        /**
         * 获取对应key的过期时间
         */
        public static long getExpire(String key, TimeUnit unit) {
            return redisTemplate.getExpire(key, unit);
        }

    }

    /**
     * Object类型通用操作
     */
    public static class ObjectOps {

        /**
         * 缓存对象
         */
        public static <T> void set(String key, T value) {
            redisTemplate.opsForValue().set(key, value);
        }

        /**
         * 缓存对象
         */
        public static <T> void set(String key, T value, final Integer timeout, final TimeUnit timeUnit) {
            redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
        }

        /**
         * 获取缓存对象
         */
        public static <T> T get(String key){
            ValueOperations<String, T> stringValueOperations = redisTemplate.opsForValue();
            return stringValueOperations.get(key);
        }

        /**
         * 批量设置
         */
        public static void set(Map<String,Object> map) {
            redisTemplate.opsForValue().multiSet(map);
        }

        /**
         * 增减整数操作
         */
        public static long incrBy(String key, long increment) {
            return redisTemplate.opsForValue().increment(key, increment);
        }

    }

    /**
     * Hash类型通用操作
     */
    public static class HashOps {

        /**
         * put值
         */
        public static void hPut(String key, String entryKey, Object entryValue) {
            redisTemplate.opsForHash().put(key, entryKey, entryValue);
        }

        /**
         * put值
         */
        public static void hPutAll(String key, Map<String, Object> map) {
            redisTemplate.opsForHash().putAll(key, map);
        }

        /**
         * 获取值
         */
        public static <T> T hGet(String key, String entryKey) {
            HashOperations<String,String,T> hashOperations = redisTemplate.opsForHash();
            return hashOperations.get(key,entryKey);
        }

        /**
         * 获取值
         */
        public static <T> Map<String, T> hGetAll(String key) {
            return redisTemplate.opsForHash().entries(key);
        }

        /**
         * 删除值
         */
        public static void hDelete(String key, Object... entryKeys) {
            redisTemplate.opsForHash().delete(key, entryKeys);
        }

        /**
         * 是否存在值
         */
        public static boolean hExists(String key, String entryKey) {
            return redisTemplate.opsForHash().hasKey(key, entryKey);
        }

        /**
         * 整数增减
         */
        public static long hIncrBy(String key, Object entryKey, long increment) {
            return redisTemplate.opsForHash().increment(key, entryKey, increment);
        }

        /**
         * 获取所有hk
         */
        public static Set<String> hKeys(String key) {
            return  redisTemplate.opsForHash().keys(key);
        }

    }

    /**
     * list功能
     */
    public static class ListOps {

        /**
         * 队列头部插入
         */
        public static void lLeftPush(String key, Object data) {
            redisTemplate.opsForList().leftPush(key, data);
        }

        /**
         * 队列尾部插入
         */
        public static void lRightPush(String key, Object data) {
            redisTemplate.opsForList().rightPush(key, data);
        }

        /**
         * 保留队列长度
         */
        public static void trim(String key,long start,long end){
            redisTemplate.opsForList().trim(key,start,end);
        }

    }


}
