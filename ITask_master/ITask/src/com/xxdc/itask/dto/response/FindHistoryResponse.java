package com.xxdc.itask.dto.response;

import java.util.ArrayList;
import java.util.List;

import com.xxdc.itask.dto.FindHisoryDataDTO;
import com.xxdc.itask.dto.UserTaskDTO;

public class FindHistoryResponse extends Response {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FindHisoryDataDTO response;

	public FindHisoryDataDTO getResponse() {
		return response;
	}

	public void setResponse(FindHisoryDataDTO response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "FindHistoryResponse [response=" + response + "]";
	}

}
