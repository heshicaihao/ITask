package com.xxdc.itask.dto;

import java.io.Serializable;

public class LoginDTO implements Serializable{
	private static final long serialVersionUID = -8552011424782724513L;

	private String token;
	
	private String userName;
	
	private long userId;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
