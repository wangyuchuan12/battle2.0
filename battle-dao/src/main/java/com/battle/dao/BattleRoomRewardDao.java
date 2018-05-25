package com.battle.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleRoomReward;

public interface BattleRoomRewardDao extends CrudRepository<BattleRoomReward, String>{

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleRoomReward> findAllByRoomIdOrderByRankAsc(String roomId);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	List<BattleRoomReward> findAllByRoomIdAndIsReceiveOrderByRankAsc(String roomId, int isReceive);

	@QueryHints({@QueryHint(name ="org.hibernate.cacheable", value ="true") })
	BattleRoomReward findOneByReceiveMemberId(String receiveMemberId);

}
