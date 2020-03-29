package com.honzooban.questionnairesystem.util;

import com.honzooban.questionnairesystem.util.vaild.CommonValidator;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName RedisUtil.java
 * @Description redis操作工具类
 * @createTime 2020年02月26日 15:21:00
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 修改某一缓存中某个字段的信息
     * @param key 缓存key
     * @param field 字段属性名
     * @param value 修改信息
     */
    public void updateCache(String key, String field, Object value){
        JSONObject object = JSONObject.fromObject(redisTemplate.opsForValue().get(key));
        object.put(field, value);
        redisTemplate.opsForValue().set(key, object);
    }

    /**
     * 修改缓存信息
     * @param key 缓存key
     * @param value 修改信息
     */
    public void updateCache(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 新增缓存
     * @param key 缓存key
     * @param value 缓存信息
     */
    public void insertCache(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 查找缓存
     * @param key 缓存key
     * @return 缓存数据
     */
    public Object selectCache(String key){
        return CommonValidator.notNull(key) ? redisTemplate.opsForValue().get(key) : null;
    }

    /**
     * 查找缓存
     * @param key 缓存key
     * @param clazz 对应的class对象
     * @return 缓存数据
     */
    public Object selectCache(String key, Class clazz){
        return CommonValidator.notNull(key) ? JSONObject.toBean(JSONObject.fromObject(redisTemplate.opsForValue().get(key)), clazz) : null;
    }
}
