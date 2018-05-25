package com.battle.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wyc.annotation.IdAnnotation;
import com.wyc.annotation.ParamAnnotation;
import com.wyc.annotation.ParamEntityAnnotation;

@ParamEntityAnnotation
@Entity
@Table(name="battle_stage_rest_member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BattleStageRestMember implements Serializable{
	
	//等待状态
	public static final Integer WAIT_STATUS = 0;
	//准备状态
	public static final Integer READY_STATUS = 1;
	//已死亡
	public static final Integer DEATY_STATUS = 2;
	//已退出
	public static final Integer OUT_STATUS = 3;
	
	public static final Integer COMPLETE_STATUS = 3;
	@Id
	@IdAnnotation
	private String id;

	@Column(name="member_id")
	@ParamAnnotation
	private String memberId;
	
	@Column
	@ParamAnnotation
	private String nickname;
	
	@Column(name="img_url")
	@ParamAnnotation
	private String imgUrl;

	//0等待中 1已准备 2已死亡 3已退出
	@Column
	@ParamAnnotation
	private Integer status;
	
	@Column(name="right_count")
	@ParamAnnotation
	private Integer rightCount;
	
	@Column(name="wrong_count")
	@ParamAnnotation
	private Integer wrongCount;;
	
	@Column
	@ParamAnnotation
	private Integer score;
	
	@Column
	@ParamAnnotation
	private Integer process;
	
	@Column(name="this_score")
	@ParamAnnotation
	private Integer thisScore;
	
	@Column(name="this_process")
	@ParamAnnotation
	private Integer thisProcess;
	
	@Column(name="user_id")
	@ParamAnnotation
	private String userId;
	
	@Column(name="room_id")
	@ParamAnnotation
	private String roomId;
	
	//爱心数量上限
	@ParamAnnotation
	@Column(name="love_count")
	private Integer loveCount;
	
	//爱心剩余
	@ParamAnnotation
	@Column(name="love_residule")
	private Integer loveResidule;
	
	@ParamAnnotation
	@Column(name="stage_index")
	private Integer stageIndex;
	
	@ParamAnnotation
	@Column(name="is_online")
	private Integer isOnline;
	
	@ParamAnnotation
	@Column(name = "create_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
    private DateTime createAt;
	
	@ParamAnnotation
    @Column(name = "update_at")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonIgnore
    private DateTime updateAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getRightCount() {
		return rightCount;
	}

	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
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

	public Integer getStageIndex() {
		return stageIndex;
	}

	public void setStageIndex(Integer stageIndex) {
		this.stageIndex = stageIndex;
	}
	
	

	public Integer getThisScore() {
		return thisScore;
	}

	public void setThisScore(Integer thisScore) {
		this.thisScore = thisScore;
	}

	public Integer getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(Integer isOnline) {
		this.isOnline = isOnline;
	}
	
	

	public Integer getProcess() {
		return process;
	}

	public void setProcess(Integer process) {
		this.process = process;
	}

	public Integer getThisProcess() {
		return thisProcess;
	}

	public void setThisProcess(Integer thisProcess) {
		this.thisProcess = thisProcess;
	}

	public DateTime getCreateAt() {
		return createAt;
	}

	public void setCreateAt(DateTime createAt) {
		this.createAt = createAt;
	}

	public DateTime getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(DateTime updateAt) {
		this.updateAt = updateAt;
	}
}
