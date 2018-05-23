package com.battle.executer.vo;


public class BattleQuestionVo {
	private String id;

	//0表示选择题，1表示填空题 2表示填词题
	private Integer type;
	
	//图片地址
	private String imgUrl;
	
	private String question;
	
	
	//正确答案，填字的时候使用
	private String answer;
	
	//是否有图片
	private Integer isImg;
	
	//正确选项
	private String rightOptionId;
	
	//填充字列表，用逗号分隔，只在成语中使用
	private String fillWords;
	
	private String subjectCode;

	private Integer score;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Integer getIsImg() {
		return isImg;
	}

	public void setIsImg(Integer isImg) {
		this.isImg = isImg;
	}

	public String getRightOptionId() {
		return rightOptionId;
	}

	public void setRightOptionId(String rightOptionId) {
		this.rightOptionId = rightOptionId;
	}

	public String getFillWords() {
		return fillWords;
	}

	public void setFillWords(String fillWords) {
		this.fillWords = fillWords;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
}
