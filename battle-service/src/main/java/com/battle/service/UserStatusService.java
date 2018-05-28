package com.battle.service;

import java.util.UUID;

import javax.persistence.LockModeType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import com.battle.dao.UserStatusDao;
import com.battle.domain.UserStatus;
import com.wyc.common.session.EhRedisCache;

@Service
public class UserStatusService {

	@Autowired
	private UserStatusDao userStatusDao;

	@Autowired
	private SimpleCacheManager ehRedisCacheManager;

	@Cacheable(value="userCache",key="#p0")
	public UserStatus findOne(String id) {
		
		return userStatusDao.findOne(id);
	}


	public void add(UserStatus userStatus) {
		
		userStatus.setId(UUID.randomUUID().toString());
		
		userStatus.setCreateAt(new DateTime());
		userStatus.setUpdateAt(new DateTime());
		
		userStatusDao.save(userStatus);
		
		EhRedisCache ehRedisCache = (EhRedisCache) ehRedisCacheManager.getCache("userCache");
		ehRedisCache.put(userStatus.getId(), userStatus);
		ehRedisCache.put("userStatusByUserId_"+userStatus.getUserId(), userStatus);
		
	}


	public void update(UserStatus userStatus) {
		userStatus.setUpdateAt(new DateTime());
		userStatusDao.save(userStatus);
		
		EhRedisCache ehRedisCache = (EhRedisCache) ehRedisCacheManager.getCache("userCache");
		ehRedisCache.put(userStatus.getId(), userStatus);
		
		ehRedisCache.put("userStatusByUserId_"+userStatus.getUserId(), userStatus);
		
	}


	
	public UserStatus findOneByUserId(String userId) {
		
		EhRedisCache ehRedisCache = (EhRedisCache) ehRedisCacheManager.getCache("userCache");
		ValueWrapper valueWrapper = ehRedisCache.get("userStatusByUserId_"+userId);
		UserStatus userStatus = null;
		
		if(valueWrapper!=null){
			userStatus = (UserStatus)valueWrapper.get();
		}
		if(userStatus==null){
			userStatus = userStatusDao.findOneByUserId(userId);
		}else{
			
		}
		
		if(userStatus!=null){
			ehRedisCache.put("userStatusByUserId_"+userStatus.getUserId(), userStatus);
		}
		
		return userStatus;
	}
}
