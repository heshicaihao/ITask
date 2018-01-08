package com.xxdc.itask.dto.response;

import java.util.ArrayList;

import com.xxdc.itask.dto.GroupUsersListDTO;

public class GroupUsersListResponse extends Response {
	private static final long serialVersionUID = 4805938784457126782L;

	private ArrayList<GroupUsersListDTO> response;

	public ArrayList<GroupUsersListDTO> getResponse() {
		return response;
	}

	public void setResponse(ArrayList<GroupUsersListDTO> response) {
		this.response = response;
	}

}
