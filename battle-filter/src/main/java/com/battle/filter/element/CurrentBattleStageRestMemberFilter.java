package com.battle.filter.element;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.battle.domain.BattleMemberPaperAnswer;
import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattleStageRestMember;
import com.battle.service.BattleMemberPaperAnswerService;
import com.battle.service.BattleStageRestMemberService;
import com.wyc.common.filter.Filter;
import com.wyc.common.session.SessionManager;

public class CurrentBattleStageRestMemberFilter extends Filter{

	@Autowired
	private BattleStageRestMemberService battleStageRestMemberService;
	
	@Autowired
	private BattleMemberPaperAnswerService battleMemberPaperAnswerService;
	
	@Override
	public Object handlerFilter(SessionManager sessionManager) throws Exception {
		
		BattlePeriodMember battlePeriodMember = sessionManager.getObject(BattlePeriodMember.class);
		BattleStageRestMember battleStageRestMember = battleStageRestMemberService.findOneByRoomIdAndMemberId(battlePeriodMember.getRoomId(),battlePeriodMember.getId());
		
		if(battleStageRestMember==null){
			battleStageRestMember = new BattleStageRestMember();
			battleStageRestMember.setWrongCount(0);
			battleStageRestMember.setRightCount(0);
			battleStageRestMember.setThisScore(0);
			battleStageRestMember.setImgUrl(battlePeriodMember.getHeadImg());
			battleStageRestMember.setMemberId(battlePeriodMember.getId());
			battleStageRestMember.setNickname(battlePeriodMember.getNickname());
			battleStageRestMember.setRoomId(battlePeriodMember.getRoomId());
			battleStageRestMember.setStatus(BattleStageRestMember.WAIT_STATUS);
			battleStageRestMember.setUserId(battlePeriodMember.getUserId());
			battleStageRestMember.setThisProcess(0);
			battleStageRestMemberService.add(battleStageRestMember);
		}
		
		BattleMemberPaperAnswer battleMemberPaperAnswer = battleMemberPaperAnswerService.findOneByBattlePeriodMemberIdAndStageIndex(battlePeriodMember.getId(), battlePeriodMember.getStageIndex());
		
		List<BattleMemberPaperAnswer> battleMemberPaperAnswers = battleMemberPaperAnswerService.findAllByBattlePeriodMemberIdAndIsSyncData(battlePeriodMember.getId(), 0);
		
		
		System.out.println("........battleMemberPaperAnswers:"+battleMemberPaperAnswers);
		for(BattleMemberPaperAnswer battleMemberPaperAnswer2:battleMemberPaperAnswers){
			System.out.println("...........battleMemberPaperAnswer2.memberId:"+battleMemberPaperAnswer2.getBattlePeriodMemberId());
			System.out.println("...........battleMemberPaperAnswer2.stageIndex:"+battleMemberPaperAnswer2.getStartIndex());
		}
		
		
		if(battlePeriodMember.getLoveResidule()<=0){
			battleStageRestMember.setStatus(BattleStageRestMember.DEATY_STATUS);
		}
		battleStageRestMember.setIsOnline(1);
		battleStageRestMember.setStageIndex(battlePeriodMember.getStageIndex());
		battleStageRestMember.setScore(battlePeriodMember.getScore());
		battleStageRestMember.setLoveCount(battlePeriodMember.getLoveCount());
		battleStageRestMember.setLoveResidule(battlePeriodMember.getLoveResidule());
		battleStageRestMember.setProcess(battlePeriodMember.getProcess());
		
		if(battleMemberPaperAnswer!=null){
			battleStageRestMember.setThisScore(battleMemberPaperAnswer.getScore());
			battleStageRestMember.setThisProcess(battleMemberPaperAnswer.getProcess());
		}else{
			System.out.println("");
		}
		battleStageRestMemberService.update(battleStageRestMember);

		return battleStageRestMember;
	}

	@Override
	public Object handlerPre(SessionManager sessionManager) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Class<? extends Filter>> dependClasses() {
		List<Class<? extends Filter>> classes = new ArrayList<>();
		classes.add(CurrentMemberInfoFilter.class);
		return classes;
	}

	@Override
	public Object handlerAfter(SessionManager sessionManager) {
		// TODO Auto-generated method stub
		return null;
	}

}
