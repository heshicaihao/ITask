package com.xxdc.itask;

import android.app.Application;
import android.graphics.Bitmap;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.xxdc.itask.dto.net.MyHttpCilent;
import com.xxdc.itask.dto.net.MyHttpCilentImpl;
import com.xxdc.itask.media.RecordListener;
import com.xxdc.lib.gusturelock.LockPatternUtils;

public class ITaskApp extends Application {

	/**
	 * 单例模式
	 */
	private static ITaskApp instance;
	
	private LockPatternUtils mLockPatternUtils;

	private MyHttpCilent mHttp;

	private BitmapUtils bitmapUtils;

	private DbUtils mDbUtils;
	
	private RecordListener mRecordListener;
	
	public boolean mNetConnect = true;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		mHttp = new MyHttpCilentImpl(this);
		bitmapUtils = new BitmapUtils(this);
		bitmapUtils.configDefaultLoadingImage(R.drawable.drawable);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.drawable);// 加载失败图片
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_8888);// 设置图片压缩类型
		mDbUtils = DbUtils.create(this, Config.DB_NAME);
		mDbUtils.configAllowTransaction(true);
		mLockPatternUtils = new LockPatternUtils(this);
	}

	public static ITaskApp getInstance() {
		return instance;
	}

	public MyHttpCilent getHttpClient() {
		return mHttp;
	}

	public BitmapUtils getBitmpUtils() {
		return bitmapUtils;
	}

	public DbUtils getDbUtils() {
		return mDbUtils;
	}
	
	public void setRecordListener(RecordListener listener) {
		mRecordListener = listener;
	}
	
	public RecordListener getRecordListener() {
		return mRecordListener;
	}
	
	public LockPatternUtils getLockPatternUtils() {
		return mLockPatternUtils;
	}
}
