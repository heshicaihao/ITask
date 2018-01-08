package com.xxdc.itask.dto;

import java.sql.Timestamp;

public class TaskFeebackDTO implements java.io.Serializable {

	private Long id;
	private Long userId;
	private Long taskId;
	private String context;
	private Integer type;
	private Long feeTime;
	private Integer feeStatus;
	private String userName;

	// Constructors

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	/** default constructor */
	public TaskFeebackDTO() {
	}

	/** default constructor */
	public TaskFeebackDTO(Long id, Long userId, Long taskId, String context, Integer type, Long feeTime,
			Integer feeStatus, String userName) {
		this.id = id;
		this.userId = userId;
		this.taskId = taskId;
		this.context = context;
		this.type = type;
		this.feeTime = feeTime;
		this.feeStatus = feeStatus;
		this.userName = userName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
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

}
