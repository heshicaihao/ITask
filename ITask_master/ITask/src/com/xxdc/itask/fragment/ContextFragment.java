package com.xxdc.itask.fragment;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.ContextAddActivity;
import com.xxdc.itask.activity.FriendApplyActivity;
import com.xxdc.itask.activity.HomeActivity;
import com.xxdc.itask.adapter.MyContactAdapter;
import com.xxdc.itask.adapter.UserContactAdapter;
import com.xxdc.itask.adapter.UserContactAdapter.CallBack;
import com.xxdc.itask.dto.GroupUsersListDTO;
import com.xxdc.itask.dto.UserListForGroupDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.GroupUsersListResponse;
import com.xxdc.itask.entity.GroupUsersListResponseString;
import com.xxdc.itask.util.StringUtils;

public class ContextFragment extends BaseFragment {
	@ViewInject(R.id.ev_contact_context)
	private ExpandableListView mcontactEview;
	@ViewInject(R.id.et_search_context)
	private AutoCompleteTextView mSearchView;
	@ViewInject(R.id.ib_search_context)
	private ImageButton mSearchImgBtn;
	@ViewInject(R.id.rb_user_groupname)
	private RadioGroup mUserGroup;
	@ViewInject(R.id.contact_list_view)
	private ListView mListView;
	@ViewInject(R.id.layout_check)
	private LinearLayout mLayoutCheck;
	private LayoutInflater inflater;
	List<String> groupList = null;
	GroupUsersListResponse response = null;
	RadioButton tempButton = null;
	private PopupWindow callWindow;
	private int position;
	private String phoneNumber;

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i("onResume");
		doPostGetGroupUsers();
	}

	private void doPostGetGroupUsers() {

		ITaskApp.getInstance().getHttpClient().getGroupUsers(new Callback() {

			@Override
			public void onSuccess(Object o) {
				mLoadingDialog.dismiss();
				response = (GroupUsersListResponse) o;
				System.out.println("联系人列表返回:>>>>>" + response);
				if (null != response && response.getResponse().size() > 0 && null != response.getResponse().get(0)
						&& null != response.getResponse().get(0).getUserList().get(0)) {
					saveGroupToDB(response);
					// 动态添加准备数据
					groupList = new ArrayList<String>();
					for (int i = 0; i < response.getResponse().size(); i++) {
						groupList.add(response.getResponse().get(i).getGroupName());
						System.out.println(">>>>>>>>>联系人列表分组名" + response.getResponse().get(i).getGroupName());
					}

					initRadioGroup();

					mListView.setAdapter(new UserContactAdapter(inflater, mActivity, response.getResponse().get(0).getUserList(), 2, new CallBack() {

						@Override
						public void callBackMethod(View v, int p, String phone) {
							if (callWindow != null) {
								position = p;
								phoneNumber = phone;
								callWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

							}
						}
					}));
					System.out.println("---->" + response.getResponse().get(0).getUserList().get(0).getTel());
					mSearchView.setAdapter(new ArrayAdapter<String>(mActivity, R.layout.context_autocomplete_textview, getNameAndTel(response.getResponse())));
					mcontactEview.setAdapter(new MyContactAdapter(mcontactEview, mActivity, inflater, response.getResponse(), 2) {
					});
					setListener(response.getResponse());
					expandGroup();
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				response = fineNotepadsFormDB();
				if (null != response && response.getResponse().size() > 0 && null != response.getResponse().get(0)
						&& null != response.getResponse().get(0).getUserList().get(0)) {
					mSearchView.setAdapter(new ArrayAdapter<String>(mActivity, R.layout.context_autocomplete_textview, getNameAndTel(response.getResponse())));
					mcontactEview.setAdapter(new MyContactAdapter(mcontactEview, mActivity, inflater, response.getResponse(), 2));
					setListener(response.getResponse());
					expandGroup();
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		return initView(inflater, R.layout.context_fragment, container);
	}

	@Override
	protected void initUI(View v) {
		mSearchView.setThreshold(1);
		// 监听项目分组的选中 刷新对应分组下的数据
		mUserGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 注意不能直接使用get(checkedId - 1) 去取数据 因为这里checkedId 会增长 造成数组越界
				int temp;
				if (checkedId > groupList.size()) {
					temp = ((checkedId - groupList.size()) % groupList.size() - 1);
					if (temp == -1) {
						temp = groupList.size() - 1;
					}
				} else {
					temp = checkedId - 1;
				}
				System.out.println(">>>>>>>>checkedId" + checkedId + ">>>>>>groupList.size" + groupList.size() + ">>>>被选中的" + temp);
				mListView.setAdapter(new UserContactAdapter(inflater, mActivity, response.getResponse().get(temp).getUserList(), 2, new CallBack() {

					@Override
					public void callBackMethod(View v, int p, String phone) {
						if (callWindow != null) {
							position = p;
							phoneNumber = phone;
							callWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
						}

					}
				}));
			}
		});

		initCallPopwindow();

	}

	/**
	 * 初始化POP
	 */
	@SuppressWarnings("deprecation")
	private void initCallPopwindow() {
		View layout = inflater.inflate(R.layout.popwindow_call_selector, null);
		callWindow = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		callWindow.setFocusable(true);
		callWindow.setOutsideTouchable(true);
		callWindow.update();
		callWindow.setBackgroundDrawable(new BitmapDrawable());
		layout.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callWindow.isShowing() && callWindow != null) {
					callWindow.dismiss();
				}

			}
		});
		layout.findViewById(R.id.tv_call).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mActivity.startActivity(intent);

			}
		});
		layout.findViewById(R.id.tv_sms).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("smsto:" + phoneNumber);
				Intent it = new Intent(Intent.ACTION_SENDTO, uri);
				it.putExtra("sms_body", "");
				mActivity.startActivity(it);
			}
		});
	}

	/**
	 * 初始化radioGroup 分组名称
	 */
	private void initRadioGroup() {
		if (mUserGroup.getChildCount() > 0) {
			System.out.println(">>>>>>>>tempButton.getId" + tempButton.getId());
			mUserGroup.check(tempButton.getId() - groupList.size() + 1);
			return;
		} else {
			if (groupList != null && groupList.size() > 0) {
				for (int i = 0; i < groupList.size(); i++) {
					tempButton = (RadioButton) mActivity.getLayoutInflater().inflate(R.layout.contact_my_radiobutton, null);
					tempButton.setText(groupList.get(i));
					mUserGroup.addView(tempButton, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					// 设置默认第一个选项被选中 标准写法
					if (i == 0) {
						mUserGroup.check(tempButton.getId());
					}
				}
			}
		}
	}
	
	@OnClick(R.id.layout_check)
	private void onClickCheck(View v){
		startActivity(new Intent(mActivity, FriendApplyActivity.class));
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		tempButton = null;
		mUserGroup = null;
		super.onDestroy();
	}

	private void saveGroupToDB(GroupUsersListResponse response) {
		try {
			Gson gson = new Gson();
			System.out.println("GroupUsersListResponseString------>" + gson.toJson(response));
			GroupUsersListResponseString gulrs = new GroupUsersListResponseString();
			gulrs.setGroupUsers(gson.toJson(response));
			ITaskApp.getInstance().getDbUtils().saveOrUpdate(gulrs);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private GroupUsersListResponse fineNotepadsFormDB() {
		GroupUsersListResponse response = null;
		try {
			List<GroupUsersListResponseString> lists = ITaskApp.getInstance().getDbUtils().findAll(Selector.from(GroupUsersListResponseString.class));
			if (null != lists && lists.size() > 0) {
				String noteLists = lists.get(0).getGroupUsers();
				Gson gson = new Gson();
				response = (GroupUsersListResponse) gson.fromJson(noteLists, GroupUsersListResponse.class);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return response;
	}

	private void setListener(List<GroupUsersListDTO> responses) {
		mSearchImgBtn.setOnClickListener(new MySearchListener(responses));
	}

	private void expandGroup() {
		int num = mcontactEview.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < num; i++) {
			mcontactEview.expandGroup(i);
		}
	}

	private List<String> getNameAndTel(List<GroupUsersListDTO> responses) {
		List<String> tips = new ArrayList<String>();
		for (GroupUsersListDTO groupUsersLis : responses) {
			List<UserListForGroupDTO> users = groupUsersLis.getUserList();
			for (UserListForGroupDTO userList : users) {
				String tel = userList.getTel();
				String realName = userList.getRealName();
				if (null == realName) {
					realName = "";
				}
				if (null == tel) {
					tel = "";
				}
				tips.add(tel);
				tips.add(realName);
			}
		}
		for (String string : tips) {
			System.out.println("String: " + string);
		}
		return tips;
	}

	private List<UserListForGroupDTO> getSearchUsers(List<GroupUsersListDTO> responses, String text) {
		List<UserListForGroupDTO> search = new ArrayList<UserListForGroupDTO>();
		for (GroupUsersListDTO groupUsersLis : responses) {
			List<UserListForGroupDTO> users = groupUsersLis.getUserList();
			for (UserListForGroupDTO userList : users) {
				String realName = userList.getRealName();
				String tel = userList.getTel();
				if (null == realName) {
					realName = "";
				}
				if (null == tel) {
					tel = "";
				}
				if (realName.contains(text) || (tel.contains(text))) {
					Integer userId = userList.getUserId();
					String userName = userList.getUserName();
					String email = userList.getEmail();
					String phone = userList.getPhone();
					String photo = userList.getPhoto();
					StringUtils.handleNull(userName, realName, email, tel, phone, photo);
					search.add(new UserListForGroupDTO(userId, userName, realName, email, tel, phone, photo));

				}
			}
		}
		return search;
	}

	class MySearchListener implements OnClickListener {
		private List<GroupUsersListDTO> responses;

		public MySearchListener(List<GroupUsersListDTO> responses) {
			this.responses = responses;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			String text = mSearchView.getText().toString().trim().replaceAll(" ", "");
			List<UserListForGroupDTO> search = getSearchUsers(responses, text);
			LogUtils.i("search.size()" + search.size());
			ContextSearchFragment fragment = new ContextSearchFragment();
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("search", (ArrayList<? extends Parcelable>) search);
			fragment.setArguments(bundle);
			((HomeActivity) mActivity).replaceFragmentBack(fragment);
		}
	}

	@OnClick(R.id.iv_addcont_contect)
	private void addContactClick(View v) {
		Intent intent = new Intent(mActivity, ContextAddActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("users", response);
		mActivity.startActivity(intent.putExtras(bundle));
		// ContextAddFragment fragment = new ContextAddFragment();
		// ((HomeActivity) mActivity).replaceFragmentBack(fragment);
	}
}
