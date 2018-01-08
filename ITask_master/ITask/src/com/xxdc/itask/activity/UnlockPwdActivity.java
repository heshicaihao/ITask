package com.xxdc.itask.activity;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.dto.LoginDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.CopyOfLoginResponse;
import com.xxdc.itask.dto.response.LoginResponse;
import com.xxdc.itask.util.PreferenceHelper;
import com.xxdc.lib.R;
import com.xxdc.lib.gusturelock.LockPatternUtils;
import com.xxdc.lib.gusturelock.LockPatternView;
import com.xxdc.lib.gusturelock.LockPatternView.Cell;

public class UnlockPwdActivity extends BaseActivity {
	@ViewInject(R.id.gesturepwd_unlock_lockview)
	private LockPatternView mLockPatternView;

	@ViewInject(R.id.gesturepwd_unlock_forget)
	private TextView mHeadTextView;
	
	@ViewInject(R.id.gesturepwd_unlock_text)
	private TextView mUnlockMessage;
	
	private CountDownTimer mCountdownTimer = null;

	private int mFailCount = 0;

	private Animation mShakeAnim;
	
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.gesture_unlock_activity);
	}

	@Override
	protected void initUI() {
		doPostLogin();
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
	}
	
	@OnClick(R.id.gesturepwd_unlock_forget)
	private void clickForget(View v) {
		ITaskApp.getInstance().getLockPatternUtils().clearLock();
		LoginActivity.luanch(this);
		finish();
	}
	
	

	private Runnable mClearPatternRunnable = new Runnable() {
		public void run() {
			mLockPatternView.clearPattern();
		}
	};

	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

		public void onPatternStart() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}

		public void onPatternCleared() {
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		public void onPatternDetected(List<LockPatternView.Cell> pattern) {
			if (pattern == null)
				return;
			if (ITaskApp.getInstance().getLockPatternUtils().checkPattern(pattern)) {
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
				HomeActivity.launch(UnlockPwdActivity.this);
				finish();
			} else {
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
					mFailCount++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailCount;
					if (retry > 0) {
						if (retry == 0) {
							Toast.makeText(UnlockPwdActivity.this, "您已5次输错密码，请30秒后再试", Toast.LENGTH_SHORT).show();
						}
						mUnlockMessage.setText("密码错误，还可以再输入" + retry + "次");
						mUnlockMessage.setTextColor(Color.RED);
						mUnlockMessage.startAnimation(mShakeAnim);
					}

				} else {
					mUnlockMessage.setText(R.string.lockpattern_recording_incorrect_too_short);
					mUnlockMessage.setTextColor(Color.RED);
					mUnlockMessage.startAnimation(mShakeAnim);
				}

				if (mFailCount >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
					mHandler.postDelayed(attemptLockout, 0);
				} else {
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		public void onPatternCellAdded(List<Cell> pattern) {

		}

		private void patternInProgress() {
		}
	};

	Runnable attemptLockout = new Runnable() {

		@Override
		public void run() {
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0) {
						mUnlockMessage.setText(secondsRemaining + " 秒后重试");
					} else {
						mUnlockMessage.setText("请绘制手势密码");
						mUnlockMessage.setTextColor(Color.WHITE);
					}
				}

				@Override
				public void onFinish() {
					mLockPatternView.setEnabled(true);
					mFailCount = 0;
				}
			}.start();
		}
	};
	
	private void doPostLogin() {
		String u = PreferenceHelper.readString(this, "login", "username");
		String p = PreferenceHelper.readString(this, "login", "pwd");
		ITaskApp.getInstance().getHttpClient().login(p, u, new Callback() {

			@Override
			public void onSuccess(Object o) {
				// 新接口 只返回一个token 不适用实体类存储
				String responseToken = ((CopyOfLoginResponse) o).getResponse();
				Config.token = responseToken;
			}

			@Override
			public void onStart() {
			}

			@Override
			public void onFailure(Object o) {
				Config.userId = PreferenceHelper.readLong(UnlockPwdActivity.this, "login", "userId", 1);
			}
		});
	}
}
