package com.xxdc.itask.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
public class UserTaskListDTO implements java.io.Serializable {
	private Long updateTime;
	List<UserTaskDTO> userTaskList = new ArrayList<UserTaskDTO>();
	List<NotepadDTO> notepadList = new ArrayList<NotepadDTO>();

	public List<NotepadDTO> getNotepadList() {
		return notepadList;
	}

	public void setNotepadList(List<NotepadDTO> notepadList) {
		this.notepadList = notepadList;
	}

	public UserTaskListDTO(List<UserTaskDTO> list, Long updateTime) {
		this.userTaskList = list;
		this.updateTime = updateTime;
	}

	public UserTaskListDTO(List<UserTaskDTO> userTaskList, List<NotepadDTO> notepadList, Long updateTime) {
		this.userTaskList = userTaskList;
		this.notepadList = notepadList;
		this.updateTime = updateTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public List<UserTaskDTO> getUserTaskList() {
		return userTaskList;
	}

	public void setUserTaskList(ArrayList<UserTaskDTO> userTaskList) {
		this.userTaskList = userTaskList;
	}

	@Override
	public String toString() {
		return "UserTaskListDTO [updateTime=" + updateTime + ", userTaskList=" + userTaskList + ", notepadList=" + notepadList + "]";
	}

}
