package com.ytbdmhy.SSO_demo.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
public class RedisUtil {

    @Value("${token.expirationSeconds}")
    private int expirationSeconds;

    @Value("${token.validTime}")
    private int validTime;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 查询key，支持模糊查询
     * @param key 传过来时key的前后端已经加入了*，或者根据具体处理
     * @return
     */
    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    /**
     * 字符串获取
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 字符串存入值
     * 默认过期时间为2小时
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 7200, TimeUnit.SECONDS);
    }

    /**
     * 字符串存入值
     * @param key
     * @param value
     * @param expire 过期时间（毫秒计）
     */
    public void set(String key, String value, Integer expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 删除key
     * @param key
     */
    public void delete(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 刷新key的剩余时间
     * @param key
     * @param expire 过期时间
     */
    public void expire(String key, Integer expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * Hash中添加单个key-value
     * 默认没有过期时间
     * @param key
     * @param filed
     * @param domain
     */
    public void hset(String key, String filed, Object domain) {
        redisTemplate.opsForHash().put(key, filed, domain);
    }

    /**
     * Hash中添加单个key-value
     * @param key
     * @param filed
     * @param domain
     * @param expire 过期时间（毫秒计）
     */
    public void hset(String key, String filed, Object domain, Integer expire) {
        redisTemplate.opsForHash().put(key, filed, domain);
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 添加HashMap
     * @param key
     * @param hm 要存入的hash表
     */
    public void hset(String key, HashMap<String, Object> hm) {
        redisTemplate.opsForHash().putAll(key, hm);
    }

    /**
     * 如果key存在就不覆盖
     * @param key
     * @param filed
     * @param domain
     */
    public void hsetAbsent(String key, String filed, Object domain) {
        redisTemplate.opsForHash().putIfAbsent(key, filed, domain);
    }

    /**
     * 查询key和filed所确定的值
     * @param key 查询的key
     * @param filed 查询的filed
     * @return
     */
    public Object hget(String key, String filed) {
        return redisTemplate.opsForHash().get(key, filed);
    }

    /**
     * 查询该key下所有值
     * @param key
     * @return Map<HK, HV>
     */
    public Object hget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除key下所有值
     * @param key
     */
    public void deleteKey(String key) {
        redisTemplate.opsForHash().getOperations().delete(key);
    }

    /**
     * 判断key和filed下是否有值
     * @param key
     * @param filed
     * @return
     */
    public Boolean hasKey(String key, String filed) {
        return redisTemplate.opsForHash().hasKey(key, filed);
    }

    /**
     * 判断key下是否有值
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.opsForHash().getOperations().hasKey(key);
    }

    /**
     * 判断此token是否在黑名单中
     * @param token
     * @return
     */
    public Boolean isBlackList(String token) {
        return hasKey("blacklist", token);
    }

    /**
     * 将token加入到redis黑名单中
     * @param token
     */
    public void addBlackList(String token) {
        hset("blacklist", token, "true");
    }

    /**
     * 查询token下的刷新时间
     * @param token
     * @return HV
     */
    public Object getTokenValidTimeByToken(String token) {
        return redisTemplate.opsForHash().get(token, "tokenValidTime");
    }

    /**
     * 查询token下的刷新时间
     * @param token
     * @return HV
     */
    public Object getUsernameByToken(String token) {
        return redisTemplate.opsForHash().get(token, "username");
    }

    /**
     * 查询token下的刷新时间
     * @param username
     * @return HV
     */
    public Object getTokenByUsername(String username) {
        return redisTemplate.opsForHash().get(username, "token");
    }

    /**
     * 查询token下的刷新时间
     * @param token
     * @return HV
     */
    public Object getIPByToken(String token) {
        return redisTemplate.opsForHash().get(token, "ip");
    }

    /**
     * 查询token下的过期时间
     * @param token
     * @return HV
     */
    public Object getExpirationTimeByToken(String token) {
        return redisTemplate.opsForHash().get(token, "expirationTime");
    }

    public void setTokenRefresh(String username, String token) {
        // 刷新时间
//        Integer expire = validTime * 24 * 60 * 60 * 1000;

//        hset(token, "tokenValidTime", DateUtil.getAddDayTime(validTime));
//        hset(token, "expirationTime", DateUtil.getAddDaySecond(expirationSeconds));
        hset(username, "token", token, expirationSeconds);
//        hset(username, "ip", ip);
    }

}
