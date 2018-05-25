package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleMemberQuestionAnswer;

public interface BattleMemberQuestionAnswerDao extends CrudRepository<BattleMemberQuestionAnswer, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleMemberQuestionAnswer> findAllByBattleMemberPaperAnswerId(String battleMemberPaperAnswerId);

}
