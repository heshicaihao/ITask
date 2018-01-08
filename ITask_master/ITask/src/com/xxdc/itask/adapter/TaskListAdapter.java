package com.xxdc.itask.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.fragment.HomeFragment;
import com.xxdc.itask.widget.SlideView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

public abstract class TaskListAdapter extends BaseExpandableListAdapter{
	protected LinkedList<UserTaskDTO> mList = new LinkedList<UserTaskDTO>();
	@SuppressLint("SimpleDateFormat") 
	protected SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
	protected Context mContext;
	protected ExpandableListView mExpandableListView;

	protected HomeFragment mHomeFragment;
	
	public TaskListAdapter(Context context, ExpandableListView listview) {
		super();
		mExpandableListView = listview;
		init(context);
	}

	public TaskListAdapter(Context context, ArrayList<UserTaskDTO> list, ExpandableListView listview) {
		super();
		this.mList.addAll(list);
		mExpandableListView = listview;
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
	}
	
	@Override
	public int getGroupCount() {
		return (mList == null) ? 0 : mList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (mList == null || mList.get(groupPosition) == null) {
			return 0;
		} else {
			return mList.get(groupPosition).getChildTask().size();
		}
	}

	@Override
	public UserTaskDTO getGroup(int groupPosition) {
		return mList.get(groupPosition);
	}

	@Override
	public UserTaskDTO getChild(int groupPosition, int childPosition) {
		return mList.get(groupPosition).getChildTask().get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public abstract View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

	@Override
	public abstract View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);
	
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void expandAllGroup() {
		for (int i = 0; i < getGroupCount(); i++) {
			mExpandableListView.expandGroup(i);
		}
	}

	public void setList(LinkedList<UserTaskDTO> list) {
		mList = list;
		notifyDataSetChanged();
	}
	
	public LinkedList<UserTaskDTO> getChildList(int groupPosition) {
		return mList.get(groupPosition).getChildTask();
	}
}
