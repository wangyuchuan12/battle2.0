package com.battle.dao;

import com.battle.domain.BattleDanReward;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

public interface BattleDanRewardDao extends CrudRepository<BattleDanReward, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleDanReward> findAllByDanId(String danId, Pageable pageable);

}
