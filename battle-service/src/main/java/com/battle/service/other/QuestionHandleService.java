package com.battle.service.other;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleMemberPaperAnswer;
import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattlePeriodStage;
import com.battle.domain.BattleQuestion;
import com.battle.domain.BattleRoom;
import com.battle.domain.BattleSubject;
import com.battle.domain.QuestionAnswer;
import com.battle.service.BattleMemberPaperAnswerService;
import com.battle.service.BattlePeriodMemberService;
import com.battle.service.BattlePeriodStageService;
import com.battle.service.BattleQuestionService;
import com.battle.service.QuestionAnswerService;

@Service
public class QuestionHandleService {

	@Autowired
	private BattlePeriodMemberService battlePeriodMemberService;
	
	@Autowired
	private BattlePeriodStageService battlePeriodStageService;
	
	
	@Autowired
	private BattleQuestionService battleQuestionService;
	
	@Autowired
	private BattleMemberPaperAnswerService battleMemberPaperAnswerService;
	
	@Autowired
	private QuestionAnswerService questionAnswerService;

	public BattleMemberPaperAnswer createPaperAnswer(BattleRoom battleRoom,BattlePeriodMember battlePeriodMember,List<BattleSubject> battleSubjects, Integer questonCount){
		
		Integer stageIndex = battlePeriodMember.getStageIndex();
		
		if(battlePeriodMember.getStatus()==BattlePeriodMember.STATUS_IN){
			
			battlePeriodMember.setStageIndex(stageIndex+1);
			
			battlePeriodMemberService.update(battlePeriodMember);
		}else{
			
			throw new RuntimeException("用户状态不是正在进行中");
		}
		
		Pageable pageable = new PageRequest(0,questonCount);
		
		List<BattleQuestion> battleQuestions = new ArrayList<>();
		
		for(BattleSubject battleSubject:battleSubjects){
			List<BattleQuestion> battleQuestions2 = battleQuestionService.findAllByBattleIdAndBattleSubjectIdRandom(battleRoom.getBattleId(), battleSubject.getId(), pageable);
			battleQuestions.addAll(battleQuestions2);
		}
		
		
		StringBuffer stringBuffer = new StringBuffer();
		
		for(BattleQuestion battleQuestion:battleQuestions){
			stringBuffer.append(battleQuestion.getId());
			stringBuffer.append(",");
		}
		
		if(battleQuestions.size()>0){
			stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(","));
		}
		
		String questions = stringBuffer.toString();
		
		
		BattlePeriodStage battlePeriodStage = battlePeriodStageService.
				findOneByBattleIdAndPeriodIdAndIndex(battlePeriodMember.getBattleId(), 
						battlePeriodMember.getPeriodId(), stageIndex);
		

		
		Integer passCount = battlePeriodStage.getPassCount();
		
		if(passCount==null){
			passCount = 0;
		}
		
		Integer passRewardBean = battlePeriodStage.getPassRewardBean();
		
		if(passRewardBean==null){
			passRewardBean = 0;
		}
		
		String memberId = battlePeriodMember.getId();

		QuestionAnswer questionAnswer = new QuestionAnswer();
		
		questionAnswer.setQuestions(questions);
		questionAnswer.setRightSum(0);
		questionAnswer.setTargetId(memberId+"_"+stageIndex);
		questionAnswer.setType(QuestionAnswer.BATTLE_TYPE);
		questionAnswer.setWrongSum(0);
		questionAnswer.setQuestionCount(questions.length());
		questionAnswer.setQuestionIndex(0);
		
		questionAnswerService.add(questionAnswer);
		
		BattleMemberPaperAnswer battleMemberPaperAnswer = new BattleMemberPaperAnswer();
		battleMemberPaperAnswer.setAddDistance(0);
		battleMemberPaperAnswer.setBattlePeriodMemberId(memberId);
		battleMemberPaperAnswer.setRightSum(0);
		battleMemberPaperAnswer.setStageIndex(stageIndex);
		battleMemberPaperAnswer.setSubLove(0);
		battleMemberPaperAnswer.setWrongSum(0);
		battleMemberPaperAnswer.setStatus(BattleMemberPaperAnswer.FREE_STATUS);
		battleMemberPaperAnswer.setQuestionAnswerId(questionAnswer.getId());
		battleMemberPaperAnswer.setQuestionCount(questions.split(",").length);
		battleMemberPaperAnswer.setAnswerCount(0);
		battleMemberPaperAnswer.setPassCount(passCount);
		battleMemberPaperAnswer.setIsPass(0);
		battleMemberPaperAnswer.setPassRewardBean(passRewardBean);
		battleMemberPaperAnswer.setThisRewardBean(0L);
		battleMemberPaperAnswer.setIsSyncData(0);
		

		battleMemberPaperAnswer.setRightAddScore(battleRoom.getRightAddScore());
		battleMemberPaperAnswer.setWrongSubScore(battleRoom.getWrongSubScore());
		
		battleMemberPaperAnswer.setStartIndex(battlePeriodMember.getProcess());
		
		battleMemberPaperAnswer.setEndIndex(battlePeriodMember.getProcess());
		
		battleMemberPaperAnswer.setIsReceive(0);
		
		battleMemberPaperAnswer.setQuestions(questions);
		
		battleMemberPaperAnswerService.add(battleMemberPaperAnswer);
		
		return battleMemberPaperAnswer;
	}
}
