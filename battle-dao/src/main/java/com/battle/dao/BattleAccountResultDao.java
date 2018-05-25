package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattleAccountResult;

public interface BattleAccountResultDao extends CrudRepository<BattleAccountResult, String>{

	
	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleAccountResult findOneByUserId(String userId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	@Query(value="from com.battle.domain.BattleAccountResult bar where exists(select id from com.battle.domain.UserFriend uf where uf.friendUserId=bar.userId and uf.userId=:userId) order by bar.level desc,bar.winTime desc")
	List<BattleAccountResult> findAllByUserFrendUserId(@Param("userId")String userId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	Page<BattleAccountResult> findAll(Pageable pageable);

}
