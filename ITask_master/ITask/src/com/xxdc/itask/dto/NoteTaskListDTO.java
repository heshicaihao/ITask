package com.xxdc.itask.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Adt entity. @author MyEclipse Persistence Tools
 */
public class NoteTaskListDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long updateTime;
	private List<NotepadDTO> notepadList = new ArrayList<NotepadDTO>();

	public List<NotepadDTO> getNotepadList() {
		return notepadList;
	}

	public void setNotepadList(List<NotepadDTO> notepadList) {
		this.notepadList = notepadList;
	}

	public NoteTaskListDTO(Long updateTime) {
		this.updateTime = updateTime;
	}

	public NoteTaskListDTO(List<NotepadDTO> notepadList, Long updateTime) {
		this.notepadList = notepadList;
		this.updateTime = updateTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "NoteTaskListDTO [updateTime=" + updateTime + ", notepadList=" + notepadList + "]";
	}

}
