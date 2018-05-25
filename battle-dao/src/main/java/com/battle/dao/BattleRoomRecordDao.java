package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRoomRecord;

public interface BattleRoomRecordDao extends CrudRepository<BattleRoomRecord, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleRoomRecord> findAllByRoomId(String roomId,Pageable pageable);

}
