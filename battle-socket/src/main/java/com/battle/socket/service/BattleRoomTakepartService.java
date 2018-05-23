package com.battle.socket.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.domain.BattlePeriodMember;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleRoomTakepartService {
	
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
    private ScheduledExecutorService executorService;
	public void takepartPublish(final BattlePeriodMember battlePeriodMember){
		
		Thread thread = new Thread(){
			public void run() {
				MessageVo messageVo = new MessageVo();
				messageVo.setCode(MessageVo.TAKEPART_CODE);
				messageVo.setRoomId(battlePeriodMember.getRoomId());
				messageVo.setType(MessageVo.ROOM_TYPE);
				messageVo.setData(battlePeriodMember);
				
				List<String> excludeUserIds = new ArrayList<>();
				excludeUserIds.add(battlePeriodMember.getUserId());
				messageVo.setExcludeUserIds(excludeUserIds);
				
				try{
					messageHandler.sendMessage(messageVo);
				}catch(Exception e){
					
				}
			}
		};
		
		executorService.submit(thread);
		
	}
}
