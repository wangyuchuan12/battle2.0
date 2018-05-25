package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleDan;

public interface BattleDanDao extends CrudRepository<BattleDan, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleDan> findAllByPointIdOrderByLevelAsc(String pointId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleDan findOneByPointIdAndLevel(String pointId, int level);

}
