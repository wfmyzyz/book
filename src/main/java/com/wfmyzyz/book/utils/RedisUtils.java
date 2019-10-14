package com.wfmyzyz.book.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private RedisTemplate redisTemplate;

	public <T> T getValue(String key, Class<T> clazz) {
		String objJsonString = this.stringRedisTemplate.opsForValue().get(key);
		if(objJsonString == null || objJsonString.trim().length() == 0) {
			return null;
		}
		return JSON.parseObject(objJsonString, clazz);
	}
	
	public String getValue(String key) {
		return this.stringRedisTemplate.opsForValue().get(key);
	}
	
	public void addToRedis(String key, Object obj) {
		String objJson = JSON.toJSON(obj).toString();
		this.addToRedis(key, objJson);
	}
	public void addToRedis(String key, Object obj, long time, TimeUnit timeUnit) {
		String objJson = JSON.toJSON(obj).toString();
		this.addToRedis(key, objJson,time,timeUnit);
	}

	private void addToRedis(String key, String value) {
		this.stringRedisTemplate.opsForValue().set(key, value, 2, TimeUnit.DAYS);
	}
	private void addToRedis(String key, String value, long userRedisTimeout, TimeUnit timeUnit) {
		this.stringRedisTemplate.opsForValue().set(key, value,userRedisTimeout, timeUnit);
	}

	public Integer getOnlineUserCount() {
		Set<String> redisKeySet = this.stringRedisTemplate.keys("*");
		String value;
		int count = 0;
		for(String key : redisKeySet) {
			value = stringRedisTemplate.opsForValue().get(key);
			if (value != null && value.length() > 0) {
				count++;
			}
		}
		return count;
	}

	public Boolean setKeyExpire(String key,long timeout, TimeUnit unit){
		return stringRedisTemplate.expire(key,timeout , unit);
	}
	
//	public boolean offLine(String key) {
//		return stringRedisTemplate.delete(key);
//	}

	public long getExpire(String key) {
		if(this.chcStringkKey(key)){
			return this.stringRedisTemplate.getExpire(key);
		}
		return 0;
	}

	public Boolean chcStringkKey(String key){
		return this.stringRedisTemplate.hasKey(key);
	}

	public Boolean deletStringeKey(String key){
		if(this.chcStringkKey(key)){
			return this.stringRedisTemplate.delete(key);
		}
		return false;
	}
	
	public Long setList(Object key, List<?> list){
		return this.redisTemplate.opsForList().leftPush(key, list);
	}
	public List<?> getList(String key){
		return (List<?>) this.redisTemplate.opsForList().leftPop(key);
	}

	public void setMap(Object key, Map<?,?> map){
		this.redisTemplate.opsForHash().putAll(key,map);
	}

	public Map<?,?> getMap(Object key){
		return this.redisTemplate.opsForHash().entries(key);
	}
	public Object getMapValue(Object key,Object key2){
		return this.redisTemplate.opsForHash().get(key,key2);
	}
	public Long deleteMapByKey(Object key,Object key2){
		return this.redisTemplate.opsForHash().delete(key,key2);
	}



}
