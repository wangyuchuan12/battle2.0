package com.wyc.handle;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.battle.domain.Battle;
import com.battle.domain.BattleCreateDetail;
import com.battle.domain.BattlePk;
import com.battle.domain.BattleRoom;
import com.battle.service.BattleCreateDetailService;
import com.battle.service.BattlePkService;
import com.battle.service.BattleRoomService;
import com.battle.service.BattleService;
import com.battle.service.other.BattleRoomHandleService;
import com.battle.socket.service.BattleEndSocketService;
import com.battle.socket.service.PkSocketService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;
import com.wyc.handle.InitRoomService;

@Service
public class BattlePkHandleService {
	
	@Autowired
	private BattlePkService battlePkService;
	
	@Autowired
	private InitRoomService initRoomService;
	
	@Autowired
	private PkSocketService pkSocketService;
	
	@Autowired
	private BattleEndSocketService battleEndSocketService;
	
	@Autowired
	private BattleCreateDetailService battleCreateDetailServie;
	
	@Autowired
	private BattleRoomService battleRoomService;
	
	@Autowired
	private BattleService battleService;
	
	final static Logger logger = LoggerFactory.getLogger(BattlePkHandleService.class);
	
	@Autowired
	private BattleRoomHandleService battleRoomHandleService;
	
	
	public BattlePk homeInto(UserInfo userInfo){
		BattlePk battlePk = battlePkService.findOneByHomeUserId(userInfo.getId());
		

		if(battlePk==null){
			battlePk = new BattlePk();
			battlePk.setHomeUserId(userInfo.getId());
			
			battlePk.setHomeUserImgurl(userInfo.getHeadimgurl());
			
			battlePk.setHomeUsername(userInfo.getNickname());
			
			battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
			
			battlePk.setBattleCount(0);
			
			battlePk.setBeatStatus(BattlePk.STATUS_LEAVE);
			
			battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
			
			battlePk.setHomeActiveTime(new DateTime());
			
			battlePk.setBeatActiveTime(new DateTime());
			
			battlePkService.add(battlePk);
			
		}
		
		
		if(battlePk.getRoomStatus().intValue()==BattlePk.ROOM_STATUS_BATTLE){
			BattleRoom battleRoom = battleRoomService.findOne(battlePk.getRoomId());
			if(battleRoom.getStatus().intValue()==BattleRoom.STATUS_END){
				battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
				battlePk.setBeatStatus(BattlePk.STATUS_LEAVE);
				battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
				battlePk.setRoomId("");
				battlePkService.update(battlePk);
			}
		}
		
		if(battlePk.getRoomStatus()==BattlePk.ROOM_STATUS_FREE||battlePk.getRoomStatus()==BattlePk.ROOM_STATUS_END){
			String roomId = battlePk.getRoomId();
			BattleRoom battleRoom = null;
			if(CommonUtil.isNotEmpty(roomId)){
				battleRoom  = battleRoomService.findOne(roomId);
			}
			
			if(battleRoom==null||battleRoom.getStatus()!=BattleRoom.STATUS_FREE){
				List<BattleCreateDetail> battleCreateDetails = battleCreateDetailServie.findAllByIsDefault(1);
				
				if(battleCreateDetails==null||battleCreateDetails.size()==0){
					throw  new RuntimeException("battleCreateDetail为空");
				}
				
				BattleCreateDetail battleCreateDetail = battleCreateDetails.get(0);
				Battle battle = battleService.findOne(battleCreateDetail.getBattleId());
				
				battleRoom = battleRoomHandleService.initRoom(battle);
				battleRoom.setIsPk(1);
				battleRoom.setPeriodId(battleCreateDetail.getPeriodId());
				battleRoom.setMaxinum(2);
				battleRoom.setMininum(2);
				battleRoom.setIsSearchAble(0);
				battleRoom.setScrollGogal(battleCreateDetail.getScrollGogal());
				battleRoom.setPlaces(1);
				battleRoom.setIsDanRoom(0);
				
				battleRoom.setIsIncrease(1);
				
				battleRoom.setStartTime(new DateTime());
				
				initRoomService.addRoom(battleRoom);
				
				battlePk.setRoomId(battleRoom.getId());
				battlePk.setBattleId(battle.getId());
				
				battlePk.setPeriodId(battleCreateDetail.getPeriodId());
				
				battlePk.setHomeActiveTime(new DateTime());
				
				battlePk.setRoomStatus(BattlePk.ROOM_STATUS_CALL);
				
				battlePkService.update(battlePk);
			}
		}
		
		return battlePk;
	}
	
	public BattlePk beatInto(String id,UserInfo userInfo){
		BattlePk battlePk = battlePkService.findOne(id);
		
		if(battlePk.getHomeUserId().equals(userInfo.getId())){
			return homeInto(userInfo);
		}
		
		if(CommonUtil.isNotEmpty(battlePk.getRoomId())){
			BattleRoom battleRoom = battleRoomService.findOne(battlePk.getRoomId());
			Integer status = battleRoom.getStatus();
			if(status==BattleRoom.STATUS_END){
				battlePk.setBeatStatus(BattlePk.STATUS_INSIDE);
				battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
				battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
				battlePk.setRoomId("");
				battlePkService.update(battlePk);
			}
		}
		
		if(battlePk.getRoomStatus()==BattlePk.ROOM_STATUS_CALL||battlePk.getRoomStatus()==BattlePk.ROOM_STATUS_FREE){
			
			Integer beatStatus = battlePk.getBeatStatus();
			
			DateTime beatActiveTime = battlePk.getBeatActiveTime();
			
			DateTime nowDatetime = new DateTime();
			
			Long differ = nowDatetime.getMillis()-beatActiveTime.getMillis();
			
			if(beatStatus==BattlePk.STATUS_LEAVE||(beatStatus==BattlePk.STATUS_INSIDE&&differ>50000)||(beatStatus==BattlePk.STATUS_READY&&differ>5000000)){
		
				battlePk.setBeatUserId(userInfo.getId());
				
				battlePk.setBeatUserImgurl(userInfo.getHeadimgurl());
				
				battlePk.setBeatUsername(userInfo.getNickname());
				
				battlePk.setBeatStatus(BattlePk.STATUS_INSIDE);
				
				battlePk.setBeatActiveTime(new DateTime());
				
				
				battlePkService.update(battlePk);
			}
		}
		
		
		if(battlePk.getRoomStatus().intValue()==BattlePk.ROOM_STATUS_BATTLE){
			BattleRoom battleRoom = battleRoomService.findOne(battlePk.getRoomId());
			if(battleRoom.getStatus().intValue()==BattleRoom.STATUS_END){
				battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
				battlePk.setBeatStatus(BattlePk.STATUS_INSIDE);
				battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
				battlePk.setRoomId("");
				battlePkService.update(battlePk);
			}
		}
		
		
		
		return battlePk;
	}
	
	public BattlePk restart(UserInfo userInfo) throws IOException{
		
		BattlePk battlePk = battlePkService.findOneByHomeUserId(userInfo.getId());

		battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
		
		
		battlePk.setBeatStatus(BattlePk.STATUS_LEAVE);
		
		battlePk.setBeatUserImgurl("");
		battlePk.setBeatUsername("");
		
		battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
		
		List<BattleCreateDetail> battleCreateDetails = battleCreateDetailServie.findAllByIsDefault(1);
		
		if(battleCreateDetails==null||battleCreateDetails.size()==0){
			throw new RuntimeException("battleCreateDetail不能为空");
		}
		
		BattleRoom oldBattleRoom = battleRoomService.findOne(battlePk.getRoomId());
		
		oldBattleRoom.setStatus(BattleRoom.STATUS_END);
		
		oldBattleRoom.setEndType(BattleRoom.FORCE_END_TYPE);
		
		battleRoomService.update(oldBattleRoom);
		
		BattleCreateDetail battleCreateDetail = battleCreateDetails.get(0);
		Battle battle = battleService.findOne(battleCreateDetail.getBattleId());
		
		final BattleRoom battleRoom = battleRoomHandleService.initRoom(battle);
		battleRoom.setIsPk(1);
		battleRoom.setPeriodId(battleCreateDetail.getPeriodId());
		battleRoom.setMaxinum(2);
		battleRoom.setMininum(2);
		battleRoom.setIsSearchAble(0);
		battleRoom.setScrollGogal(battleCreateDetail.getScrollGogal());
		battleRoom.setPlaces(1);
		battleRoom.setIsDanRoom(0);
		battleRoom.setStartTime(new DateTime());
		
		battleRoomService.add(battleRoom);
		
		battlePk.setRoomId(battleRoom.getId());
		battlePk.setBattleId(battle.getId());
		
		battlePk.setPeriodId(battleCreateDetail.getPeriodId());
		
		battlePkService.update(battlePk);
		
		
		battleEndSocketService.endPublish(battleRoom.getId());
		
		return battlePk;
	}

	public BattlePk ready(String id,Integer role,UserInfo userInfo){


		BattlePk battlePk = battlePkService.findOne(id);;


		if(role==0&&userInfo.getId().equals(battlePk.getHomeUserId())){
			
			if(battlePk.getHomeStatus().equals(BattlePk.STATUS_READY)){
				battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
				
			}else if(battlePk.getHomeStatus().equals(BattlePk.STATUS_INSIDE)){
				battlePk.setHomeStatus(BattlePk.STATUS_READY);
			}
		}else if(role==1&&!userInfo.getId().equals(battlePk.getHomeUserId())&&userInfo.getId().equals(battlePk.getBeatUserId())){
			if(battlePk.getBeatStatus().equals(BattlePk.STATUS_INSIDE)){
				battlePk.setBeatStatus(BattlePk.STATUS_READY);
			
			}else if(battlePk.getBeatStatus().equals(BattlePk.STATUS_READY)){
				battlePk.setBeatStatus(BattlePk.STATUS_INSIDE);				
			}
		}else{
			throw new RuntimeException("数据错误");
		}
		
		if(battlePk.getHomeStatus().intValue()==BattlePk.STATUS_READY&&
				battlePk.getBeatStatus().intValue()==BattlePk.STATUS_READY&&
				battlePk.getRoomStatus().intValue()==BattlePk.ROOM_STATUS_FREE){
			BattleRoom battleRoom = initRoomService.addPkRoom(battlePk.getHomeUserId(),battlePk.getBeatUserId());
			battlePk.setHomeStatus(BattlePk.STATUS_BATTLE);
			battlePk.setBeatStatus(BattlePk.STATUS_BATTLE);
			battlePk.setRoomStatus(BattlePk.ROOM_STATUS_BATTLE);
			battlePk.setRoomId(battleRoom.getId());
			battlePk.setBattleId(battleRoom.getBattleId());
			battlePk.setPeriodId(battleRoom.getPeriodId());
		}
		
		battlePkService.update(battlePk);
		pkSocketService.statusPublish(battlePk);
		
		return battlePk;
	}
}
