package com.xxdc.itask.dto.response;

import com.xxdc.itask.dto.LoginDTO;

public class LoginResponse extends Response{
	private static final long serialVersionUID = 5607383897495821567L;
	
	private LoginDTO response;

	public LoginDTO getResponse() {
		return response;
	}

	public void setResponse(LoginDTO response) {
		this.response = response;
	}
}
