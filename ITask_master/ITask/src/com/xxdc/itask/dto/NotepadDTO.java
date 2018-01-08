package com.xxdc.itask.dto;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
/**
 * 
 * @author Administrator
 *
 */

public class NotepadDTO implements java.io.Serializable, Comparable<NotepadDTO> {

	private static final long serialVersionUID = 1L;
	@Id
	private Long notepadId;
	private String context;
	private Integer type;
	private Long createTime;
	private Long userId;
	private Long userTaskId;
	private Long updateTime;
	private Long taskId;
	private String title;
	
	@Foreign(column="parentId", foreign="notepadId")
	private UserTaskDTO userTask;
	
	private ArrayList<String> images;
	private ArrayList<String> voices;

	public NotepadDTO() {

	}

	public Long getUserTaskId() {
		return userTaskId;
	}

	public void setUserTaskId(Long userTaskId) {
		this.userTaskId = userTaskId;
	}

	public Long getNotepadId() {
		return notepadId;
	}

	public void setNotepadId(Long notepadId) {
		this.notepadId = notepadId;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public int compareTo(NotepadDTO obj) {
		return createTime.compareTo(obj.createTime);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public ArrayList<String> getVoices() {
		return voices;
	}

	public void setVoices(ArrayList<String> voices) {
		this.voices = voices;
	}

	@Override
	public String toString() {
		return "NotepadDTO [notepadId=" + notepadId + ", context=" + context + ", type=" + type + ", createTime=" + createTime + ", userId=" + userId
				+ ", userTaskId=" + userTaskId + ", updateTime=" + updateTime + ", taskId=" + taskId + ", title=" + title + ", images=" + images + ", voices="
				+ voices + "]";
	}
}
