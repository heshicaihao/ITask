package com.xxdc.itask.dto.response;

import com.xxdc.itask.dto.NoteTaskListDTO;

public class NoteListResponse extends Response {

	private static final long serialVersionUID = 1L;
	private NoteTaskListDTO response;

	public NoteTaskListDTO getResponse() {
		return response;
	}

	public void setResponse(NoteTaskListDTO response) {
		this.response = response;
	}
}
