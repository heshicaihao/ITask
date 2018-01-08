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
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.util.LogUtils;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.HomeActivity;
import com.xxdc.itask.dto.GroupUsersListDTO;
import com.xxdc.itask.dto.UserListForGroupDTO;
import com.xxdc.itask.dto.response.GroupUsersListResponse;
import com.xxdc.itask.entity.Users;
import com.xxdc.itask.fragment.ContextDetailFragment;
import com.xxdc.itask.util.FormatTools;
import com.xxdc.itask.util.StringUtils;

public class MyContactAdapter extends BaseExpandableListAdapter implements OnChildClickListener {
	private HomeActivity mActivity;
	private Activity context;
	private List<GroupUsersListDTO> responses;
	private List<GroupUsersListDTO> groupUsersList;
	private List<UserListForGroupDTO> tempUserLists;
	private List<List<UserListForGroupDTO>> userLists;
	private LayoutInflater inflater;
	private ExpandableListView mcontactEview;
	private GridView mContactGv;
	private int flag;// 1代表从首页传过来，2代表从记事本传过来
	private CheckBox mCheckBox;
	private ImageView mCallBt;
	private ImageView mChildIview;

	public MyContactAdapter(ExpandableListView mcontactEview, Activity mActivity, LayoutInflater inflater, List<GroupUsersListDTO> responses, int flag) {
		this.mcontactEview = mcontactEview;
		this.mActivity = (HomeActivity) mActivity;
		this.responses = responses;
		this.inflater = inflater;
		this.flag = flag;
		mcontactEview.setOnChildClickListener(this);
		initData();
		status();
	}

	public MyContactAdapter(ExpandableListView mcontactEview, GridView mContactGv, Activity mActivity, List<GroupUsersListDTO> responses, int flag) {
		this.mcontactEview = mcontactEview;
		this.mContactGv = mContactGv;
		this.responses = responses;
		context = mActivity;
		this.inflater = LayoutInflater.from(mActivity);
		this.flag = flag;
		initData();
		status();
	}

	/** 数据初始化 */
	public void initData() {
		groupUsersList = new ArrayList<GroupUsersListDTO>();
		userLists = new ArrayList<List<UserListForGroupDTO>>();
		for (GroupUsersListDTO groupUsersListDTO : responses) {
			Integer groupId = groupUsersListDTO.getGroupId();
			String groupName = groupUsersListDTO.getGroupName();
			String groupCode = groupUsersListDTO.getGroupCode();
			String parentGroup = groupUsersListDTO.getParentGroup();
			Integer createUser = groupUsersListDTO.getCreateUser();
			groupUsersList.add(new GroupUsersListDTO(groupId, groupName, groupCode, parentGroup, createUser));

			List<UserListForGroupDTO> users = groupUsersListDTO.getUserList();
			tempUserLists = new ArrayList<UserListForGroupDTO>();
			for (UserListForGroupDTO userList : users) {
				Integer userId = userList.getUserId();
				String userName = userList.getUserName();
				String realName = userList.getRealName();
				String email = userList.getEmail();
				String tel = userList.getTel();
				String phone = userList.getPhone();
				String photo = userList.getPhoto();

				StringUtils.handleNull(userName, realName, email, tel, phone, photo);
				tempUserLists.add(new UserListForGroupDTO(userId, userName, realName, email, tel, phone, photo));
			}
			userLists.add(tempUserLists);
		}
	}

	/** 设置不同群组中，属于同一个联系人的状态保持一致，即确保相同id的联系人在各群组状态保持一致*/
	public void status() {
		System.out.println("执行到这里, " + flag);

		if (flag == 1) {
			System.out.println("Users.getUsers()的长度：" + Users.getUsers());
			for (String s : Users.getUsers()) {
				for (List<UserListForGroupDTO> list : userLists) {
					for (UserListForGroupDTO u : list) {
						System.out.println("状态：" + (u.getUserId() == Integer.valueOf(s.split(":")[0])));
						if (u.getUserId() == Integer.valueOf(s.split(":")[0])) {
							u.setCheck(true);
						}
					}
				}
			}
		}
	}

	@Override
	public int getGroupCount() {
		return groupUsersList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return userLists.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupUsersList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return userLists.get(groupPosition).get(childPosition);
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
		return false;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_context_item_group, null);
		}
		TextView mGroupName = (TextView) convertView.findViewById(R.id.tv_groupName_context_item);
		mGroupName.setText(((GroupUsersListDTO) getGroup(groupPosition)).getGroupName());
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.list_context_item_child, null);
		mChildIview = (ImageView) convertView.findViewById(R.id.iv_head_context_item);
		TextView mRealName = (TextView) convertView.findViewById(R.id.tv_name_context_item);
		TextView mTelTview = (TextView) convertView.findViewById(R.id.tv_phone_context_item);
		mCallBt = (ImageView) convertView.findViewById(R.id.ib_call_context_item);
		mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_choice_context_item);
		showView();
		// checkBox状态显示
		boolean nowStatus = ((UserListForGroupDTO) getChild(groupPosition, childPosition)).isCheck();
		mCheckBox.setChecked(nowStatus);

		mCallBt.setFocusable(false);
		mRealName.setText(((UserListForGroupDTO) getChild(groupPosition, childPosition)).getRealName());
		mTelTview.setText(((UserListForGroupDTO) getChild(groupPosition, childPosition)).getTel());
		ITaskApp.getInstance().getBitmpUtils().display(mChildIview, ((UserListForGroupDTO) getChild(groupPosition, childPosition)).getPhoto());
		setListener(groupPosition, childPosition);

		return convertView;
	}

	/** 根据传过来的flag判断显示控件，1代表显示checkBox控件，2代表显示拨打电话控件 */
	private void showView() {
		if (flag == 1) {
			mCheckBox.setVisibility(View.VISIBLE);
			mCallBt.setVisibility(View.GONE);
		} else if (flag == 2) {
			mCheckBox.setVisibility(View.GONE);
			mCallBt.setVisibility(View.VISIBLE);
		}
	}

	/** 设置监听事件 */
	private void setListener(final int groupPosition, final int childPosition) {

		if (flag == 1) {
			final UserListForGroupDTO user = (UserListForGroupDTO) getChild(groupPosition, childPosition);
			final String idName = user.getUserId() + ":" + user.getRealName();
			// CheckBox监听
			mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						Users.addUsers(idName);
						System.out.println("Users.getUsers().size()**************>" + Users.getUsers().size());
//						mContactGv.setAdapter(new AllotGridViewAdapter(Users.getUsers(), context, mcontactEview, 1, userLists ));
					} else {
						Users.removeUsers(idName);
						System.out.println("Users.getUsers().size()**************>" + Users.getUsers().size());
//						mContactGv.setAdapter(new AllotGridViewAdapter(Users.getUsers(), context, mcontactEview, 2, userLists));
					}
					// 控制相同id的联系人，保证状态一致。即如果相同id号，当点击此id时，其他相同id的状态要变成与点击id的状态一致。
					int userId = user.getUserId();
					for (List<UserListForGroupDTO> lists : userLists) {
						for (UserListForGroupDTO users : lists) {
							int id = users.getUserId();
							String in = user.getUserId() + ":" + user.getRealName();
							if (userId == id) {
								users.setCheck(isChecked);
								if (!isChecked) {
									Users.removeUsers(idName);
									Users.removeUsers(in);
								}
							}
						}
					}
					((UserListForGroupDTO) getChild(groupPosition, childPosition)).setCheck(isChecked);
				}
			});
		} else if (flag == 2) {
			// 打电话按钮监听
			mCallBt.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.CALL");
					intent.setData(Uri.parse("tel:" + ((UserListForGroupDTO) getChild(groupPosition, childPosition)).getTel()));
					mActivity.startActivity(intent);
				}
			});
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	/** 联系人点击事件 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		UserListForGroupDTO user = (UserListForGroupDTO) getChild(groupPosition, childPosition);
		LogUtils.i("onChildClick");
		Drawable drawable = mChildIview.getDrawable();
		byte[] headByte = FormatTools.getInstance().Drawable2Bytes(drawable);
		ContextDetailFragment fragment = new ContextDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		bundle.putByteArray("headByte", headByte);
		fragment.setArguments(bundle);
		((HomeActivity) mActivity).replaceFragmentBack(fragment);
		return false;
	}
	
	
}
