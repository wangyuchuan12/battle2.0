package com.battle.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.battle.domain.Battle;
import com.battle.domain.BattleCreateDetail;
import com.battle.domain.BattlePeriodMember;
import com.battle.domain.BattlePk;
import com.battle.domain.BattleRoom;
import com.battle.domain.BattleRoomPk;
import com.battle.filter.api.BattleTakepartApiFilter;
import com.battle.filter.element.LoginStatusFilter;
import com.battle.service.BattleCreateDetailService;
import com.battle.service.BattlePkService;
import com.battle.service.BattleRoomPkService;
import com.battle.service.BattleRoomService;
import com.battle.service.BattleService;
import com.battle.service.other.BattleRoomHandleService;
import com.battle.service.redis.BattlePkRedisService;
import com.battle.socket.service.BattleEndSocketService;
import com.battle.socket.service.PkSocketService;
import com.wyc.annotation.HandlerAnnotation;
import com.wyc.common.domain.vo.ResultVo;
import com.wyc.common.session.SessionManager;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;
import com.wyc.handle.BattlePkHandleService;
import com.wyc.handle.InitRoomService;

@Controller
@RequestMapping(value="/api/battlePk/")
public class BattlePkApi {

	@Autowired
	private BattlePkService battlePkService;
	
	@Autowired
	private BattleRoomHandleService battleRoomHandleService;
	
	@Autowired
	private BattleCreateDetailService battleCreateDetailServie;
	
	@Autowired
	private BattleService battleService;
	
	@Autowired
	private BattleRoomService battleRoomService;
	
	@Autowired
	private BattlePkRedisService battlePkRedisService;
	
	@Autowired
	private BattleRoomPkService battleRoomPkService;
	
	@Autowired
	private BattleEndSocketService battleEndSocketService;
	
	@Autowired
	private InitRoomService initRoomService;
	
	@Autowired
	private PkSocketService pkSocketService;
	
	@Autowired
	private BattlePkHandleService battlePkHandleService;
	
	
	final static Logger logger = LoggerFactory.getLogger(BattlePkApi.class);
	
	
	@RequestMapping(value="pkMain")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo pkMain(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		BattlePk battlePk = battlePkService.findOneByHomeUserId(userInfo.getId());
		
		BattleRoomPk battleRoomPk = battleRoomPkService.findOneByUserId(userInfo.getId());
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("battlePk", battlePk);
		data.put("battleRoomPk", battleRoomPk);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
	}
	
	
	@RequestMapping(value="battleRoomPkInto")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo battleRoomPkInto(HttpServletRequest httpServletRequest)throws Exception{
	
		String maxinum = httpServletRequest.getParameter("maxinum");
		String mininum = httpServletRequest.getParameter("mininum");
		
		
		
		if(CommonUtil.isEmpty(maxinum)){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		if(CommonUtil.isEmpty(mininum)){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			return resultVo;
		}
		
		Integer maxinumInt = Integer.parseInt(maxinum);
		Integer mininumInt = Integer.parseInt(mininum);
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		BattleRoomPk battleRoomPk = battleRoomPkService.findOneByUserId(userInfo.getId());
		
		if(battleRoomPk==null){
			battleRoomPk = new BattleRoomPk();
			battleRoomPk.setNickname(userInfo.getNickname());
			battleRoomPk.setUserId(userInfo.getId());
			battleRoomPkService.add(battleRoomPk);
		}
		
		BattleRoom battleRoom = null;
		
		if(!CommonUtil.isEmpty(battleRoomPk.getRoomId())){
			battleRoom = battleRoomService.findOne(battleRoomPk.getRoomId());
		}
		
		if(battleRoom==null){
			List<BattleCreateDetail> battleCreateDetails = battleCreateDetailServie.findAllByIsDefault(1);
			
			if(battleCreateDetails==null||battleCreateDetails.size()==0){
				ResultVo resultVo = new ResultVo();
				
				resultVo.setSuccess(false);
				
				resultVo.setErrorMsg("createDetail为空");
				
				return resultVo;
			}
			
			BattleCreateDetail battleCreateDetail = battleCreateDetails.get(0);
			Battle battle = battleService.findOne(battleCreateDetail.getBattleId());
			battleRoom = battleRoomHandleService.initRoom(battle);
			
			battleRoom.setIsPk(0);
			battleRoom.setPeriodId(battleCreateDetail.getPeriodId());
			battleRoom.setMaxinum(maxinumInt);
			battleRoom.setMininum(mininumInt);
			battleRoom.setIsSearchAble(0);
			battleRoom.setScrollGogal(battleCreateDetail.getScrollGogal());
			battleRoom.setPlaces(10);
			battleRoom.setIsDanRoom(0);
			battleRoom.setIsIncrease(1);
			battleRoom.setStartTime(new DateTime());
			
			battleRoomService.add(battleRoom);
		}
		
		battleRoomPk.setRoomId(battleRoom.getId());
		
		battleRoomPk.setBattleId(battleRoom.getBattleId());
		
		battleRoomPkService.update(battleRoomPk);
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		resultVo.setData(battleRoom);
		
		return resultVo;
		
	}
	
	//主场方进入
	@RequestMapping(value="homeInto")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo homeInto(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		
		BattlePk battlePk = battlePkHandleService.homeInto(userInfo);
		
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("id", battlePk.getId());
		data.put("homeUserId", battlePk.getHomeUserId());
		data.put("homeUserImgurl", battlePk.getHomeUserImgurl());
		data.put("homeUsername", battlePk.getHomeUsername());
		data.put("homeStatus", battlePk.getHomeStatus());
		
		data.put("beatUserId", battlePk.getBeatUserId());
		data.put("beatUserImgurl", battlePk.getBeatUserImgurl());
		data.put("beatUsername", battlePk.getBeatUsername());
		data.put("beatStatus", battlePk.getBeatStatus());
		data.put("battleCount", battlePk.getBattleCount());
		data.put("roomStatus", battlePk.getRoomStatus());
		
		data.put("roomId", battlePk.getRoomId());
		
		data.put("battleId", battlePk.getBattleId());
		
		data.put("periodId", battlePk.getPeriodId());
		
		data.put("role", 0);
		
		if(battlePk.getHomeUserId().equals(userInfo.getId())){
			data.put("isObtain",1);
		}else{
			data.put("isObtain",0);
		}
		
		
		ResultVo resultVo = new ResultVo();
		
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
		
	}
	
	
	@RequestMapping(value="beatOut")
	@ResponseBody
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo beatOut(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);

		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		BattlePk battlePk = battlePkService.findOne(id);
		
		/*if(battlePk.getRoomStatus().intValue()!=BattlePk.ROOM_STATUS_END){
			BattleRoom battleRoom = battleRoomService.findOne(battlePk.getRoomId());
			if(battleRoom.getStatus().intValue()==BattleRoom.STATUS_END){
				battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
				battlePk.setBeatStatus(BattlePk.STATUS_INSIDE);
				battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
				battlePkService.update(battlePk);
			}
		}*/
		
		if(CommonUtil.isEmpty(battlePk.getBeatUserId())){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorMsg("没有客场选手");
			return resultVo;
		}
		
		if(!battlePk.getBeatUserId().equals(userInfo.getId())){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorMsg("客场选手不符合");
			return resultVo;
		}
		
		if(CommonUtil.isEmpty(battlePk.getRoomId())){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorMsg("roomId不能为空");
			return resultVo;
		}
		
		battlePk.setBeatUserImgurl("");
		battlePk.setBeatUserId("");
		
		final BattleRoom battleRoom = battleRoomService.findOne(battlePk.getRoomId());
		battleRoom.setStatus(BattleRoom.STATUS_END);
		battleRoom.setEndType(BattleRoom.FORCE_END_TYPE);
		
		battleRoomService.update(battleRoom);
		
		
	
		new Thread(){
			public void run() {
				try{
					battleEndSocketService.endPublish(battleRoom.getId());
				}catch(Exception e){
					logger.error("{}",e);
				}
			}
		}.start();
		
		ResultVo resultVo = new ResultVo();
		resultVo.setSuccess(true);
		
		return resultVo;
	}
	
	
	//客场方进入
	@RequestMapping(value="beatInto")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo beatInto(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		BattlePk battlePk = battlePkHandleService.beatInto(id, userInfo);
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("id", battlePk.getId());
		data.put("homeUserId", battlePk.getHomeUserId());
		data.put("homeUserImgurl", battlePk.getHomeUserImgurl());
		data.put("homeUsername", battlePk.getHomeUsername());
		data.put("homeStatus", battlePk.getHomeStatus());
		
		data.put("beatUserId", battlePk.getBeatUserId());
		data.put("beatUserImgurl", battlePk.getBeatUserImgurl());
		data.put("beatUsername", battlePk.getBeatUsername());
		data.put("beatStatus", battlePk.getBeatStatus());
		data.put("battleCount", battlePk.getBattleCount());
		data.put("roomStatus", battlePk.getRoomStatus());
		data.put("battleId", battlePk.getBattleId());
		data.put("periodId", battlePk.getPeriodId());
		data.put("roomId", battlePk.getRoomId());
		
		data.put("role", 1);
		
		if(battlePk.getBeatUserId().equals(userInfo.getId())){
			data.put("isObtain",1);
		}else{
			data.put("isObtain",0);
		}
		
		ResultVo resultVo = new ResultVo();
		
		resultVo.setSuccess(true);
		resultVo.setData(data);
		
		return resultVo;
	}
	
	
	@RequestMapping(value="restart")
	@ResponseBody
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo restart(HttpServletRequest httpServletRequest)throws Exception{
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		battlePkHandleService.restart(userInfo);

		ResultVo resultVo = new ResultVo();
		
		resultVo.setSuccess(true);
		
		return resultVo;
	}
	
	
	@RequestMapping(value="immediateData")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=LoginStatusFilter.class)
	public ResultVo immediateData(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		String id = httpServletRequest.getParameter("id");
		
		if(CommonUtil.isEmpty(id)){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorMsg("调用参数id不能为空");
			logger.error("调用immediateData方法的时候参数传入的参数id为空");
			return resultVo;
		}
		
		BattlePk battlePk = battlePkService.findOne(id);

		if(battlePk==null){
			ResultVo resultVo = new ResultVo();
			resultVo.setSuccess(false);
			resultVo.setErrorMsg("返回battlePk对象为空");
			logger.error("调用immediateData方法的时候返回battlePk对象为空");
			return resultVo;
		}
		
		if(battlePk.getRoomStatus().intValue()!=BattlePk.ROOM_STATUS_CALL){
			BattleRoom battleRoom = battleRoomService.findOne(battlePk.getRoomId());
			if(battleRoom!=null){
				if(battleRoom.getStatus().intValue()==BattleRoom.STATUS_END){
					battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
					battlePk.setBeatStatus(BattlePk.STATUS_INSIDE);
					battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
					battlePk.setRoomId("");
					battlePkService.update(battlePk);
				}
			}else{
				battlePk.setRoomStatus(BattlePk.ROOM_STATUS_FREE);
				battlePk.setBeatStatus(BattlePk.STATUS_INSIDE);
				battlePk.setHomeStatus(BattlePk.STATUS_INSIDE);
				battlePk.setRoomId("");
				battlePkService.update(battlePk);
			}
		}
		
		String homeUserId = battlePk.getHomeUserId();
		
		String beatUserId = battlePk.getBeatUserId();
		
		if(!CommonUtil.isEmpty(homeUserId)){
			if(homeUserId.equals(userInfo.getId())){
				battlePk.setHomeActiveTime(new DateTime());
			}
		}else if(!CommonUtil.isEmpty(beatUserId)){
			if(beatUserId.equals(userInfo.getId())){
				battlePk.setBeatActiveTime(new DateTime());
			}
		}
		
		
		battlePkService.update(battlePk);
	
		
		ResultVo resultVo = new ResultVo();
		
		resultVo.setSuccess(true);
		
		resultVo.setData(battlePk);
		
		return resultVo;
		
	}
	
	
	@RequestMapping(value="ready")
	@ResponseBody
	@Transactional
	@HandlerAnnotation(hanlerFilter=BattleTakepartApiFilter.class)
	public ResultVo ready(HttpServletRequest httpServletRequest)throws Exception{
		
		SessionManager sessionManager = SessionManager.getFilterManager(httpServletRequest);
				
		String id = httpServletRequest.getParameter("id");
		
		String role = httpServletRequest.getParameter("role");

		if(CommonUtil.isEmpty(role)){
			role = "0";
		}
		
		Integer roleInt = Integer.parseInt(role);
		
		UserInfo userInfo = sessionManager.getObject(UserInfo.class);
		
		BattlePk battlePk= battlePkHandleService.ready(id, roleInt, userInfo);
		
		ResultVo resultVo = new ResultVo();
		
		resultVo.setSuccess(true);
		
		resultVo.setData(battlePk);
		
		return resultVo;
		
	}
	
}
