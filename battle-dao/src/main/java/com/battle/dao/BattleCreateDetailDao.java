package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleCreateDetail;

public interface BattleCreateDetailDao extends CrudRepository<BattleCreateDetail, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleCreateDetail> findAllByIsDefault(int isDefault);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleCreateDetail findOneByCode(String code);

}
