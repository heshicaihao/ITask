package com.xxdc.itask.dto;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
public class UserDTO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String userName;
	private String realName;

	public UserDTO() {

	}

	public UserDTO(long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public UserDTO(long userId, String userName, String realName) {
		this.userId = userId;
		this.userName = userName;
		this.realName = realName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
