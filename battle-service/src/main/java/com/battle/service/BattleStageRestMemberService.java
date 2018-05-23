package com.battle.service;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.dao.BattleStageRestMemberDao;
import com.battle.domain.BattleStageRestMember;

@Service
public class BattleStageRestMemberService {

	@Autowired
	private BattleStageRestMemberDao battleStageRestMemberDao;

	public void add(BattleStageRestMember battleStageRestMember) {
		
		battleStageRestMember.setId(UUID.randomUUID().toString());
		battleStageRestMember.setUpdateAt(new DateTime());
		battleStageRestMember.setCreateAt(new DateTime());
		
		battleStageRestMemberDao.save(battleStageRestMember);
		
	}
	
	public void update(BattleStageRestMember battleStageRestMember) {
		
		battleStageRestMember.setUpdateAt(new DateTime());
		
		battleStageRestMemberDao.save(battleStageRestMember);
		
	}

	public BattleStageRestMember findOneByRoomIdAndMemberId(String roomId, String memberId) {
		
		return battleStageRestMemberDao.findOneByRoomIdAndMemberId(roomId,memberId);
		
	}

	public List<BattleStageRestMember> findAllByRoomId(String roomId) {
		
		return battleStageRestMemberDao.findAllByRoomId(roomId);
	}

	public List<BattleStageRestMember> findAllByUserId(String userId) {
		
		return battleStageRestMemberDao.findAllByUserId(userId);
	}
}
