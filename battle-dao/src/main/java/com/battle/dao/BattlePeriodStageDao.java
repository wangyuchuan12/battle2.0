package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.battle.domain.BattlePeriodStage;

public interface BattlePeriodStageDao extends CrudRepository<BattlePeriodStage, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattlePeriodStage findOneByBattleIdAndPeriodIdAndIndex(String battleId, String periodId, Integer stageIndex);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattlePeriodStage> findAllByPeriodIdOrderByIndexAsc(String periodId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	@Query("select id from com.battle.domain.BattlePeriodStage bpg where bpg.battleId=:battleId and bpg.periodId=:periodId")
	List<String> findAll(@Param("battleId")String battleId, @Param("periodId")String periodId);

}
