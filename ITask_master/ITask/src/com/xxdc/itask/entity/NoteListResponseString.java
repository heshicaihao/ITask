package com.xxdc.itask.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "NoteListResponseString")
public class NoteListResponseString {
	@Id
	private int id;
	private String noteListResponseString;

	public NoteListResponseString() {
	}

	public NoteListResponseString(int id, String noteListResponseString) {
		super();
		this.id = id;
		this.noteListResponseString = noteListResponseString;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNoteListResponseString() {
		return noteListResponseString;
	}

	public void setNoteListResponseString(String noteListResponseString) {
		this.noteListResponseString = noteListResponseString;
	}

}
