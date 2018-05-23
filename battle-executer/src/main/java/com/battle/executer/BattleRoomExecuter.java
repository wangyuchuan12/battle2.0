package com.battle.executer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.battle.executer.vo.BattleQuestionAnswer;
import com.battle.executer.vo.BattleQuestionVo;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.BattleSubjectVo;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

public class BattleRoomExecuter {
	
	private String id;

	private BattleRoomVo battleRoomVo;
	
	@Autowired
	private MessageHandler messageHandler;
	
	
	
	public BattleRoomVo getBattleRoomVo() {
		return battleRoomVo;
	}

	public void setBattleRoomVo(BattleRoomVo battleRoomVo) {
		this.battleRoomVo = battleRoomVo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BattleRoomExecuter(BattleRoomVo battleRoomVo) {
		this.battleRoomVo = battleRoomVo;
	}

	public void answerQuestion(String userId,BattleQuestionAnswer battleQuestionAnswer)throws Exception{
		Integer stageIndex = battleRoomVo.getStageIndex();
		
		List<BattleStageVo> battleStageVos = battleRoomVo.getBattleStageVos();
		
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomVo.getBattleRoomMemberVos();
		
		
		BattleStageVo battleStageVo = null;
		
		BattleRoomMemberVo battleRoomMemberVo = null;
		
		for(BattleStageVo battleStageVo2:battleStageVos){
			if(battleStageVo2.getStageIndex().intValue()==stageIndex.intValue()){
				battleStageVo = battleStageVo2;
			}
		}
		
		for(BattleRoomMemberVo battleRoomMemberVo2:battleRoomMemberVos){
			if(battleRoomMemberVo2.getUserId().equals(userId)){
				battleRoomMemberVo = battleRoomMemberVo2;
			}
		}
		
		List<BattleSubjectVo> battleSubjectVos = battleStageVo.getBattleSubjectVos();
		
		BattleSubjectVo battleSubjectVo = null;
		for(BattleSubjectVo battleSubjectVo2:battleSubjectVos){
			if(battleSubjectVo2.getId().equals(battleQuestionAnswer.getSubjectId())){
				battleSubjectVo = battleSubjectVo2;
			}
		}
		
		List<BattleQuestionVo> battleQuestionVos = battleSubjectVo.getBattleQuestionVos();
		
		BattleQuestionVo battleQuestionVo = null;
		
		for(BattleQuestionVo battleQuestionVo2:battleQuestionVos){
			if(battleQuestionVo2.getId().equals(battleQuestionAnswer.getQuestionId())){
				
				battleQuestionVo = battleQuestionVo2;
			}
		}
		
		Map<String, Object> data = new HashMap<>();
		if(battleQuestionVo.getAnswer().equals(battleQuestionAnswer.getAnswer())){
			data.put("isRight", true);
			Integer process = battleRoomMemberVo.getProcess();
			process++;
			battleRoomMemberVo.setProcess(process);
			data.put("process", process);
			data.put("memberId", battleRoomMemberVo.getId());
			battleRoomMemberVo.setLoveResidule(battleRoomMemberVo.getLoveResidule()+1);
			data.put("loveResidule", battleRoomMemberVo.getLoveResidule());
		}else{
			Integer process = battleRoomMemberVo.getProcess();
			data.put("isRight", false);
			data.put("process", process);
			data.put("memberId", battleRoomMemberVo.getId());
			data.put("loveResidule", battleRoomMemberVo.getLoveResidule());
		}
		
		MessageVo messageVo = new MessageVo();
		messageVo.setType(MessageVo.USERS_TYPE);
		
		List<String> userIds = new ArrayList<>();
		
		for(BattleRoomMemberVo battleRoomMemberVo2:battleRoomMemberVos){
			userIds.add(battleRoomMemberVo2.getUserId());
		}
		
		messageVo.setData(data);
		
		messageVo.setCode(MessageVo.PROGRESS_CODE);
		
		messageHandler.sendMessage(messageVo);
		
	}
	
	public void roomReady(String userId)throws Exception{
		List<BattleRoomMemberVo> battleRoomMemberVos = battleRoomVo.getBattleRoomMemberVos();
		BattleRoomMemberVo battleRoomMemberVo = null;
		
		boolean flag = false;
		for(BattleRoomMemberVo battleRoomMemberVo2:battleRoomMemberVos){
			if(battleRoomMemberVo2.getUserId().equals(userId)){
				battleRoomMemberVo = battleRoomMemberVo2;
				battleRoomMemberVo2.setIsRoomReady(1);
			}else{
				if(battleRoomMemberVo2.getIsRoomReady().intValue()!=1){
					flag = true;
				}
			}
		}
		
		if(!flag){
			Map<String, Object> data = new HashMap<>();
			
			MessageVo messageVo = new MessageVo();
			messageVo.setCode(MessageVo.ROOM_START_CODE);
			
			messageHandler.sendMessage(messageVo);
		}
		
		
		
	}
	
	public void stageReady(){
		
	}
	
	public void out(){
		
	}
}
