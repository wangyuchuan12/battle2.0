package com.wyc.common.repositories;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.wyc.common.wx.domain.UserInfo;

public interface WxUserInfoRepository extends CrudRepository<UserInfo, String>{
	

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
    public UserInfo findByToken(String token);

    @QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
    public UserInfo findByOpenid(String openid);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Cacheable(value="userCache")
	public UserInfo findOne(String id);
	
	public UserInfo save(UserInfo userInfo);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	public UserInfo findByOpenidAndSource(String openid, int source);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	public UserInfo findOneBySignature(String signature);

}
