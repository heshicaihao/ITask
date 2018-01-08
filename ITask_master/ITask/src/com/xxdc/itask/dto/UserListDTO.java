package com.xxdc.itask.dto;

import java.util.ArrayList;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
public class UserListDTO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<UserDTO> user;

	public ArrayList<UserDTO> getUser() {
		return user;
	}

	public void setUser(ArrayList<UserDTO> user) {
		this.user = user;
	}

}
