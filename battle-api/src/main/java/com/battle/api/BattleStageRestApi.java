package com.battle.api;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.battle.domain.BattleMemberPaperAnswer;
import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattleRoom;
import com.battle.domain.BattleStageRestMember;
import com.battle.domain.BattleSubject;
import com.battle.filter.element.CurrentBattleStageRestMemberFilter;
import com.battle.filter.element.CurrentMemberInfoFilter;
import com.battle.service.BattleStageRestMemberService;
import com.battle.service.BattleSubjectService;
import com.battle.service.other.QuestionHandleService;
import com.battle.socket.service.BattleStageRestPublishService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;

@Controller
@RequestMapping(value="/api/battleStageRest/")
public class BattleStageRestApi {

	
	@Autowired
	private BattleStageRestMemberService battleStageRestMemberService;
	
	@Autowired
	private QuestionHandleService questionService;
	
	@Autowired
	private BattleSubjectService battleSubjectService;
	
	@Autowired
	private BattleStageRestPublishService battleStageRestPublishService;
	
	@RequestMapping(value="ready")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=CurrentBattleStageRestMemberFilter.class)
	public Object ready(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		BattleStageRestMember battleStageRestMember = sessionManager.getObject(BattleStageRestMember.class);
		
		battleStageRestMember.setStatus(BattleStageRestMember.READY_STATUS);
		
		battleStageRestMemberService.update(battleStageRestMember);

		battleStageRestPublishService.stageRestMemberStatusPublish(battleStageRestMember);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		return resultVo;
		
	}
	
	@RequestMapping(value="createPaperAnswer")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=CurrentMemberInfoFilter.class)
	public Object createPaperAnswer(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		BattlePeriodMember battlePeriodMember = sessionManager.getObject(BattlePeriodMember.class);
		
		BattleRoom battleRoom = sessionManager.getObject(BattleRoom.class);
		
		Pageable pageable = new PageRequest(0, 3);
		
		List<BattleSubject> battleSubjects = battleSubjectService.findAllByBattleIdAndIsDel(battleRoom.getBattleId(), 0, pageable);
		
		BattleMemberPaperAnswer battleMemberPaperAnswer = questionService.createPaperAnswer(battleRoom, battlePeriodMember, battleSubjects, 2);;
	
		Map<String, Object> data = new HashMap<>();
		
		data.put("subjects", battleSubjects);
		
		data.put("questions", battleMemberPaperAnswer.getQuestions().split(","));
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
	}
	
	
	
	@RequestMapping(value="members")
	@ResponseBody
	@Transactional
	public Object members(HttpServletRequest httpServletRequest){
		
		String roomId = httpServletRequest.getParameter("roomId");
		List<BattleStageRestMember> battleStageRestMembers = battleStageRestMemberService.findAllByRoomId(roomId);
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleStageRestMembers);
		return resultVo;
		
	}
}
