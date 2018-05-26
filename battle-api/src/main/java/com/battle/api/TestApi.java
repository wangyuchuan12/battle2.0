package com.battle.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattlePk;
import com.battle.service.BattlePeriodMemberService;
import com.battle.service.BattlePkService;

@Controller
@RequestMapping(value="/api/test")
public class TestApi {
	
	@Autowired
	private BattlePeriodMemberService battlePeriodMemberService;
	
	@Autowired
	private BattlePkService battlePkService;
	
	
	@RequestMapping(value="test1")
	@ResponseBody
	public Object test1(HttpServletRequest httpServletRequest){
		String roomId = httpServletRequest.getParameter("roomId");
		String userId = httpServletRequest.getParameter("userId");
		BattlePeriodMember battlePeriodMember = battlePeriodMemberService.findOneByRoomIdAndUserIdAndIsDel(roomId, userId, 0);
		
		return battlePeriodMember;
	}
	
	@RequestMapping(value="test2")
	@ResponseBody
	public Object test2(HttpServletRequest httpServletRequest){

		String userId = httpServletRequest.getParameter("userId");
		BattlePk battlePk = battlePkService.findOneByHomeUserId(userId);
		
		return battlePk;
	}
	
	@RequestMapping(value="test3")
	@ResponseBody
	public Object test3(HttpServletRequest httpServletRequest){
		String userId = httpServletRequest.getParameter("userId");
		BattlePk battlePk = battlePkService.findOneByHomeUserId(userId);
		battlePkService.update(battlePk);
		
		return battlePk;
	}
	
}