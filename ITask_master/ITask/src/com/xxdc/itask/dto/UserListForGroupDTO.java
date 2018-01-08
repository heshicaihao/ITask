package com.xxdc.itask.dto;

import com.lidroid.xutils.db.annotation.Table;

@Table(name = "child")
public class UserListForGroupDTO implements java.io.Serializable, Comparable<UserListForGroupDTO> {
	private static final long serialVersionUID = 1L;
	private Integer userId;
	private String userName;
	private String realName;
	private String email;
	private String tel;
	private String phone;
	private String photo;
	private boolean isCheck = false;
	public GroupUsersListDTO groupUsersList;

	public UserListForGroupDTO() {
		super();
	}

	public UserListForGroupDTO(Integer userId, String userName, String realName, String email, String tel, String phone, String photo) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.realName = realName;
		this.email = email;
		this.tel = tel;
		this.phone = phone;
		this.photo = photo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public GroupUsersListDTO getGroupUsersList() {
		return groupUsersList;
	}

	public void setGroupUsersList(GroupUsersListDTO groupUsersList) {
		this.groupUsersList = groupUsersList;
	}

	@Override
	public int compareTo(UserListForGroupDTO another) {
		return userId.compareTo(another.userId);

	}

	@Override
	public String toString() {
		return "UserListForGroupDTO [userId=" + userId + ", userName=" + userName + ", realName=" + realName + ", email=" + email + ", tel=" + tel + ", phone="
				+ phone + ", photo=" + photo + "]";
	}

}
