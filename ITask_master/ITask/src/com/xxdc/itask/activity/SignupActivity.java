package com.xxdc.itask.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.Response;
import com.xxdc.itask.util.StringUtils;

public class SignupActivity extends BaseActivity {

	/** 用户名 */
	@ViewInject(R.id.user_edit)
	private EditText mUserEdit;
	
	/** 真实姓名 */
	@ViewInject(R.id.real_name_edit)
	private EditText mRealNameEdit;

	/** 密码 */
	@ViewInject(R.id.pwd_edit)
	private EditText mPwdEdit;

	/** 确认密码 */
	@ViewInject(R.id.sure_pwd_edit)
	private EditText mSurePwdEdit;
	
	/**
	 * 启动注册activity
	 */
	public static void launch(Context c) {
		Intent intent = new Intent(c, SignupActivity.class);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.register_activity);
	}

	@Override
	protected void initUI() {

	}

	/**
	 * 注册
	 * 
	 * @param v
	 */
	@OnClick(R.id.signup_tv)
	private void clickSignup(View v) {
		String u = mUserEdit.getText().toString().trim();
		String realName = mRealNameEdit.getText().toString().trim();
		String p = mPwdEdit.getText().toString().trim();
		String p1 = mSurePwdEdit.getText().toString().trim();
		
		if (StringUtils.isEmpty(u)) {
			Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (StringUtils.isEmpty(realName)) {
			Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (StringUtils.isEmpty(p)) {
			Toast.makeText(this, R.string.pwd_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (!p.equals(p1)) {
			Toast.makeText(this, R.string.pwd_entered_differ, Toast.LENGTH_SHORT).show();
			return;
		}
		
		doPostCommitSignup(u, p,realName);
	}
	
	/**
	 * 提交注册
	 * @param u
	 * @param p
	 */
	private void doPostCommitSignup(String u, String p,String realName) {
		ITaskApp.getInstance().getHttpClient().signup(u, p,realName, new Callback() {

			@Override
			public void onSuccess(Object o) {
				Response response = (Response) o;
				Toast.makeText(SignupActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
				if (response.getStatus()) {
					SignupActivity.this.finish();
				}
				mLoadingDialog.dismiss();
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
}
