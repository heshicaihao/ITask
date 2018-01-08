package com.xxdc.itask.adapter;

import java.util.List;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.UserListForGroupDTO;
import com.xxdc.itask.entity.Users;

public class AllotGridViewAdapter extends BaseAdapter {
	private List<String> users;
	private Activity context;
	private ExpandableListView mContactElv;
	List<List<UserListForGroupDTO>> userLists;
	private CallBack callBack;

	public AllotGridViewAdapter(List<String> users, Activity context, ExpandableListView mContactElv, int flag, List<List<UserListForGroupDTO>> userLists, CallBack callBck) {
		this.mContactElv = mContactElv;
		this.context = context;
		this.users = users;
		this.userLists = userLists;
		this.callBack = callBck;
	}

	public AllotGridViewAdapter(List<String> users, Activity context, int flag, List<List<UserListForGroupDTO>> userLists) {
		this.context = context;
		this.users = users;
		this.userLists = userLists;
	}

	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		return users;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = context.getLayoutInflater().inflate(R.layout.allot_gridview_item, null);
			viewHolder.mNameTv = (TextView) convertView.findViewById(R.id.tv_name_allot_gvitem);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		setListener(viewHolder, position);
		System.out.println(Users.getUsers());
		viewHolder.mNameTv.setText(users.get(position).split(":")[1]);
		return convertView;
	}

	private void setListener(ViewHolder viewHolder, final int position) {
		viewHolder.mNameTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * 移除GridView中的数据通知适配器刷新
				 * 同时设置实体类里的check属性
				 * 并回调activity通知ListView刷新
				 */
				users.remove(position);
				notifyDataSetChanged();// 通知AllotGridViewAdapter刷新
				if (callBack != null && users.size() == 0) {
					callBack.callBackMethod(v, position);
				}
			}
		});
	}

	class ViewHolder {
		TextView mNameTv;
	}
	
	public interface CallBack {
		abstract void callBackMethod(View v, int p);
	}
}
