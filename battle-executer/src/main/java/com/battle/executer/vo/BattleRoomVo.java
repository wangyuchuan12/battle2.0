package com.battle.executer.vo;

import java.util.List;

public class BattleRoomVo {
	
	private String id;

	private List<BattleRoomMemberVo> battleRoomMemberVos;
	
	private List<BattleStageVo> battleStageVos;
	
	private List<BattleSubjectVo> battleSubjectVos;
	
	private Integer scrollGogal;
	
	private Integer processGogal;
	
	private Integer stageIndex;

	public List<BattleRoomMemberVo> getBattleRoomMemberVos() {
		return battleRoomMemberVos;
	}

	public void setBattleRoomMemberVos(List<BattleRoomMemberVo> battleRoomMemberVos) {
		this.battleRoomMemberVos = battleRoomMemberVos;
	}

	public List<BattleStageVo> getBattleStageVos() {
		return battleStageVos;
	}

	public void setBattleStageVos(List<BattleStageVo> battleStageVos) {
		this.battleStageVos = battleStageVos;
	}

	public List<BattleSubjectVo> getBattleSubjectVos() {
		return battleSubjectVos;
	}

	public void setBattleSubjectVos(List<BattleSubjectVo> battleSubjectVos) {
		this.battleSubjectVos = battleSubjectVos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getScrollGogal() {
		return scrollGogal;
	}

	public void setScrollGogal(Integer scrollGogal) {
		this.scrollGogal = scrollGogal;
	}

	public Integer getProcessGogal() {
		return processGogal;
	}

	public void setProcessGogal(Integer processGogal) {
		this.processGogal = processGogal;
	}

	public Integer getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}
}
