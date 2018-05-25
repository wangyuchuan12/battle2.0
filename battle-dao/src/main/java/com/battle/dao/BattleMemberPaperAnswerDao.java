package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleMemberPaperAnswer;

public interface BattleMemberPaperAnswerDao extends CrudRepository<BattleMemberPaperAnswer, String>{


	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleMemberPaperAnswer findOneByQuestionAnswerId(String id);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleMemberPaperAnswer> findAllByBattlePeriodMemberIdAndIsSyncData(String memberId, int isSyncData);
	
	
	@Cacheable(value="userCache",keyGenerator="sessionKeyGenerator") 
	public BattleMemberPaperAnswer findOne(String id);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleMemberPaperAnswer findOneByBattlePeriodMemberIdAndStageIndex(String memberId, Integer stageIndex);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleMemberPaperAnswer> findAllByBattlePeriodMemberIdAndIsReceive(String memberId, int isReceive);

}
