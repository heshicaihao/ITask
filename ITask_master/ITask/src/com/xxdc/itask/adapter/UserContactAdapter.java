package com.xxdc.itask.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.ContextDetailsActivity;
import com.xxdc.itask.activity.HomeActivity;
import com.xxdc.itask.dto.GroupUsersListDTO;
import com.xxdc.itask.dto.UserListForGroupDTO;
import com.xxdc.itask.fragment.ContextDetailFragment;
import com.xxdc.itask.util.FormatTools;
import com.xxdc.itask.util.StringUtils;

/**
 * 差友联系人列表适配器
 * 
 * @author Administrator
 *
 */
public class UserContactAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<UserListForGroupDTO> userDatas;
	private List<UserListForGroupDTO> groupUsersList;
	private List<GroupUsersListDTO> responses;
	private int flag;
	private HomeActivity mActivity;
	private AQuery aq;
	private CallBack callBack;

	public UserContactAdapter(LayoutInflater inflater, Activity mActivity, List<UserListForGroupDTO> responses, int flag, CallBack callback) {
		this.inflater = inflater;
		this.userDatas = responses;
		this.flag = flag;
		this.callBack = callback;
		this.mActivity = (HomeActivity) mActivity;
		aq = new AQuery(mActivity);
		initData();
	}

	private void initData() {
		groupUsersList = new ArrayList<UserListForGroupDTO>();
		List<UserListForGroupDTO> users = userDatas;
		for (UserListForGroupDTO userList : users) {

			Integer userId = userList.getUserId();
			String userName = userList.getUserName();
			String realName = userList.getRealName();
			String email = userList.getEmail();
			String tel = userList.getTel();
			String phone = userList.getPhone();
			String photo = userList.getPhoto();

			StringUtils.handleNull(userName, realName, email, tel, phone, photo);
			groupUsersList.add(new UserListForGroupDTO(userId, userName, realName, email, tel, phone, photo));
		}

	}

	@Override
	public int getCount() {
		return groupUsersList.size();
	}

	@Override
	public Object getItem(int position) {
		return groupUsersList;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.list_context_item_child, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.tv_name_context_item);
			holder.imgHeader = (ImageView) view.findViewById(R.id.iv_head_context_item);
			holder.tvPhoneNum = (TextView) view.findViewById(R.id.tv_phone_context_item);
			holder.imgCall = (ImageView) view.findViewById(R.id.ib_call_context_item);
			holder.cbCheck = (CheckBox) view.findViewById(R.id.cb_choice_context_item);
			holder.ll_item = (LinearLayout) view.findViewById(R.id.linear_item);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tvName.setText(userDatas.get(position).getRealName());
		holder.tvPhoneNum.setText(userDatas.get(position).getPhone());
		aq.id(holder.imgHeader).image(userDatas.get(position).getPhoto(), true, true, 0, R.drawable.drawable);
		// ITaskApp.getInstance().getBitmpUtils().display(holder.imgHeader,
		// userDatas.get(position).getPhoto());
		showView(holder, position);
		// checkBox状态显示
		boolean nowStatus = groupUsersList.get(position).isCheck();
		holder.cbCheck.setChecked(nowStatus);
		// 点击进入联系人详情
		holder.ll_item.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserListForGroupDTO user = (UserListForGroupDTO) userDatas.get(position);
				ContextDetailFragment fragment = new ContextDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				bundle.putString("headString", userDatas.get(position).getPhoto());
				// fragment.setArguments(bundle);
				// ((HomeActivity) mActivity).replaceFragmentBack(fragment);
				mActivity.startActivity(new Intent(mActivity, ContextDetailsActivity.class).putExtras(bundle));
			}
		});
		// 点击拨打联系人电话
		holder.imgCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(callBack != null){
					callBack.callBackMethod(v, position, userDatas.get(position).getPhone());
				}
			}
		});
		return view;
	}

	/** 根据传过来的flag判断显示控件，1代表显示checkBox控件，2代表显示拨打电话控件 */
	private void showView(ViewHolder holder, int p) {
		if (userDatas.get(p).getPhone() == null) {
			holder.imgCall.setVisibility(View.GONE);
		} else {
			holder.imgCall.setVisibility(View.VISIBLE);
		}
	}

	private class ViewHolder {
		private ImageView imgHeader;
		private TextView tvName;
		private TextView tvPhoneNum;
		private ImageView imgCall;
		private CheckBox cbCheck;
		private LinearLayout ll_item;
	}
	
	public interface CallBack{
		abstract void callBackMethod(View v, int p, String phone);
	}
}
