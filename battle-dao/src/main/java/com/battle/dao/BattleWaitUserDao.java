package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleWaitUser;

public interface BattleWaitUserDao extends CrudRepository<BattleWaitUser, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleWaitUser findOneByWaitIdAndUserId(String waitId, String userId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleWaitUser> findAllByWaitId(String waitId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleWaitUser> findAllByWaitIdAndStatus(String waitId, Integer status);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleWaitUser findOneByWaitIdAndUserIdAndStatus(String waitId, String userId, Integer status);

}
