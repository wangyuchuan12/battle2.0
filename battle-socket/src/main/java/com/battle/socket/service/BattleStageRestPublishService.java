package com.battle.socket.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.battle.domain.BattleStageRestMember;
import com.battle.service.BattleStageRestMemberService;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleStageRestPublishService {
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private BattleStageRestMemberService battleStageRestMemberService;
	
	public void stageRestMemberStatusPublish(BattleStageRestMember battleStageRestMember)throws Exception{
		
		List<BattleStageRestMember> battleStageRestMembers = battleStageRestMemberService.findAllByRoomId(battleStageRestMember.getRoomId());
	
		List<String> userIds = new ArrayList<>();
		
		for(BattleStageRestMember thisBattleStageRestMember:battleStageRestMembers){
			if(thisBattleStageRestMember.getIsOnline()==1){
				userIds.add(thisBattleStageRestMember.getUserId());
			}
		}
		
		MessageVo messageVo = new MessageVo();
		messageVo.setUserIds(userIds);
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setData(battleStageRestMember);
		messageVo.setCode(MessageVo.STAGE_REST_CODE);
		
		messageHandler.sendMessage(messageVo);
	}
}
