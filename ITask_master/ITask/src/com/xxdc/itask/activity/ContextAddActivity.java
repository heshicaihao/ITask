package com.xxdc.itask.activity;

import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.GroupUsersListDTO;
import com.xxdc.itask.dto.UserListForGroupDTO;
import com.xxdc.itask.dto.response.GroupUsersListResponse;
import com.xxdc.itask.util.StringUtils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 添加新的联系人
 * 
 * @author Administrator
 *
 */
public class ContextAddActivity extends BaseActivity {
	@ViewInject(R.id.tv_find_linkman)
	private LinearLayout mAddLinkman;
	@ViewInject(R.id.et_userName_con_add)
	private EditText mNickname;
	private ProgressDialog mPostDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.context_add_activity);
	}

	@Override
	protected void initUI() {
		mPostDialog = new ProgressDialog(activity);
		mPostDialog.setMessage("正在查找...");
		mPostDialog.setCancelable(false);
		mNickname.setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
		Bundle bundle = getIntent().getExtras();
		GroupUsersListResponse intentResponse = (GroupUsersListResponse) bundle.getSerializable("users");
		setListener(intentResponse.getResponse());

	}

	private void setListener(List<GroupUsersListDTO> responses) {
		mAddLinkman.setOnClickListener(new MySearchListener(responses));
	}

	class MySearchListener implements OnClickListener {
		private List<GroupUsersListDTO> responses;

		public MySearchListener(List<GroupUsersListDTO> responses) {
			this.responses = responses;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) {
			String text = mNickname.getText().toString().trim();
			if (text.equals("")) {
				Toast.makeText(activity, "请输入搜索条件", Toast.LENGTH_SHORT).show();
				return;
			}
			List<UserListForGroupDTO> searchResult = getUsers(responses, text);
			LogUtils.i("searchResult" + searchResult);
			if (searchResult == null || searchResult.size() < 1) {
				Toast.makeText(activity, "没有查找到相应用户,请检查搜索条件", Toast.LENGTH_SHORT).show();
				return;
			} else {
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("check", (ArrayList<? extends Parcelable>) searchResult);
				startActivity(new Intent(activity, CheckResultActivity.class).putExtras(bundle));
				activity.finish();
			}

		}
	}

	/**
	 * 比对输入的字符与已存在的用户名进行匹配
	 * 
	 * @param responses
	 * @param context
	 * @return
	 */
	private List<UserListForGroupDTO> getUsers(List<GroupUsersListDTO> responses, String context) {
		List<UserListForGroupDTO> resultData = new ArrayList<UserListForGroupDTO>();
		for (GroupUsersListDTO groupUsersLis : responses) {
			List<UserListForGroupDTO> users = groupUsersLis.getUserList();
			for (UserListForGroupDTO userList : users) {
				String realName = userList.getRealName();
				String tel = userList.getTel();
				String eml = userList.getEmail();
				if (null == realName) {
					realName = "";
				}
				if (null == tel) {
					tel = "";
				}
				if (null == eml) {
					eml = "";
				}
				if (context.equals(realName)) {
					Integer userId = userList.getUserId();
					String userName = userList.getUserName();
					String email = userList.getEmail();
					String phone = userList.getPhone();
					String photo = userList.getPhoto();
					StringUtils.handleNull(userName, realName, email, tel, phone, photo);
					resultData.add(new UserListForGroupDTO(userId, userName, realName, email, tel, phone, photo));
				}
			}
		}
		return resultData;
	}

}
