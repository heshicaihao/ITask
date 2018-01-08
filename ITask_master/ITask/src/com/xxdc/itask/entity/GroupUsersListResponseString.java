package com.xxdc.itask.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "GroupUsers")
public class GroupUsersListResponseString {
	@Id
	private int id;
	private String groupUsers;

	public GroupUsersListResponseString() {
	}

	public GroupUsersListResponseString(int id, String groupUsers) {
		super();
		this.id = id;
		this.groupUsers = groupUsers;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupUsers() {
		return groupUsers;
	}

	public void setGroupUsers(String groupUsers) {
		this.groupUsers = groupUsers;
	}

}
