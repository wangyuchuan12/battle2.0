package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.QuestionOption;

public interface QuestionOptionDao extends CrudRepository<QuestionOption, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<QuestionOption> findAllByQuestionId(String questionId);

}
