package com.battle.executer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.battle.domain.BattlePeriodStage;
import com.battle.domain.BattleSubject;
import com.battle.domain.BattleWait;
import com.battle.domain.BattleWaitUser;
import com.battle.executer.vo.BattleRoomMemberVo;
import com.battle.executer.vo.BattleRoomVo;
import com.battle.executer.vo.BattleStageVo;
import com.battle.executer.vo.BattleSubjectVo;
import com.battle.service.BattlePeriodStageService;
import com.battle.service.BattleSubjectService;
import com.battle.service.BattleWaitService;
import com.battle.service.BattleWaitUserService;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleRoomExecuterFactory {
	
	private Map<String, BattleRoomExecuter> battleRoomExecuterMap = new HashMap<>();

	@Autowired
	private BattleWaitService battleWaitService;
	
	@Autowired
	private BattleWaitUserService battleWaitUserService;
	
	@Autowired
	private BattlePeriodStageService battlePeriodStageService;
	
	@Autowired
	private BattleSubjectService battleSubjectService;
	
	@Autowired
	private MessageHandler messageHandler;
	
	public BattleRoomExecuter getBattleRoomExecuter(String id){
		
		return battleRoomExecuterMap.get(id);
		
	}
	
	public BattleRoomExecuter createFromDraw(String waitId)throws Exception{
		
		BattleWait battleWait = battleWaitService.findOne(waitId);
		
		BattleRoomVo battleRoomVo = new BattleRoomVo();
		
		List<BattleWaitUser> battleWaitUsers = battleWaitUserService.findAllByWaitIdAndStatus(waitId, BattleWaitUser.READY_STATUS);
		
		List<BattleRoomMemberVo> battleRoomMemberVos = new ArrayList<>();
		
		List<BattleStageVo> battleStageVos = new ArrayList<>();
		
		List<String> userIds = new ArrayList<>();
		for(BattleWaitUser battleWaitUser:battleWaitUsers){
			BattleRoomMemberVo battleRoomMemberVo = new BattleRoomMemberVo();
			battleRoomMemberVo.setHeadImg(battleWaitUser.getImgUrl());
			battleRoomMemberVo.setIsRoomReady(0);
			battleRoomMemberVo.setIsStageReady(0);
			battleRoomMemberVo.setLoveCount(4);
			battleRoomMemberVo.setLoveResidule(4);
			battleRoomMemberVo.setNickname(battleWaitUser.getNickname());
			battleRoomMemberVo.setProcess(0);
			battleRoomMemberVo.setScore(0);
			battleRoomMemberVo.setStatus(BattleRoomMemberVo.STATUS_FREE);
			battleRoomMemberVo.setUserId(battleWaitUser.getUserId());
			userIds.add(battleWaitUser.getUserId());
			battleRoomMemberVos.add(battleRoomMemberVo);
		}
		
		List<BattlePeriodStage> battlePeriodStages = battlePeriodStageService.findAllByPeriodIdOrderByIndexAsc(battleWait.getPeriodId());
		
		for(BattlePeriodStage battlePeriodStage:battlePeriodStages){
			BattleStageVo battleStageVo = new BattleStageVo();
			battleStageVo.setStageIndex(battlePeriodStage.getIndex());
			battleStageVos.add(battleStageVo);
		}
		
		List<BattleSubjectVo> battleSubjectVos = new ArrayList<>();
		
		Pageable pageable = new PageRequest(0, 100);
		
		
		System.out.println("..................battleId:"+battleWait.getBattleId());
		
		List<BattleSubject> battleSubjects = battleSubjectService.findAllByBattleIdAndIsDel(battleWait.getBattleId(), 0, pageable);
		
		for(BattleSubject battleSubject:battleSubjects){
			BattleSubjectVo battleSubjectVo = new BattleSubjectVo();
			battleSubjectVo.setImgUrl(battleSubject.getImgUrl());
			battleSubjectVo.setName(battleSubject.getName());
			battleSubjectVos.add(battleSubjectVo);
		}
		battleRoomVo.setBattleRoomMemberVos(battleRoomMemberVos);
		battleRoomVo.setBattleStageVos(battleStageVos);
		battleRoomVo.setBattleSubjectVos(battleSubjectVos);
		battleRoomVo.setProcessGogal(120);
		battleRoomVo.setScrollGogal(2000);
		battleRoomVo.setStageIndex(0);
		
		BattleRoomExecuter battleRoomExecuter = new BattleRoomExecuter(battleRoomVo);
		String id = UUID.randomUUID().toString();
		
		battleRoomVo.setId(id);
		battleRoomExecuter.setId(id);
		
		battleRoomExecuterMap.put(id, battleRoomExecuter);
		
		MessageVo messageVo = new MessageVo();
		messageVo.setType(MessageVo.USERS_TYPE);
		messageVo.setCode(MessageVo.WAIT_END_CODE);
		messageVo.setData(battleRoomVo);
		messageVo.setUserIds(userIds);
		messageHandler.sendMessage(messageVo);
		return battleRoomExecuter;
		
	}
}
