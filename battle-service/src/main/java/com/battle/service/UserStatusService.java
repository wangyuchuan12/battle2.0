package com.battle.service;

import java.util.UUID;

import javax.persistence.LockModeType;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Lock(value=LockModeType.PESSIMISTIC_WRITE)
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
		UserStatus userStatus = (UserStatus)ehRedisCache.get("userStatusByUserId_"+userId);
		if(userStatus==null){
			return userStatusDao.findOneByUserId(userId);
		}else{
			return userStatus;
		}
	}
}
