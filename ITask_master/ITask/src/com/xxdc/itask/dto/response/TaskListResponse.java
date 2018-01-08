package com.xxdc.itask.dto.response;

import com.xxdc.itask.dto.UserTaskListDTO;

public class TaskListResponse extends Response {
	private static final long serialVersionUID = 4805938784457126782L;
	
	private UserTaskListDTO response;

	public UserTaskListDTO getResponse() {
		return response;
	}

	public void setResponse(UserTaskListDTO response) {
		this.response = response;
	}
}
