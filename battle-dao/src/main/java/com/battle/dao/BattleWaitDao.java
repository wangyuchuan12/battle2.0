package com.battle.dao;

import java.util.List;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleWait;

public interface BattleWaitDao extends CrudRepository<BattleWait, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleWait> findAllByBattleId(String battleId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleWait> findAllByBattleIdAndStatus(String battleId, Integer status);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Cacheable(value="userCache") 
	BattleWait findOne(String waitId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleWait> findAllByStatus(Integer status);

}
