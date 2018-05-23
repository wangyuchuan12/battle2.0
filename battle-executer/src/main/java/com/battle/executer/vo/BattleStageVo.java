package com.battle.executer.vo;

import java.util.List;

public class BattleStageVo {

	private String id;
	
	private Integer stageIndex;
	
	private List<BattleSubjectVo> battleSubjectVos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}

	public List<BattleSubjectVo> getBattleSubjectVos() {
		return battleSubjectVos;
	}

	public void setBattleSubjectVos(List<BattleSubjectVo> battleSubjectVos) {
		this.battleSubjectVos = battleSubjectVos;
	}
}
