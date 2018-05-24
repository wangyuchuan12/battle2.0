package com.battle.socket.vo;

public class ProgressStatusVo {

	private Integer score;
	
	private Integer process;
	
	private Integer loveCount;
	
	private String memberId;
	
	private Integer status;
	
	private Integer roomStatus;
	
	private Integer thisProcess;
	
	private Integer stageIndex;
	
	public Integer getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(Integer roomStatus) {
		this.roomStatus = roomStatus;
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

	public Integer getLoveCount() {
		return loveCount;
	}

	public void setLoveCount(Integer loveCount) {
		this.loveCount = loveCount;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getThisProcess() {
		return thisProcess;
	}

	public void setThisProcess(Integer thisProcess) {
		this.thisProcess = thisProcess;
	}

	public Integer getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}
}
