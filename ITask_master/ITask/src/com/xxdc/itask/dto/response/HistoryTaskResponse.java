package com.xxdc.itask.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.xxdc.itask.dto.UserTaskDTO;

public class HistoryTaskResponse extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<UserTaskDTO> response;
	private long updateTime;
	public ArrayList<UserTaskDTO> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<UserTaskDTO> response) {
		this.response = response;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "HistoryTaskResponse [response=" + response + ", updateTime=" + updateTime + "]";
	}

}
