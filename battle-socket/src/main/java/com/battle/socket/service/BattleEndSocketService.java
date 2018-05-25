package com.battle.socket.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleMemberRank;
import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattleRoom;
import com.battle.domain.BattleRoomReward;
import com.battle.service.BattleMemberRankService;
import com.battle.service.BattlePeriodMemberService;
import com.battle.service.BattleRoomRewardService;
import com.battle.service.BattleRoomService;
import com.battle.socket.BattleEndRankVo;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleEndSocketService {

	@Autowired
	private BattlePeriodMemberService battlePeriodMemberService;
	@Autowired
	private MessageHandler messageHandler;
	
	@Autowired
	private BattleRoomService battleRoomService;
	
	@Autowired
	private BattleRoomRewardService battleRoomRewardService;
	
	@Autowired
	private BattleMemberRankService battleMemberRankService;
	
	@Autowired
    private ScheduledExecutorService executorService;
	public void endPublish(final String roomId) throws IOException{
		
		
		Thread thread = new Thread(){
			@Override
			public void run() {
				Sort sort = new Sort(Direction.DESC,"score");
				Pageable pageable = new PageRequest(0, 100, sort);
				BattleRoom battleRoom = battleRoomService.findOne(roomId);
				List<BattlePeriodMember> battlePeriodMembers = battlePeriodMemberService.
						findAllByBattleIdAndPeriodIdAndRoomId(battleRoom.getBattleId(), battleRoom.getPeriodId(), battleRoom.getId(), pageable);
				
				if(battlePeriodMembers==null||battlePeriodMembers.size()==0){
					return;
				}
				MessageVo messageVo = new MessageVo();
				messageVo.setCode(MessageVo.BATTLE_END_CODE);
				messageVo.setRoomId(battleRoom.getId());
				messageVo.setType(MessageVo.ROOM_TYPE);
				
				List<BattleEndRankVo> battleEndRankVos = new ArrayList<>();
				
				List<BattleRoomReward> battleRoomRewards = battleRoomRewardService.findAllByRoomIdOrderByRankAsc(roomId);
				
				List<BattleMemberRank> battleMemberRanks = battleMemberRankService.findAllByRoomId(roomId);
				
				Map<String, BattleRoomReward> battleRoomRewardMap = new HashMap<>();
				Map<String, BattleMemberRank> battleMemberRankMap = new HashMap<>();
				
				for(BattleRoomReward battleRoomReward:battleRoomRewards){
					battleRoomRewardMap.put(battleRoomReward.getReceiveMemberId(), battleRoomReward);
				}
				
				for(BattleMemberRank battleMemberRank:battleMemberRanks){
					battleMemberRankMap.put(battleMemberRank.getMemberId(), battleMemberRank);	
				}
				for(int i = 0;i<battlePeriodMembers.size();i++){
					BattlePeriodMember battlePeriodMember = battlePeriodMembers.get(i);
					BattleEndRankVo battleEndVo = new BattleEndRankVo();
					battleEndVo.setHeadImg(battlePeriodMember.getHeadImg());
					battleEndVo.setMemberId(battlePeriodMember.getId());
					battleEndVo.setNickname(battlePeriodMember.getNickname());
					battleEndVo.setProcess(battlePeriodMember.getProcess());
					battleEndVo.setRank(i+1);
					battleEndVo.setRoomId(battlePeriodMember.getRoomId());
					battleEndVo.setScore(battlePeriodMember.getScore());
					battleEndVo.setPlaces(battleRoom.getPlaces());
					BattleRoomReward battleRoomReward = battleRoomRewardMap.get(battlePeriodMember.getId());
					
					BattleMemberRank battleMemberRank = battleMemberRankMap.get(battlePeriodMember.getId());
					
					if(battleMemberRank!=null){
						battleEndVo.setRank(battleMemberRank.getRank());
					}
					
					if(battleRoomReward!=null){
						battleEndVo.setRewardBean(battleRoomReward.getRewardBean());
						battleEndVo.setRewardLove(battleRoomReward.getRewardLove());
					}else{
						battleEndVo.setRewardBean(0);
						battleEndVo.setRewardLove(0);
					}
					battleEndRankVos.add(battleEndVo);
				}
				messageVo.setData(battleEndRankVos);
				
				try {
					messageHandler.sendMessage(messageVo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.run();
			}
		};
		
		executorService.schedule(thread, 5, TimeUnit.SECONDS);
		
	}
}
