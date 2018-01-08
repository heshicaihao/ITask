package com.xxdc.itask.dto.response;

import com.xxdc.itask.dto.NotepadDTO;

public class CreatNotepadResponse extends Response {
	private static final long serialVersionUID = 5607383897495821567L;

	private NotepadDTO response;

	public NotepadDTO getResponse() {
		return response;
	}

	public void setResponse(NotepadDTO response) {
		this.response = response;
	}
}
