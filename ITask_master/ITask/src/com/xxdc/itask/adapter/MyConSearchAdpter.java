package com.xxdc.itask.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.HomeActivity;
import com.xxdc.itask.dto.UserListForGroupDTO;

public class MyConSearchAdpter extends BaseAdapter {

	private HomeActivity mActivity;
	private List<UserListForGroupDTO> data;
	private LayoutInflater inflater;

	public MyConSearchAdpter(List<UserListForGroupDTO> data, LayoutInflater inflater, Activity mActivity) {
		this.data = data;
		this.inflater = inflater;
		this.mActivity = (HomeActivity) mActivity;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ChildHolder childHolder = null;
		if (convertView == null) {
			childHolder = new ChildHolder();
			convertView = inflater.inflate(R.layout.list_context_item_child, null);
			childHolder.mChildIview = (ImageView) convertView.findViewById(R.id.iv_head_context_item);
			childHolder.mRealName = (TextView) convertView.findViewById(R.id.tv_name_context_item);
			childHolder.mTelTview = (TextView) convertView.findViewById(R.id.tv_phone_context_item);
			childHolder.mCallBt = (ImageView) convertView.findViewById(R.id.ib_call_context_item);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		childHolder.mCallBt.setFocusable(false);
		childHolder.mRealName.setText(((UserListForGroupDTO) getItem(position)).getRealName());
		childHolder.mTelTview.setText(((UserListForGroupDTO) getItem(position)).getTel());
		ITaskApp.getInstance().getBitmpUtils().display(childHolder.mChildIview, (((UserListForGroupDTO) getItem(position)).getPhoto()));
		childHolder.mCallBt.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setAction("android.intent.action.CALL");
				intent.setData(Uri.parse("tel:" + (((UserListForGroupDTO) getItem(position)).getTel())));
				mActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	class ChildHolder {
		ImageView mChildIview;// 个人头像
		TextView mRealName;// 个人名字
		TextView mTelTview;// 个人电话
		ImageView mCallBt;// 拨号按钮
	}
}
