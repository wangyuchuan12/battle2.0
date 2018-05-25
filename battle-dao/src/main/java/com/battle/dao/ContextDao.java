package com.battle.dao;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.Context;



public interface ContextDao extends CrudRepository<Context, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Context findOneByCode(String code);

}
