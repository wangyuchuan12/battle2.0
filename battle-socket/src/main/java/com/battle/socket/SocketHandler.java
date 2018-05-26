package com.battle.socket;
import com.alibaba.druid.support.json.JSONUtils;
import com.battle.domain.DataView;
import com.battle.service.DataViewService;
import com.wyc.common.util.CommonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaojf 2017/3/2 9:55.
 */
@Service
public class SocketHandler extends TextWebSocketHandler {
    private static final Map<String,WebSocketSession> sessionMap = new HashMap<String, WebSocketSession>();

    @Autowired
	private DataViewService dataViewService;
    
    @Autowired
    private OnlineListener onlineListener;
    
    @Autowired
    private ScheduledExecutorService executorService;
    
    final static Logger logger = LoggerFactory.getLogger(SocketHandler.class);
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        session.sendMessage(new TextMessage(session.getPrincipal().getName()+",你是第" + (sessionMap.size()) + "位访客")); //p2p

        Object parse = JSONUtils.parse(message.getPayload());

        System.out.println(".......message:"+parse);
        
        
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
    	
    	Map<String, Object> attributes = session.getAttributes();
    	final Object token = attributes.get("token");
    	final Object userId = attributes.get("userId");
    	
    	sessionMap.remove(token.toString());
    	
    	Thread timerTask = new Thread() {
			
			@Override
			public void run() {
				
				System.out.println("...............尊贵和荣耀");
				logger.debug("put session delay 1000 run");
				sessionMap.put(token.toString(),session);
				onlineListener.onLine(userId.toString());

				try{
					
				}catch(Exception e){
					logger.error("{}",e);
				}
			}
		};
		
		executorService.schedule(timerTask, 100,TimeUnit.MILLISECONDS);
    	
		/*
    	sessionMap.put(token.toString(),session);
		onlineListener.onLine(userId.toString());
    	*/
		SocketHandler.super.afterConnectionEstablished(session);
    	
    }

    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    	Map<String, Object> attributes = session.getAttributes();
    	Object token = attributes.get("token");
    	final Object userId = attributes.get("userId");
    	
    	TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {
				
				System.out.println("...............尊贵和荣耀2");
				onlineListener.downLine(userId.toString());
			}
		};
		
		executorService.schedule(timerTask,90,TimeUnit.MILLISECONDS);
    	
		/*
    	onlineListener.downLine(userId.toString());
		*/
    	sessionMap.remove(token.toString());
        super.afterConnectionClosed(session, status);
    }

    /**
     * 发送消息
     * @author xiaojf 2017/3/2 11:43
     */
    public void sendMessage(String token,String message) throws IOException {
        sendMessage(Arrays.asList(token),Arrays.asList(message));
    }

    /**
     * 发送消息
     * @author xiaojf 2017/3/2 11:43
     */
    public void sendMessage(Collection<String> acceptorList,String message) throws IOException {
        sendMessage(acceptorList,Arrays.asList(message));
       
    }
    

    /**
     * 发送消息，p2p 群发都支持
     * @author xiaojf 2017/3/2 11:43
     */
    public void sendMessage(Collection<String> acceptorList, Collection<String> msgList) throws IOException {
    	
    	System.out.println(".......acceptorlist:"+acceptorList+",msgList:"+msgList);
        if (acceptorList != null && msgList != null) {
            for (String acceptor : acceptorList) {
                WebSocketSession session = sessionMap.get(acceptor);
                
                System.out.println("..........session:"+session);
                if (session != null) {
                    for (String msg : msgList) {
                        session.sendMessage(new TextMessage(msg.getBytes()));
                    }
                }
            }
        }
    }
}