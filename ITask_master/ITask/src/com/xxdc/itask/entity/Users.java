package com.xxdc.itask.entity;

import java.util.ArrayList;
import java.util.List;

public class Users {
	private static List<String> users = new ArrayList<String>();

	public static List<String> getUsers() {
		if (null == users) {
			users = new ArrayList<String>();
		}
		return users;
	}

	public static void setUsers(List<String> users) {
		Users.users = users;
	}
	
	public static void addUsers(String user){
		Users.users.add(user);
	}
	
	public static void removeUsers(String user){
		Users.users.remove(user);
	}
}
