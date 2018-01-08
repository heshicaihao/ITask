package com.xxdc.itask.entity;

public class ListFooterEntity {
	private int parentTaskId;
	
	public ListFooterEntity(int parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	
	public int getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(int parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
}
