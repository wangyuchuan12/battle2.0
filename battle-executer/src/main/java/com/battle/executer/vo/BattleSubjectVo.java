package com.battle.executer.vo;

import java.util.List;

public class BattleSubjectVo {

	private String id;
	
	private String name;
	
	private String imgUrl;
	
	private List<BattleQuestionVo> battleQuestionVos;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<BattleQuestionVo> getBattleQuestionVos() {
		return battleQuestionVos;
	}

	public void setBattleQuestionVos(List<BattleQuestionVo> battleQuestionVos) {
		this.battleQuestionVos = battleQuestionVos;
	}
}
