package com.xxdc.itask.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "HistoryIDR")
public class HistoryIDRString {
	@Id
	private int id;
	private String HistoryID;

	public HistoryIDRString() {
	}

	public HistoryIDRString(int id, String historyID) {
		super();
		this.id = id;
		HistoryID = historyID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHistoryID() {
		return HistoryID;
	}

	public void setHistoryID(String historyID) {
		HistoryID = historyID;
	}
}
