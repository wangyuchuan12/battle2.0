package com.battle.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.battle.domain.BattleStageRestMember;

public interface BattleStageRestMemberDao extends CrudRepository<BattleStageRestMember, String>{


	BattleStageRestMember findOneByRoomIdAndMemberId(String roomId, String memberId);

	List<BattleStageRestMember> findAllByRoomId(String roomId);

	List<BattleStageRestMember> findAllByUserId(String userId);

	List<BattleStageRestMember> findAllByUserIdAndIsOnline(String userId, int i);

}
