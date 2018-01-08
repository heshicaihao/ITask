package com.xxdc.itask.dto;

import java.util.ArrayList;

import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
/**
 * 
 * @author Administrator
 * 
 */
@Table(name="feedback")
public class FeebackDTO implements java.io.Serializable, Comparable<FeebackDTO> {
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String userName;
	private String context;
	private Integer type;
	private Long feeTime;
	private Integer feeStatus;
	private Long taskId;
	private String realName;
	private ArrayList<String> imgs;
	private ArrayList<String> voices;
	private ArrayList<String> attachs;
	
	@Foreign(column="parentId", foreign="id")
	private UserTaskDTO userTask;

	public ArrayList<String> getImgs() {
		return imgs;
	}

	public void setImgs(ArrayList<String> imgs) {
		this.imgs = imgs;
	}

	public ArrayList<String> getVoices() {
		return voices;
	}

	public void setVoices(ArrayList<String> voices) {
		this.voices = voices;
	}

	public ArrayList<String> getAttachs() {
		return attachs;
	}

	public void setAttachs(ArrayList<String> attachs) {
		this.attachs = attachs;
	}

	public FeebackDTO() {

	}

	public FeebackDTO(Long id, Long taskId, String userName, String context, Integer type, Long feeTime,
			Integer feeStatus, String realName) {
		this.id = id;
		this.taskId = taskId;
		this.context = context;
		this.type = type;
		this.feeTime = feeTime;
		this.feeStatus = feeStatus;
		this.userName = userName;
		this.realName = realName;

	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getFeeTime() {
		return feeTime;
	}

	public void setFeeTime(Long feeTime) {
		this.feeTime = feeTime;
	}

	public Integer getFeeStatus() {
		return feeStatus;
	}

	public void setFeeStatus(Integer feeStatus) {
		this.feeStatus = feeStatus;
	}

	@Override
	public int compareTo(FeebackDTO object) {
		return id.compareTo(object.id);
	}

	@Override
	public String toString() {
		return "FeebackDTO [id=" + id + ", userName=" + userName + ", context=" + context + ", type=" + type
				+ ", feeTime=" + feeTime + ", feeStatus=" + feeStatus + ", taskId=" + taskId + ", realName=" + realName
				+ ", imgs=" + imgs + ", voices=" + voices + ", attachs=" + attachs + "]";
	}

	public UserTaskDTO getUserTask() {
		return userTask;
	}

	public void setUserTask(UserTaskDTO userTask) {
		this.userTask = userTask;
	}

}
