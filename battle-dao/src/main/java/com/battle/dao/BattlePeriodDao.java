package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattlePeriod;

public interface BattlePeriodDao extends CrudRepository<BattlePeriod, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattlePeriod findOneByBattleIdAndIndex(String battleId, Integer index);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattlePeriod> findAllByBattleIdAndStatusAndIsPublicOrderByIndexAsc(String battleId,Integer status,Integer isPublic);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattlePeriod> findAllByBattleIdOrderByIndexAsc(String battleId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattlePeriod> findAllByBattleIdAndAuthorBattleUserIdAndStatus(String battleId, String battleUserId,Integer status);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattlePeriod findOneByBattleIdAndAuthorBattleUserIdAndStatus(String battleId, String battleUserId, Integer status);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattlePeriod> findAllByBattleIdAndStatusAndAuthorBattleUserIdAndIsPublicOrderByIndexAsc(String battleId,
			Integer status, String battleUserId, int isPublic);

}
