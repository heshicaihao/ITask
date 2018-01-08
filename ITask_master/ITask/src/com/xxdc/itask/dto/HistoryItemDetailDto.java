package com.xxdc.itask.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoryItemDetailDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long parentTaskId;
	private int taskType;
	private String localTag;
	private int orderNo;
	private Long endDate;
	private long taskClock;
	private Long userTaskId;
	private String voice;
	private String photo;
	private long taskId;
	private String taskName;
	private String sendUserId;
	private String sendUserRealName;
	private String doUsers;
	private ArrayList<NotepadDTO> notepads;
	private List<FeebackDTO> feebacks;

	public HistoryItemDetailDto() {
		super();
	}

	public long getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(long parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getLocalTag() {
		return localTag;
	}

	public void setLocalTag(String localTag) {
		this.localTag = localTag;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public long getTaskClock() {
		return taskClock;
	}

	public void setTaskClock(long taskClock) {
		this.taskClock = taskClock;
	}

	public Long getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(Long userTaskId) {
		this.userTaskId = userTaskId;
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

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserRealName() {
		return sendUserRealName;
	}

	public void setSendUserRealName(String sendUserRealName) {
		this.sendUserRealName = sendUserRealName;
	}

	public String getDoUsers() {
		return doUsers;
	}

	public void setDoUsers(String doUsers) {
		this.doUsers = doUsers;
	}

	public ArrayList<NotepadDTO> getNotepads() {
		return notepads;
	}

	public void setNotepads(ArrayList<NotepadDTO> notepads) {
		this.notepads = notepads;
	}

	public List<FeebackDTO> getFeebacks() {
		return feebacks;
	}

	public void setFeebacks(List<FeebackDTO> feebacks) {
		this.feebacks = feebacks;
	}

	@Override
	public String toString() {
		return "HistoryItemDetailDto [parentTaskId=" + parentTaskId + ", taskType=" + taskType + ", localTag=" + localTag + ", orderNo=" + orderNo
				+ ", endDate=" + endDate + ", taskClock=" + taskClock + ", userTaskId=" + userTaskId + ", voice=" + voice + ", photo=" + photo + ", taskId="
				+ taskId + ", taskName=" + taskName + ", sendUserId=" + sendUserId + ", sendUserRealName=" + sendUserRealName + ", doUsers=" + doUsers
				+ ", notepads=" + notepads + ", feebacks=" + feebacks + "]";
	}

}
