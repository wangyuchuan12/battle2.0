package com.battle.api;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.BattleMemberRank;
import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattleRoom;
import com.battle.domain.BattleRoomReward;
import com.battle.filter.element.CurrentMemberInfoFilter;
import com.battle.service.BattleMemberRankService;
import com.battle.service.BattleRoomRewardService;
import com.battle.service.other.SyncDataService;
import com.battle.socket.service.BattleEndSocketService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
@Controller
@RequestMapping(value="/api/battle/sync")
public class BattleSyncDataApi {

	@Autowired
	private BattleRoomRewardService battleRoomRewardService;
	
	
	@Autowired
	private BattleEndSocketService battleEndSocketService;

	
	@Autowired
	private SyncDataService syncDataService;
	
	@Autowired
	private BattleMemberRankService battleMemberRankService;
	
	final static Logger logger = LoggerFactory.getLogger(BattleSyncDataApi.class);
	
	@RequestMapping(value="syncPaperData")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=CurrentMemberInfoFilter.class)
	public ResultVo syncPaperData(HttpServletRequest httpServletRequest)throws Exception{
				
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		BattlePeriodMember battlePeriodMember = sessionManager.getObject(BattlePeriodMember.class);

		Map<String, Object> data = new HashMap<>();
		
		BattleRoom battleRoom = syncDataService.syncPaperData(battlePeriodMember);
		
		BattleRoomReward battleRoomReward = battleRoomRewardService.findOneByReceiveMemberId(battlePeriodMember.getId());
	
		BattleMemberRank battleMemberRank = battleMemberRankService.findOneByMemberId(battlePeriodMember.getId());
		
		if(battleMemberRank!=null){
			data.put("rank", battleMemberRank.getRank());
		}
		
		if(battleRoomReward!=null){
			data.put("rewardBean", battleRoomReward.getRewardBean());
			data.put("rewardLove", battleRoomReward.getRewardLove());
		}else{
			data.put("rewardBean", 0);
			data.put("rewardLove", 0);
		}
		
		data.put("places", battleRoom.getPlaces());
		
		data.put("roomStatus", battleRoom.getStatus());
		data.put("status", battlePeriodMember.getStatus());
		data.put("endType", battleRoom.getEndType());
		
		data.put("roomProcess", battleRoom.getRoomProcess());
		data.put("roomScore", battleRoom.getRoomScore());
		
		data.put("process", battlePeriodMember.getProcess());
		
		data.put("score", battlePeriodMember.getScore());
		
		data.put("scoreGogal", battlePeriodMember.getScrollGogal());

		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		resultVo.setData(data);
		
		if(battleRoom.getStatus()==BattleRoom.STATUS_END){
			battleEndSocketService.endPublish(battleRoom.getId());
		}
		return resultVo;
	}
}
