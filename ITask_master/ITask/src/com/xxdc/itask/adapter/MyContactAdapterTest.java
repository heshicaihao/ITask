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
import com.xxdc.itask.fragment.ContextDetailFragment;
import com.xxdc.itask.util.FormatTools;
import com.xxdc.itask.util.StringUtils;

/** 此类会造成CheckBox错乱，暂时搁弃不用 */
public class MyContactAdapterTest extends BaseExpandableListAdapter implements OnChildClickListener {
	private HomeActivity mActivity;
	private Activity context;
	private GroupUsersListResponse response;
	private List<GroupUsersListDTO> groupUsersList;
	private List<UserListForGroupDTO> tempUserLists;
	private List<String> users;
	private List<List<UserListForGroupDTO>> userLists;
	private LayoutInflater inflater;
	private ExpandableListView mcontactEview;
	private GridView mContactGv;
	private ChildHolder childHolder;
	private int flag;// 1代表从首页传过来，2代表从记事本传过来

	public MyContactAdapterTest(ExpandableListView mcontactEview, Activity mActivity, LayoutInflater inflater, GroupUsersListResponse response, int flag) {
		this.mcontactEview = mcontactEview;
		this.mActivity = (HomeActivity) mActivity;
		this.response = response;
		this.inflater = inflater;
		this.flag = flag;
		mcontactEview.setOnChildClickListener(this);
		initData();
	}

	public MyContactAdapterTest(ExpandableListView mcontactEview, GridView mContactGv, Activity mActivity, GroupUsersListResponse response, int flag) {
		this.mcontactEview = mcontactEview;
		this.mContactGv = mContactGv;
		this.response = response;
		context = mActivity;
		this.inflater = LayoutInflater.from(mActivity);
		this.flag = flag;
		initData();
	}

	public void initData() {
		List<GroupUsersListDTO> groups = response.getResponse();
		groupUsersList = new ArrayList<GroupUsersListDTO>();
		users = new ArrayList<String>();
		userLists = new ArrayList<List<UserListForGroupDTO>>();
		for (GroupUsersListDTO groupUsersListDTO : groups) {
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
			System.out.println("tempUserLists:" + tempUserLists.size());
			System.out.println("userLists:" + userLists.size());
			System.out.println("groupUsersList:" + groupUsersList.size());
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
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		GroupHolder groupHolder = null;
		if (convertView == null) {
			groupHolder = new GroupHolder();
			convertView = inflater.inflate(R.layout.list_context_item_group, null);
			groupHolder.mGroupName = (TextView) convertView.findViewById(R.id.tv_groupName_context_item);
			convertView.setTag(groupHolder);
		} else {
			groupHolder = (GroupHolder) convertView.getTag();
		}
		groupHolder.mGroupName.setText(((GroupUsersListDTO) getGroup(groupPosition)).getGroupName());
		return convertView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		childHolder = null;
		if (convertView == null) {
			childHolder = new ChildHolder();
			convertView = inflater.inflate(R.layout.list_context_item_child, null);
			childHolder.mChildIview = (ImageView) convertView.findViewById(R.id.iv_head_context_item);
			childHolder.mRealName = (TextView) convertView.findViewById(R.id.tv_name_context_item);
			childHolder.mTelTview = (TextView) convertView.findViewById(R.id.tv_phone_context_item);
			childHolder.mCallBt = (ImageView) convertView.findViewById(R.id.ib_call_context_item);
			childHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_choice_context_item);
			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}

		showView();

		childHolder.mCallBt.setFocusable(false);
		childHolder.mRealName.setText(((UserListForGroupDTO) getChild(groupPosition, childPosition)).getRealName());
		childHolder.mTelTview.setText(((UserListForGroupDTO) getChild(groupPosition, childPosition)).getTel());
		ITaskApp.getInstance().getBitmpUtils().display(childHolder.mChildIview, ((UserListForGroupDTO) getChild(groupPosition, childPosition)).getPhoto());
		setListener(groupPosition, childPosition);

		return convertView;
	}

	/** 根据传过来的flag判断显示控件，1代表显示checkBox控件，2代表显示拨打电话控件 */
	private void showView() {
		if (flag == 1) {
			childHolder.mCheckBox.setVisibility(View.VISIBLE);
			childHolder.mCallBt.setVisibility(View.GONE);
		} else if (flag == 2) {
			childHolder.mCheckBox.setVisibility(View.GONE);
			childHolder.mCallBt.setVisibility(View.VISIBLE);
		}
	}

	/** 设置监听事件 */
	private void setListener(final int groupPosition, final int childPosition) {

		if (flag == 1) {
			final UserListForGroupDTO user = (UserListForGroupDTO) getChild(groupPosition, childPosition);
			final String idName = user.getUserId() +":"+ user.getRealName();
			// CheckBox监听
			childHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						users.add(idName);
						mContactGv.setAdapter(new AllotGridViewAdapter(users, context, 1, userLists));
					} else {
						users.remove(idName);
						mContactGv.setAdapter(new AllotGridViewAdapter(users, context, 2, userLists));
					}
				}
			});
		} else if (flag == 2) {
			// 打电话按钮监听
			childHolder.mCallBt.setOnClickListener(new View.OnClickListener() {
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
		Drawable drawable = childHolder.mChildIview.getDrawable();
		byte[] headByte = FormatTools.getInstance().Drawable2Bytes(drawable);
		ContextDetailFragment fragment = new ContextDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("user", user);
		bundle.putByteArray("headByte", headByte);
		fragment.setArguments(bundle);
		((HomeActivity) mActivity).replaceFragmentBack(fragment);
		return false;
	}

	class GroupHolder {
		TextView mGroupName;// 群组名
	}

	class ChildHolder {
		ImageView mChildIview;// 个人头像
		TextView mRealName;// 个人名字
		TextView mTelTview;// 个人电话
		CheckBox mCheckBox;// 选择框
		ImageView mCallBt;// 拨号按钮
	}
}
