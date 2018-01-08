package com.xxdc.itask.dto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;
import com.xxdc.itask.widget.SlideView;
/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
/**
 * 
 * @author Administrator
 *
 */
@Table(name="user_task")
public class UserTaskDTO implements java.io.Serializable, Comparable<UserTaskDTO> {
	private static final long serialVersionUID = 1L;
	@NoAutoIncrement
	@Id
	@Unique
	private Long userTaskId;
	private long userId;
	//反馈
	private long taskId;
	private String taskName;
	private String voice;
	private String photo;
	private long taskClock;
	private Long endDate;
	private int orderNo;
	private String localTag; //1.删除
	private int taskType;
	private long parentTaskId;
	private String sendUserId;
	private String sendUserRealName;
	private String doUsers;
	private long localId;
	private transient SlideView slideView;
	private int status = 0;//状态

	@Finder(valueColumn="taskId", targetColumn="parentId")
	private List<FeebackDTO> feebacks;
	
	@Finder(valueColumn="userTaskId", targetColumn="parentId")
	private List<NotepadDTO> notepads;
	
	/**子项*/
	private LinkedList<UserTaskDTO> childTask = new LinkedList<UserTaskDTO>();
	
	/** full constructor */
	
	public UserTaskDTO(){
		
	}
	
	public UserTaskDTO(Long userTaskId) {
		this.userTaskId = userTaskId;
	}

	@Override
	public int compareTo(UserTaskDTO obj) {
		return endDate.compareTo(obj.endDate);
	}

	public Long getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(Long userTaskId) {
		this.userTaskId = userTaskId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public long getTaskClock() {
		return taskClock;
	}

	public void setTaskClock(long taskClock) {
		this.taskClock = taskClock;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public String getLocalTag() {
		return localTag;
	}

	public void setLocalTag(String localTag) {
		this.localTag = localTag;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public long getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(long parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public List<FeebackDTO> getFeebacks() {
		return feebacks;
	}

	public void setFeebacks(ArrayList<FeebackDTO> feebacks) {
		this.feebacks = feebacks;
	}

	public ArrayList<NotepadDTO> getNotepads() {
		return (ArrayList<NotepadDTO>) notepads;
	}

	public void setNotepads(ArrayList<NotepadDTO> notepads) {
		this.notepads = notepads;
	}

	public LinkedList<UserTaskDTO> getChildTask() {
		return childTask;
	}

	public void setChildTask(LinkedList<UserTaskDTO> childTask) {
		this.childTask = childTask;
	}

	public String getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(String sendUserId) {
		this.sendUserId = sendUserId;
	}

	public String getSendUserRealName() {
		return sendUserRealName;
	}

	public void setSendUserRealName(String sendUserRealName) {
		this.sendUserRealName = sendUserRealName;
	}

	public String getDoUsers() {
		return doUsers;
	}

	public void setDoUsers(String doUsers) {
		this.doUsers = doUsers;
	}

	@Override
	public String toString() {
		return "UserTaskDTO [userTaskId=" + userTaskId + ", taskId=" + taskId + ", taskName=" + taskName + ", voice="
				+ voice + ", photo=" + photo + ", taskClock=" + taskClock + ", endDate=" + endDate + ", orderNo="
				+ orderNo + ", localTag=" + localTag + ", taskType=" + taskType + ", parentTaskId=" + parentTaskId
				+ ", sendUserId=" + sendUserId + ", sendUserRealName=" + sendUserRealName + ", doUsers=" + doUsers
				+ ", taskFeebacks=" + ", feebacks=" + feebacks + ", notepads=" + notepads
				+ ", childTask=" + childTask + "]";
	}

	public SlideView getSlideView() {
		return slideView;
	}

	public void setSlideView(SlideView slideView) {
		this.slideView = slideView;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

//	public SlideView getSlideView() {
//		return slideView;
//	}

//	public void setSlideView(SlideView slideView) {
//		this.slideView = slideView;
//	}


}
