package com.battle.executer.vo;


public class BattleRoomMemberVo {
	
	//游离状态
	public static final Integer STATUS_FREE = 0;
	
	//进行中
	public static final Integer STATUS_IN = 1;
	
	//完成
	public static final Integer STATUS_COMPLETE =2;
	
	//退出
	public static final Integer STATUS_OUT=3;
	
	//结束或者失败了
	public static final Integer STATUS_END = 4;
	
	private String id;
	
	private String userId;

	private Integer loveCount;
	
	private Integer loveResidule;
	
	private Integer status;

	private String nickname;

	private String headImg;
	
	private Integer score;
	
	private Integer process;
	
	private Integer isRoomReady;
	
	private Integer isStageReady;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getLoveCount() {
		return loveCount;
	}

	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}

	public Integer getLoveResidule() {
		return loveResidule;
	}

	public void setLoveResidule(Integer loveResidule) {
		this.loveResidule = loveResidule;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public Integer getIsRoomReady() {
		return isRoomReady;
	}

	public void setIsRoomReady(Integer isRoomReady) {
		this.isRoomReady = isRoomReady;
	}

	public Integer getIsStageReady() {
		return isStageReady;
	}

	public void setIsStageReady(Integer isStageReady) {
		this.isStageReady = isStageReady;
	}
}
