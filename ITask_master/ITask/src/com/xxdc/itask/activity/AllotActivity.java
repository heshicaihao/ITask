package com.xxdc.itask.activity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.AllotGridViewAdapter;
import com.xxdc.itask.adapter.AllotUserContactAdapter;
import com.xxdc.itask.adapter.MyContactAdapter;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.GroupUsersListResponse;
import com.xxdc.itask.entity.Users;

public class AllotActivity extends BaseActivity {
	@ViewInject(R.id.ev_contact_allot_activity)
	private ExpandableListView mContactElv;
	@ViewInject(R.id.gv_contact_allot)
	private GridView mContactGv;
	@ViewInject(R.id.rb_user_groupname)
	private RadioGroup mUserGroup;
	@ViewInject(R.id.contact_list_view)
	private ListView mListView;
	GroupUsersListResponse response = null;
	List<String> users;
	MyContactAdapter adapter;
	private String taskId;
	RadioButton tempButton = null;
	List<String> groupList = null;
	AllotGridViewAdapter allotGridViewAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.allot_activity);
	}

	@Override
	protected void initUI() {

		doPostGetGroupUsers();
		// 从前面传过来的数据，格式为 "1:流量,29:远飞"
		// 监听项目分组的选中 刷新对应分组下的数据
		String idName = getIntent().getStringExtra("idName");
		taskId = getIntent().getStringExtra("taskId");
		if (taskId == null) {
			taskId = 0 + "";
		}
		users = new ArrayList<String>();
		System.out.println("idName:" + idName);
		if (null != idName) {
			String[] ins = idName.split(",");
			for (String string : ins) {
				users.add(string);
				System.out.println(">>>>>>>uses:" + users);
				Users.getUsers().add(string);// Users.getUsers()中存储的数据string格式为"1:流量"
			}
		}
		allotGridViewAdapter = new AllotGridViewAdapter(users, AllotActivity.this, mContactElv, 1, null, new AllotGridViewAdapter.CallBack() {

			@Override
			public void callBackMethod(View v, int p) {

			}
		});
		mContactGv.setAdapter(allotGridViewAdapter);
		if (Users.getUsers().size() > 0 && Users.getUsers().get(0).contains(":")) {
			System.out.println("users.size()" + users.size());
			allotGridViewAdapter.notifyDataSetChanged();
		} else {
			Users.getUsers().clear();
			users.clear();
		}

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

				mListView.setAdapter(new AllotUserContactAdapter(activity, response.getResponse().get(temp).getUserList(), 2,
						new AllotUserContactAdapter.CallBack() {
							@Override
							public void callBackMethod(View v, int p, String name) {
								if (null != name) {

									String[] ins = name.split(",");
									for (String string : ins) {
										users.add(string);
										Users.getUsers().add(string);// Users.getUsers()中存储的数据string格式为"1:流量"
									}
								}
								allotGridViewAdapter.notifyDataSetChanged();
							}
						}));
			}
		});
	}

	// 得到联系人列表 与差友界面统一
	private void doPostGetGroupUsers() {
		ITaskApp.getInstance().getHttpClient().getGroupUsers(new Callback() {
			@Override
			public void onSuccess(Object o) {
				mLoadingDialog.dismiss();
				response = (GroupUsersListResponse) o;
				System.out.println(response.getMessage());
				if (response.getResponse().size() > 0 && null != response && null != response.getResponse().get(0)
						&& null != response.getResponse().get(0).getUserList().get(0)) {
					// 动态添加准备数据
					groupList = new ArrayList<String>();
					for (int i = 0; i < response.getResponse().size(); i++) {
						groupList.add(response.getResponse().get(i).getGroupName());
					}

					initRadioGroup();
					mListView.setAdapter(new AllotUserContactAdapter(activity, response.getResponse().get(0).getUserList(), 2,
							new AllotUserContactAdapter.CallBack() {

								@Override
								public void callBackMethod(View v, int p, String name) {
									if (null != name) {
										String[] ins = name.split(",");
										for (String string : ins) {
											users.add(string);
											Users.getUsers().add(string);// Users.getUsers()中存储的数据string格式为"1:流量"
										}
									}
									allotGridViewAdapter.notifyDataSetChanged();
								}
							}));
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
			}
		});
	}

	/**
	 * 初始化radioGroup 分组名称
	 */
	private void initRadioGroup() {
		if (mUserGroup.getChildCount() > 0) {
			mUserGroup.check(tempButton.getId() - groupList.size() + 1);
			return;
		} else {
			if (groupList != null && groupList.size() > 0) {
				for (int i = 0; i < groupList.size(); i++) {
					tempButton = (RadioButton) activity.getLayoutInflater().inflate(R.layout.contact_my_radiobutton, null);
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

	/** 确定按钮点击 */
	@OnClick(R.id.tv_ok_allot)
	public void okClick(View v) {
		// 直接调用Users.getUsers()获取选中的联系人，然后调接口分配任务传到服务器即可
		System.out.println("目前剩余的users数为----》" + Users.getUsers().size());
		if (Users.getUsers().size() == 0) {
			Toast.makeText(this, "请先选择联系人", Toast.LENGTH_SHORT).show();
			return;
		}
		dopostArrTaskToUser();
	}

	private void dopostArrTaskToUser() {
		String userIds = "";
		List<String> idName = Users.getUsers();
		for (String in : idName) {
			String id = in.split(":")[0];
			userIds += "," + id;
		}
		// System.out.println("userIds:" + userIds);
		// System.out.println("userIds:" + userIds.substring(1));
		ITaskApp.getInstance().getHttpClient().arrangeTaskToUser(taskId, userIds.substring(1), new Callback() {

			@Override
			public void onSuccess(Object o) {
				mLoadingDialog.dismiss();
				AllotActivity.this.finish();
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				Toast.makeText(AllotActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		Users.getUsers().clear();
		super.onDestroy();
	}

	/** 展开所有列表 */
	private void expandGroup() {
		int num = mContactElv.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < num; i++) {
			mContactElv.expandGroup(i);
		}
	}
}
