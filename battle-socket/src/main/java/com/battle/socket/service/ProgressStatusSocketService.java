package com.battle.socket.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleMemberPaperAnswer;
import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattleRoom;
import com.battle.domain.QuestionAnswerItem;
import com.battle.service.BattlePeriodMemberService;
import com.battle.service.BattleRoomService;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;
import com.battle.socket.vo.ProgressStatusVo;

@Service
public class ProgressStatusSocketService {

	@Autowired
	private BattlePeriodMemberService battlePeriodMemberService;
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private BattleRoomService battleRoomService;
	
	@Autowired
	private ScheduledExecutorService executorService;
	public void statusPublish(
			final String roomId,
			final BattlePeriodMember battlePeriodMember,
			final BattleMemberPaperAnswer battleMemberPaperAnswer,
			final QuestionAnswerItem questionAnswerItem,
			final Integer loveDiff){
		
		Thread thread = new Thread(){
			public void run() {
				List<ProgressStatusVo> progressStatusVos = new ArrayList<>();
				
				BattleRoom battleroom = battleRoomService.findOne(roomId);
				
				ProgressStatusVo progressStatusVo = new ProgressStatusVo();
				progressStatusVo.setLoveCount(battlePeriodMember.getLoveResidule());
				progressStatusVo.setMemberId(battleMemberPaperAnswer.getBattlePeriodMemberId());
				progressStatusVo.setProcess(battlePeriodMember.getProcess());
				progressStatusVo.setScore(battlePeriodMember.getScore());
				progressStatusVo.setStatus(battlePeriodMember.getStatus());
				progressStatusVo.setThisProcess(battleMemberPaperAnswer.getProcess());
				progressStatusVo.setRoomStatus(battleroom.getStatus());
				progressStatusVo.setStageIndex(battlePeriodMember.getStageIndex());
				progressStatusVo.setLoveDiff(loveDiff);
				progressStatusVo.setIsRight(questionAnswerItem.getIsRight());
				progressStatusVos.add(progressStatusVo);
				MessageVo messageVo = new MessageVo();
				
				messageVo.setCode(MessageVo.PROGRESS_CODE);
				messageVo.setType(MessageVo.ROOM_TYPE);
				messageVo.setRoomId(roomId);
				messageVo.setData(progressStatusVos);
				//messageVo.setExcludeUserIds(Arrays.asList(excludeIds));
				
				try {
					messageHandler.sendMessage(messageVo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		executorService.schedule(thread, 500,TimeUnit.MILLISECONDS);
	
	}
	
	public void statusPublish(final String roomId,final String ...excludeIds){
		
		
		Thread thread = new Thread(){
			public void run() {
				BattleRoom battleRoom = battleRoomService.findOne(roomId);
				List<BattlePeriodMember> battlePeriodMembers = battlePeriodMemberService.findAllByBattleIdAndPeriodIdAndRoomId(battleRoom.getBattleId(), battleRoom.getPeriodId(), battleRoom.getId());
				List<ProgressStatusVo> progressStatusVos = new ArrayList<>();
				
				for(BattlePeriodMember battlePeriodMember:battlePeriodMembers){
					
					System.out.println(".........loveResidule:"+battlePeriodMember.getLoveResidule());
					ProgressStatusVo progressStatusVo = new ProgressStatusVo();
					progressStatusVo.setLoveCount(battlePeriodMember.getLoveResidule());
					progressStatusVo.setMemberId(battlePeriodMember.getId());
					progressStatusVo.setProcess(battlePeriodMember.getProcess());
					progressStatusVo.setScore(battlePeriodMember.getScore());
					progressStatusVo.setStatus(battlePeriodMember.getStatus());
					progressStatusVo.setRoomStatus(battleRoom.getStatus());
					progressStatusVos.add(progressStatusVo);
				}
				
				MessageVo messageVo = new MessageVo();
				
				messageVo.setCode(MessageVo.PROGRESS_CODE);
				messageVo.setType(MessageVo.ROOM_TYPE);
				messageVo.setRoomId(roomId);
				messageVo.setData(progressStatusVos);
				messageVo.setExcludeUserIds(Arrays.asList(excludeIds));
				try{
					messageHandler.sendMessage(messageVo);
				}catch(Exception e){
					
				}
				
			}
		};
		
		executorService.submit(thread);
	}
}