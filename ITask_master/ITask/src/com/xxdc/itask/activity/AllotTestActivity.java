package com.xxdc.itask.activity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import com.devsmart.android.ui.HorizontalListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.GroupUsersListResponse;
import com.xxdc.itask.entity.Users;

public class AllotTestActivity extends BaseActivity {
	@ViewInject(R.id.gv_contact_allot)
	private GridView mContactGv;
	@ViewInject(R.id.hlv_group_name_allot)
	private HorizontalListView mContactGroupName;
	@ViewInject(R.id.lv_contact_allot)
	private ListView mContactInfo;
	List<String> users;
	private String taskId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.allottest_activity);
	}

	@Override
	protected void initUI() {
		doPostGetGroupUsers();

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
				Users.getUsers().add(string);
			}
		}

		if (Users.getUsers().size() > 0 && Users.getUsers().get(0).contains(":")) {
			System.out.println("users.size()" + users.size());
			// mContactGv.setAdapter(new AllotGridViewAdapter(users,
			// AllotTestActivity.this, mContactElv, 1, null));
		} else {
			Users.getUsers().clear();
			users.clear();
		}
	}

	private void doPostGetGroupUsers() {
		ITaskApp.getInstance().getHttpClient().getGroupUsers(new Callback() {
			@Override
			public void onSuccess(Object o) {
				mLoadingDialog.dismiss();
				GroupUsersListResponse response = (GroupUsersListResponse) o;
				System.out.println(response.getMessage());
				if (response.getResponse().size() > 0 && null != response && null != response.getResponse().get(0)
						&& null != response.getResponse().get(0).getUserList().get(0)) {
					System.out.println("---->" + response.getResponse().get(0).getUserList().get(0).getTel());
					// adapter = new MyContactAdapter(mContactElv, mContactGv,
					// AllotTestActivity.this, response.getResponse(), 1);
					// mContactElv.setAdapter(adapter);
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
				AllotTestActivity.this.finish();
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				Toast.makeText(AllotTestActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		Users.getUsers().clear();
		super.onDestroy();
	}
}
