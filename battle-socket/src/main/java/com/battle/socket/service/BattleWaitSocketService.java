package com.battle.socket.service;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.battle.domain.BattleRoom;
import com.battle.domain.BattleWaitUser;
import com.battle.service.BattleWaitUserService;
import com.battle.socket.MessageHandler;
import com.battle.socket.MessageVo;

@Service
public class BattleWaitSocketService {

	@Autowired
	private  MessageHandler messageHandler;
	
	final static Logger logger = LoggerFactory.getLogger(BattleWaitSocketService.class);
	@Autowired
	private BattleWaitUserService battleWaitUserService;
	
	@Autowired
    private ScheduledExecutorService executorService;
	
	
	public void waitEndPublish(final BattleRoom battleRoom,final String waitId)throws Exception{
		executorService.schedule(new TimerTask() {
			@Override
			public void run() {
				try{
					
					List<BattleWaitUser> battleWaitUsers = battleWaitUserService.findAllByWaitIdAndStatus(waitId,BattleWaitUser.READY_STATUS);
					List<String> userIds = new ArrayList<>();
					for(BattleWaitUser battleWaitUser:battleWaitUsers){
						String userId = battleWaitUser.getUserId();
						userIds.add(userId);
					}
					final MessageVo messageVo = new MessageVo();
					messageVo.setData(battleRoom);
					messageVo.setType(MessageVo.USERS_TYPE);
					messageVo.setUserIds(userIds);
					messageVo.setCode(MessageVo.WAIT_END_CODE);
					messageHandler.sendMessage(messageVo);
					
				}catch(Exception e){
					logger.error("{}",e);
				}
			}
		},1,TimeUnit.SECONDS);
	}
	
	public void waitPublish(final BattleWaitUser user)throws Exception{
	
		
		System.out.println("........waitPublish");
		executorService.schedule(new TimerTask() {
			@Override
			public void run() {
				try{
					List<BattleWaitUser> battleWaitUsers = battleWaitUserService.findAllByWaitId(user.getWaitId());
					
					System.out.println(".............battleWaitUsers:"+battleWaitUsers);
					List<String> userIds = new ArrayList<>();
					for(BattleWaitUser battleWaitUser:battleWaitUsers){
						String userId = battleWaitUser.getUserId();
						if(!user.getUserId().equals(userId)){
							userIds.add(userId);
						}
					}
					
					System.out.println(".............userIds:"+userIds);
					
					final MessageVo messageVo = new MessageVo();
					messageVo.setData(user);
					messageVo.setType(MessageVo.USERS_TYPE);
					messageVo.setUserIds(userIds);
					messageVo.setCode(MessageVo.WAIT_STATUS_CODE);
					messageHandler.sendMessage(messageVo);
				}catch(Exception e){
					logger.error("{}",e);
				}
			}
		},1,TimeUnit.SECONDS);
		
	}
	
}
