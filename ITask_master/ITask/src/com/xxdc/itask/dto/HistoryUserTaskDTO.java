package com.xxdc.itask.dto;

import android.annotation.SuppressLint;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
/**
 * 
 * @author Administrator
 * 
 */
public class HistoryUserTaskDTO implements java.io.Serializable, Comparable<HistoryUserTaskDTO> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userTaskId;
	private Long taskId;
	private String taskName;
	private String voice;
	private String photo;
	private Timestamp taskClock;
	private Timestamp endDate;
	private Integer taskType;
	private Long parentTaskId;
	@SuppressLint("UseSparseArrays")
	private Map<Long, String> childTasks = new HashMap<Long, String>(0);
	private String parentTaskName;

	/** full constructor */
	public HistoryUserTaskDTO() {

	}

	public Long getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(Long userTaskId) {
		this.userTaskId = userTaskId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Timestamp getTaskClock() {
		return taskClock;
	}

	public void setTaskClock(Timestamp taskClock) {
		this.taskClock = taskClock;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	@Override
	public int compareTo(HistoryUserTaskDTO obj) {
		return endDate.compareTo(obj.endDate);
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Long getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(Long parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public Map<Long, String> getChildTasks() {
		return childTasks;
	}

	public void setChildTasks(Map<Long, String> childTasks) {
		this.childTasks = childTasks;
	}

	public String getParentTaskName() {
		return parentTaskName;
	}

	public void setParentTaskName(String parentTaskName) {
		this.parentTaskName = parentTaskName;
	}

	@Override
	public String toString() {
		return "HistoryUserTaskDTO [userTaskId=" + userTaskId + ", taskId=" + taskId + ", taskName=" + taskName
				+ ", voice=" + voice + ", photo=" + photo + ", taskClock=" + taskClock + ", endDate=" + endDate
				+ ", taskType=" + taskType + ", parentTaskId=" + parentTaskId + ", childTasks=" + childTasks
				+ ", parentTaskName=" + parentTaskName + "]";
	}

}
