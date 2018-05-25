package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleDanPoint;

public interface BattleDanPointDao extends CrudRepository<BattleDanPoint, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleDanPoint> findAllByIsRun(int isRun);

}
