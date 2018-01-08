package com.xxdc.itask.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class FindHisoryDataDTO implements Serializable {

	/**
	 * 根据关键字和时间来查询相应的任务
	 * author:yxb
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<UserTaskDTO> userTaskList;
	private long updateTime;
	public ArrayList<UserTaskDTO> getUserTaskList() {
		return userTaskList;
	}
	public void setUserTaskList(ArrayList<UserTaskDTO> userTaskList) {
		this.userTaskList = userTaskList;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "FindHisoryDataDTO [userTaskList=" + userTaskList + ", updateTime=" + updateTime + "]";
	}

}
