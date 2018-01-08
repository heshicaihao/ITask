package com.xxdc.itask.dto;

import java.util.List;

public class GroupUsersListDTO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer groupId;
	private String groupName;
	private String groupCode;
	private String parentGroup;
	private Integer createUser;
	private List<UserListForGroupDTO> userList;

	public GroupUsersListDTO() {
	}

	public GroupUsersListDTO(Integer groupId, String groupName, String groupCode, String parentGroup, Integer createUser) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupCode = groupCode;
		this.parentGroup = parentGroup;
		this.createUser = createUser;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
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

	public String getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(String parentGroup) {
		this.parentGroup = parentGroup;
	}

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public List<UserListForGroupDTO> getUserList() {
		return userList;
	}

	public void setUserList(List<UserListForGroupDTO> userList) {
		this.userList = userList;
	}

	@Override
	public String toString() {
		return "GroupUsersListDTO [groupId=" + groupId + ", groupName=" + groupName + ", groupCode=" + groupCode + ", parentGroup=" + parentGroup
				+ ", createUser=" + createUser + ", userListDTO=" + userList + "]";
	}
}
