package com.xxdc.itask.service;

import java.io.File;
import java.io.IOException;

import com.lidroid.xutils.util.LogUtils;
import com.xxdc.itask.Config;
import com.xxdc.itask.media.PlayerEngin;
import com.xxdc.itask.util.StringUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.IBinder;

public class PlayerService extends Service {
	public static final String ACTION_PLAY = "play";

	public static final String ACTION_STOP = "stop";

	public static final String PARAM_NAME = "name";

	private MediaPlayer mPlayer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			// 执行onStartCommand方法后，服务异常被kill掉不会重启服务
			return super.onStartCommand(intent, flags, startId);
		}
		String action = intent.getAction();
		if (action.equals(ACTION_STOP)) {
			stopSelfResult(startId);
		} else if (action.equals(ACTION_PLAY)) {
			play(intent.getExtras().getString(PARAM_NAME));
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public static void startPlay(Context c, String url) {
		Intent intent = new Intent(c, PlayerService.class);
		intent.putExtra(PARAM_NAME, url);
		intent.setAction(ACTION_PLAY);
		c.startService(intent);
	}

	private void play(String url) {
		if (StringUtils.isEmpty(url)) {
			return;
		}
		try {
			LogUtils.i("url:" + url);
			if (mPlayer == null) {
				mPlayer = new MediaPlayer();
			} else {
				mPlayer.reset();
			}
			if (mPlayer != null) {
				mPlayer.release();
				mPlayer = null;
			}
			mPlayer = new MediaPlayer();
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					mPlayer.release();
					mPlayer = null;
				}	
			});
			mPlayer.setDataSource(url);
			mPlayer.prepare();
			mPlayer.start();
			
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					mp = null;
				}
			});
			
			mPlayer.setOnErrorListener(new OnErrorListener() {
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					mp.release();
					mp = null;
					return false;
				}
			});
			 
		} catch (Exception e) {
			mPlayer.release();
			mPlayer = null;
			e.printStackTrace();
		} 
	}
}
