package com.xxdc.itask.dto;

import java.util.ArrayList;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
public class GroupUserDTO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String userName;
	private Long groupId;
	private String groupName;
	private String groupCode;
	private Long parentGroup;

	private ArrayList<UserDTO> userList;

	public GroupUserDTO() {

	}

	public GroupUserDTO(Long groupId, Long userId) {
		this.userId = userId;
		this.groupId = groupId;

	}

	public GroupUserDTO(long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Long getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(Long parentGroup) {
		this.parentGroup = parentGroup;
	}

	public ArrayList<UserDTO> getUserList() {
		return userList;
	}

	public void setUserList(ArrayList<UserDTO> userList) {
		this.userList = userList;
	}

}
