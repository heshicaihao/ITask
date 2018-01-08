package com.xxdc.itask.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.CopyOfLoginResponse;
import com.xxdc.itask.util.PreferenceHelper;
import com.xxdc.itask.util.StringUtils;

public class LoginActivity extends BaseActivity {

	/** 用户名 */
	@ViewInject(R.id.user_edit)
	private EditText mUserEdit;

	/** 密码 */
	@ViewInject(R.id.pwd_edit)
	private EditText mPwdEdit;

	public static void luanch(Context c) {
		Intent intent = new Intent(c, LoginActivity.class);
		c.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		checkGesture();
		setContentView(R.layout.login_activity);
	}

	@Override
	protected void initUI() {
		if (null != PreferenceHelper.readString(this, "login", "username")) {
			mUserEdit.setText(PreferenceHelper.readString(this, "login", "username"));
		} else {
			mUserEdit.setText("admin1");
		}
		if (null != PreferenceHelper.readString(this, "login", "pwd")) {
			mPwdEdit.setText(PreferenceHelper.readString(this, "login", "pwd"));
		} else {
			mPwdEdit.setText("123456");
		}
	}

	
	private void checkGesture() {
		if (ITaskApp.getInstance().getLockPatternUtils().savedPatternExists()) {
			startActivity(new Intent(this, UnlockPwdActivity.class));
			finish();
		}
	}

	/**
	 * 登录
	 * 
	 * @param v
	 */
	@OnClick(R.id.login_tv)
	private void clickLogin(View v) {
		String u = mUserEdit.getText().toString().trim();
		String p = mPwdEdit.getText().toString().trim();
		if (StringUtils.isEmpty(u)) {
			Toast.makeText(this, R.string.user_not_null, Toast.LENGTH_SHORT).show();
			return;
		}

		if (StringUtils.isEmpty(p)) {
			Toast.makeText(this, R.string.pwd_not_null, Toast.LENGTH_SHORT).show();
			return;
		}
		doPostLogin(u, p, false);
	}

	/**
	 * 忘记密码
	 * 
	 * @param v
	 */
	@OnClick(R.id.forget_pwd_text)
	private void clickForgetPwd(View v) {

	}

	/**
	 * 注册
	 * 
	 * @param v
	 */
	@OnClick(R.id.sign_up_text)
	private void clickSignUp(View v) {
		SignupActivity.launch(this);
	}

	/**
	 * 
	 * @param u
	 * @param p
	 * @param isSaveLockPwd
	 *            是否保存了手势密码
	 */
	private void doPostLogin(final String u, final String p, final boolean isSaveLockPwd) {
		PreferenceHelper.write(this, "login", "username", u);
		PreferenceHelper.write(this, "login", "pwd", p);
		ITaskApp.getInstance().getHttpClient().login(p, u, new Callback() {

			@Override
			public void onSuccess(Object o) {
				Toast.makeText(activity, "登陆成功", Toast.LENGTH_LONG).show();
				mLoadingDialog.dismiss();
				// 新接口 只返回一个token 不适用实体类存储
				String responseToken = ((CopyOfLoginResponse) o).getResponse();
				Config.token = responseToken;
				Config.user = u;
				Config.pwd = p;
				// 写进prefenrence 存储文件
				PreferenceHelper.write(LoginActivity.this, "login", "token", Config.token);
				PreferenceHelper.write(LoginActivity.this, "login", "username", Config.user);	
				PreferenceHelper.write(LoginActivity.this, "login", "pwd", Config.pwd);	
				LockSetupActivity.launch(LoginActivity.this);// 跳转操作
				LoginActivity.this.finish();
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				LockSetupActivity.launch(LoginActivity.this);// 跳转操作
				LoginActivity.this.finish();
			}
		});
	}

}
