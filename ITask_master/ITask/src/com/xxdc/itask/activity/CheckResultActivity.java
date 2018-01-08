package com.xxdc.itask.activity;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.UserListForGroupDTO;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 查找结果
 * 
 * @author Administrator
 *
 */
public class CheckResultActivity extends BaseActivity {
	@ViewInject(R.id.tv_context_name)
	private TextView mUsername;
	@ViewInject(R.id.tv_context_phone)
	private TextView mUserphone;
	@ViewInject(R.id.tv_add_linkman)
	private LinearLayout mAdd;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.context_result_activity);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void initUI() {
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			ArrayList<?> list = bundle.getParcelableArrayList("check");
			List<UserListForGroupDTO> search = (List<UserListForGroupDTO>) list;
			mUsername.setText(search.get(0).getRealName());
			mUserphone.setText(search.get(0).getPhone());
		}
	}

	@OnClick(R.id.tv_add_linkman)
	private void onClickAdd(View v) {
		Toast.makeText(activity, "好友请求已发送,等待验证", Toast.LENGTH_SHORT).show();
		activity.finish();
	}

}
