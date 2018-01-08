package com.xxdc.itask.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.lidroid.xutils.ViewUtils;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.dialog.LoadingDialog;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.FileResponse;
import com.xxdc.itask.entity.FileURLS;

public abstract class BaseActivity extends FragmentActivity {
	
	public static final String TOKEN_PAST = "token_past";
	
	/** 从图库选取图片 */
	public static final int SELECT_PICTRUE = 1000;

	/** 拍照 */
	public static final int SELECT_PHOTO = 1001;

	public static final int PIC_TO_VIEW = 1002;
	/**
	 * 网络请求时loading
	 */
	protected LoadingDialog mLoadingDialog;
	
	public Activity activity;

	/**
	 * fragment管理
	 */
	protected FragmentManager mFragmentManager;

	private File mUploadImgFile= null;
	
	private String mUploadImgUrl = null;
	
	private String mUploadVoiceUrl = null;
	
	private TokenPastReceiver mTokenPastReciver;
	
	protected AQuery aq;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		activity = this;
		aq = new AQuery(activity);
	}
	

	
	protected void registerTokenPastReceiver() {
		/**token 过期广播*/
		if (mTokenPastReciver == null) {
			mTokenPastReciver = new TokenPastReceiver();
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(TOKEN_PAST);
		registerReceiver(mTokenPastReciver, filter);
	}
	
	protected void unregisterTokenPastReceiver() {
		unregisterReceiver(mTokenPastReciver);
	}

	protected abstract void initUI();

	/**
	 * 把viewUtils注入到activity里
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		ViewUtils.inject(this);
		mLoadingDialog = new LoadingDialog(this);
		mFragmentManager = getSupportFragmentManager();
		initUI();
	}

	/**
	 * 返回按钮处理
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// AppManager.getAppManager().finishActivity();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTokenPastReciver != null) {
			unregisterTokenPastReceiver();
		}
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case SELECT_PICTRUE:
			startPhotoZoom(intent.getData());
			break;
		case PIC_TO_VIEW:
			if (intent != null) {
				setPicToView(intent);
			}
			break;
		case SELECT_PHOTO:
			File file = new File(Environment.getExternalStorageDirectory(), "textphoto.jpg");
			Uri outputFileUri = Uri.fromFile(file);
			startPhotoZoom(outputFileUri);
			break;
		}
	}

	/**
	 * 裁剪图片
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.0);
		intent.putExtra("aspectY", 1.0);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PIC_TO_VIEW);
	}

	private void setPicToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			File imgFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath(), System.currentTimeMillis() + ".png");
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(imgFile);
				photo.compress(CompressFormat.PNG, 100, fos);
				mUploadImgFile = imgFile;
				selectPictrueComplete(imgFile);
//				doPostUploadFile(imgFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void selectPictrueComplete(File pictrueFile) {
	}

	protected void uploadFileComplete(String url) {
	}

	private void doPostUploadFile(File file) {
		ITaskApp.getInstance().getHttpClient().uploadFile(file, new Callback() {

			@Override
			public void onSuccess(Object o) {
				FileResponse response = (FileResponse) o;
				if (response.getResponse() != null) {
					uploadFileComplete(response.getResponse());
					mUploadImgUrl = response.getResponse();
					FileURLS.getFileURLS().add(mUploadImgUrl);
					mLoadingDialog.dismiss();
					Toast.makeText(BaseActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				Toast.makeText(BaseActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public void replaceFragment(int id, Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.replace(id, fragment);
		ft.commit();
	}

	public void replaceFragment(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.replace(android.R.id.tabcontent, fragment);
		
		ft.commit();
	}

	public void replaceFragmentBack(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		ft.replace(android.R.id.tabcontent, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public void hideFragment(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		ft.hide(fragment);
		ft.commit();
	}
	
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		ft.show(fragment);
		ft.commit();
	}

	public LoadingDialog getLoadingDialog() {
		return mLoadingDialog;
	}
	
	public FragmentManager getFM() {
		return mFragmentManager;
	}

	public File getUploadImgFile() {
		return mUploadImgFile;
	}

	public void cleanUploadImgFile() {
		mUploadImgFile = null;
	}
	
	public String getUploadImgUrl() {
		return mUploadImgUrl;
	}
	
	public void cleanUploadImgUrl() {
		mUploadImgUrl = null;
	}
	
	public String getUploadVoiceUrl() {
		return mUploadVoiceUrl;
	}
	
	public void cleanUploadVoiceUrl() {
		mUploadVoiceUrl = null;
	}
	
	class TokenPastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(TOKEN_PAST)) {
				LoginActivity.luanch(BaseActivity.this);
				finish();
			}
		}
		
	}
}
