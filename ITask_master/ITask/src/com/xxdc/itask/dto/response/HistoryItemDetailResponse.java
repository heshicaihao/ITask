package com.xxdc.itask.dto.response;

import java.io.Serializable;
import com.xxdc.itask.dto.UserTaskDTO;

public class HistoryItemDetailResponse extends Response implements Serializable {
	/**
	 * 获取单个历史任务的详细内容
	 */
	private static final long serialVersionUID = 1L;
	private UserTaskDTO response;

	public UserTaskDTO getResponse() {
		return response;
	}

	public void setResponse(UserTaskDTO response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "HistoryItemDetailResponse [response=" + response + "]";
	}
}
