package com.xxdc.itask.dto.response;

import com.xxdc.lib.update.RemoteDTO;

public class RemoteResponse extends Response{
	private static final long serialVersionUID = 4819978222123567417L;
	
	private RemoteDTO response;

	public RemoteDTO getResponse() {
		return response;
	}

	public void setResponse(RemoteDTO response) {
		this.response = response;
	}
}
