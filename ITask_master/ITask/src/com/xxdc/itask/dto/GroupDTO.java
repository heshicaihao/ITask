package com.xxdc.itask.dto;



	import java.util.ArrayList;



	/**
	 * Adt entity. @author MyEclipse Persistence Tools
	 */
	public class GroupDTO implements
			java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private Long groupId;
	    private String groupName;
	    private String groupCode;
	    private Long parentGroup;
	    private Long createUser;
        private ArrayList<UserDTO> userList;


		
		public GroupDTO(){
			
		}
		
        public GroupDTO(Long groupId,String groupName,String groupCode,Long parentGroup,Long createUser){
			this.groupId=groupId;
			this.groupName=groupName;
			this.groupCode=groupCode;
			this.parentGroup=parentGroup;
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
		public Long getCreateUser() {
			return createUser;
		}

		public void setCreateUser(Long createUser) {
			this.createUser = createUser;
		}



}
