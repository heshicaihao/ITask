package com.xxdc.itask.dto.response;

import java.util.ArrayList;

import com.xxdc.itask.dto.FeebackDTO;

public class SubmitFeedbackResponse extends Response {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<FeebackDTO> response;

	public ArrayList<FeebackDTO> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<FeebackDTO> response) {
		this.response = response;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SubmitFeedbackResponse [response=" + response + "]";
	}

}
