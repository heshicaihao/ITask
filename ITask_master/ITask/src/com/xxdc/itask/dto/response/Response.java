package com.xxdc.itask.dto.response;

import java.io.Serializable;

public class Response implements Serializable{
	private static final long serialVersionUID = 4920503043717565355L;

	private boolean status;
	
	private String message;
	

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
