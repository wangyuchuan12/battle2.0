package com.battle.socket;

import java.util.List;


import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.battle.domain.BattleStageRestMember;
import com.battle.domain.UserStatus;
import com.battle.service.BattleStageRestMemberService;
import com.battle.service.UserStatusService;
import com.battle.socket.service.BattleStageRestPublishService;
import com.wyc.common.service.WxUserInfoService;
import com.wyc.common.util.CommonUtil;
import com.wyc.common.wx.domain.UserInfo;

@Service
public class OnlineListener {

	@Autowired
	private WxUserInfoService userInfoService;
	
	@Autowired
	private UserStatusService userStatusService;
	
	@Autowired
    private PlatformTransactionManager platformTransactionManager;
	
	@Autowired
	private BattleStageRestMemberService battleStageRestMemberService;
	
	@Autowired
	private BattleStageRestPublishService battleStageRestPublishService;

	final static Logger logger = LoggerFactory.getLogger(OnlineListener.class);
	public synchronized void onLine(final String id){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
    	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    	
    	TransactionStatus transactionStatus = platformTransactionManager.getTransaction(def);
		UserInfo userInfo = userInfoService.findOne(id);
		
		UserStatus userStatus = null;
		if(!CommonUtil.isEmpty(userInfo.getStatusId())){
			userStatus = userStatusService.findOne(userInfo.getStatusId());
		}
		
		System.out.println("onLine userStatus1:"+userStatus+",userId:"+id);
		
		if(userStatus==null){
			userStatus = userStatusService.findOneByUserId(userInfo.getId());
		}
		
		System.out.println("onLine userStatus2:"+userStatus+",userId:"+id);
		
		
		if(userStatus==null){
			userStatus = new UserStatus();
			userStatus.setIsLine(1);
			userStatus.setToken(userInfo.getToken());
			userStatus.setUserId(userInfo.getId());
			userStatus.setOnLineCount(0);
			userStatus.setDownLineCount(0);
			userStatusService.add(userStatus);
			
			userInfo.setStatusId(userStatus.getId());
			userInfoService.update(userInfo);
		}
		
		Integer onLineCount = userStatus.getOnLineCount();
		if(onLineCount==null){
			onLineCount = 0;
		}
		onLineCount++;
		userStatus.setOnLineCount(onLineCount);
		
		userStatus.setIsLine(1);
		userStatus.setOnLineAt(new DateTime());
		
		/*List<BattleStageRestMember> battleStageRestMembers = battleStageRestMemberService.findAllByUserId(userInfo.getId());
		if(battleStageRestMembers!=null&&battleStageRestMembers.size()>0){
			for(BattleStageRestMember battleStageRestMember:battleStageRestMembers){
				battleStageRestMember.setIsOnline(1);
				battleStageRestMemberService.update(battleStageRestMember);
				try{
					battleStageRestPublishService.stageRestMemberStatusPublish(battleStageRestMember);
				}catch(Exception e){
					logger.error("{}",e);
				}
			}
		}*/
		
		userStatusService.update(userStatus);
		platformTransactionManager.commit(transactionStatus);
		
		
		/*
		DataView dataView = dataViewService.findOneByCode(DataView.ONELINE_NUM_CODE);
    	String value = dataView.getValue();
    	Integer num = 0;
    	if(CommonUtil.isNotEmpty(value)){
    		num = Integer.parseInt(value);
    	}
    	num++;
    	dataView.setValue(num+"");
    	dataViewService.update(dataView);*/
    	
    	//
		
	}
	
	
	public synchronized void downLine(final String id){

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
    	def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    	
    	TransactionStatus transactionStatus = platformTransactionManager.getTransaction(def);
		UserInfo userInfo = userInfoService.findOne(id);
		
		List<BattleStageRestMember> battleStageRestMembers = battleStageRestMemberService.findAllByUserIdAndIsOnline(userInfo.getId(),1);
		
		if(battleStageRestMembers!=null&&battleStageRestMembers.size()>0){
			for(BattleStageRestMember battleStageRestMember:battleStageRestMembers){
				battleStageRestMember.setIsOnline(0);
				battleStageRestMember.setStatus(BattleStageRestMember.OUT_STATUS);
				battleStageRestMemberService.update(battleStageRestMember);
				try{
					battleStageRestPublishService.stageRestMemberStatusPublish(battleStageRestMember);
				}catch(Exception e){
					logger.error("{}",e);
				}
			}
		}
		
		String statusId = userInfo.getStatusId();
		UserStatus userStatus = userStatusService.findOne(statusId);
		
		System.out.println("downLine userStatus1:"+userStatus+",userId:"+id);
		
		if(userStatus==null){
			userStatus = userStatusService.findOneByUserId(userInfo.getId());
		}
		
		System.out.println("downLine userStatus2:"+userStatus+",userId:"+id);
		
		if(userStatus!=null){
			Integer downLineCount = userStatus.getDownLineCount();
			if(downLineCount==null){
				downLineCount = 0;
			}
			downLineCount++;
			userStatus.setDownLineCount(downLineCount);
			userStatus.setIsLine(0);
			userStatus.setDownLineAt(new DateTime());
			
			userStatusService.update(userStatus);
		}else{
			logger.error("userStatus is null,this uerId  is {},statusId is {}",id,statusId);
			
		}
		platformTransactionManager.commit(transactionStatus);
		
		
		/*
		DataView dataView = dataViewService.findOneByCode(DataView.ONELINE_NUM_CODE);
    	String value = dataView.getValue();
    	Integer num = 0;
    	if(CommonUtil.isNotEmpty(value)){
    		num = Integer.parseInt(value);
    	}
    	num--;
    	if(num<0){
    		num = 0;
    	}
    	dataView.setValue(num+"");
    	dataViewService.update(dataView);*/
    	
    

	}
}
