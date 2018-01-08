/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xxdc.itask.service;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.io.IOException;

import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.media.RecordListener;
import com.xxdc.itask.media.Recorder;

public class RecorderService extends Service implements MediaRecorder.OnErrorListener {

	public final static String ACTION_NAME = "action_type";

	public final static int ACTION_INVALID = 0;

	public final static int ACTION_START_RECORDING = 1;

	public final static int ACTION_STOP_RECORDING = 2;

	public final static int ACTION_ENABLE_MONITOR_REMAIN_TIME = 3;

	public final static int ACTION_DISABLE_MONITOR_REMAIN_TIME = 4;

	public final static String ACTION_PARAM_FORMAT = "format";

	public final static String ACTION_PARAM_PATH = "path";

	public final static String ACTION_PARAM_HIGH_QUALITY = "high_quality";

	public final static String ACTION_PARAM_MAX_FILE_SIZE = "max_file_size";

	public final static String RECORDER_SERVICE_BROADCAST_NAME = "com.android.soundrecorder.broadcast";

	public final static String RECORDER_SERVICE_BROADCAST_STATE = "is_recording";

	public final static String RECORDER_SERVICE_BROADCAST_ERROR = "error_code";

	public final static int NOTIFICATION_ID = 62343234;

	private static MediaRecorder mRecorder = null;

	private static String mFilePath = null;

	private static long mStartTime = 0;

	// private RemainingTimeCalculator mRemainingTimeCalculator;

	private NotificationManager mNotifiManager;

	private Notification mLowStorageNotification;

	private TelephonyManager mTeleManager;

	private WakeLock mWakeLock;

	private KeyguardManager mKeyguardManager;
	
	private RecordListener mRecordListener;

	private final PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state != TelephonyManager.CALL_STATE_IDLE) {
				localStopRecording();
			}
		}
	};

	private final Handler mHandler = new Handler();

	/**
	 * 声波大小
	 */
	private Runnable mAmplitudeRunnable = new Runnable() {
		@Override
		public void run() {
			if (mRecordListener != null) {
				mRecordListener.onAmplitude(getAmplitude());
				mHandler.postDelayed(this, 150);
			}
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
		mRecorder = null;
		mLowStorageNotification = null;
		mNotifiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mTeleManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SoundRecorder");
		mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		
		mRecordListener = ITaskApp.getInstance().getRecordListener();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle bundle = intent.getExtras();
		if (bundle != null && bundle.containsKey(ACTION_NAME)) {
			switch (bundle.getInt(ACTION_NAME, ACTION_INVALID)) {
			case ACTION_START_RECORDING:
				localStartRecording(bundle.getInt(ACTION_PARAM_FORMAT), bundle.getString(ACTION_PARAM_PATH), bundle.getBoolean(ACTION_PARAM_HIGH_QUALITY));
				break;
			case ACTION_STOP_RECORDING:
				localStopRecording();
				break;
			default:
				break;
			}
			return START_STICKY;
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		mTeleManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		if (mWakeLock.isHeld()) {
			mWakeLock.release();
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLowMemory() {
		localStopRecording();
		super.onLowMemory();
	}
	
	/**
	 * 开始录音
	 * @param outputfileformat
	 * @param path
	 * @param highQuality
	 * @param maxFileSize
	 */
	private void localStartRecording(int outputfileformat, String path, boolean highQuality) {
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			if (outputfileformat == MediaRecorder.OutputFormat.THREE_GPP) {
				mRecorder.setAudioSamplingRate(highQuality ? 44100 : 22050);
				mRecorder.setOutputFormat(outputfileformat);
				mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
			} else {
				mRecorder.setAudioSamplingRate(highQuality ? 16000 : 8000);
				mRecorder.setOutputFormat(outputfileformat);
				mRecorder.setAudioEncoder(highQuality ? MediaRecorder.AudioEncoder.AMR_WB : MediaRecorder.AudioEncoder.AMR_NB);
			}
			mRecorder.setOutputFile(path);
			mRecorder.setOnErrorListener(this);

			// Handle IOException
			try {
				mRecorder.prepare();
			} catch (IOException exception) {
				mRecorder.reset();
				mRecorder.release();
				mRecorder = null;
				return;
			}
			// Handle RuntimeException if the recording couldn't start
			try {
				mRecorder.start();
				mHandler.removeCallbacks(mAmplitudeRunnable);
				mHandler.postDelayed(mAmplitudeRunnable, 150);
			} catch (RuntimeException exception) {
				AudioManager audioMngr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				boolean isInCall = (audioMngr.getMode() == AudioManager.MODE_IN_CALL);
				if (isInCall) {
					sendErrorBroadcast(Recorder.IN_CALL_RECORD_ERROR);
				} else {
					sendErrorBroadcast(Recorder.INTERNAL_ERROR);
				}
				mRecorder.reset();
				mRecorder.release();
				mRecorder = null;
				return;
			}
			mFilePath = path;
			mStartTime = System.currentTimeMillis();
			mWakeLock.acquire();
		}
	}

	private void localStopRecording() {
		if (mRecorder != null) {
			try {
				mRecorder.stop();
			} catch (RuntimeException e) {
			}
			mRecorder.release();
			mRecorder = null;
			mHandler.removeCallbacks(mAmplitudeRunnable);
			if (mRecordListener != null) {
				mRecordListener.stop();
			}
//			sendStateBroadcast();
//			showStoppedNotification();
		}
		stopSelf();
	}

	private void showRecordingNotification() {
		// Notification notification = new
		// Notification(R.drawable.stat_sys_call_record,
		// getString(R.string.notification_recording),
		// System.currentTimeMillis());
		// notification.flags = Notification.FLAG_ONGOING_EVENT;
		// PendingIntent pendingIntent;
		// pendingIntent = PendingIntent
		// .getActivity(this, 0, new Intent(this, SoundRecorder.class), 0);
		//
		// notification.setLatestEventInfo(this, getString(R.string.app_name),
		// getString(R.string.notification_recording), pendingIntent);
		//
		// startForeground(NOTIFICATION_ID, notification);
	}

	private void showLowStorageNotification(int minutes) {
		if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
			// it's not necessary to show this notification in lock-screen
			return;
		}

		// if (mLowStorageNotification == null) {
		// mLowStorageNotification = new
		// Notification(R.drawable.stat_sys_call_record_full,
		// getString(R.string.notification_recording),
		// System.currentTimeMillis());
		// mLowStorageNotification.flags = Notification.FLAG_ONGOING_EVENT;
		// }
		//
		// PendingIntent pendingIntent;
		// pendingIntent = PendingIntent
		// .getActivity(this, 0, new Intent(this, SoundRecorder.class), 0);
		//
		// mLowStorageNotification.setLatestEventInfo(this,
		// getString(R.string.app_name),
		// getString(R.string.notification_warning, minutes), pendingIntent);
		// startForeground(NOTIFICATION_ID, mLowStorageNotification);
	}

	private void showStoppedNotification() {
		// stopForeground(true);
		// mLowStorageNotification = null;
		//
		// Notification notification = new
		// Notification(R.drawable.stat_sys_call_record,
		// getString(R.string.notification_stopped),
		// System.currentTimeMillis());
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// Intent intent = new Intent(Intent.ACTION_VIEW);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.setType("audio/*");
		// intent.setDataAndType(Uri.fromFile(new File(mFilePath)), "audio/*");
		//
		// PendingIntent pendingIntent;
		// pendingIntent = PendingIntent.getActivity(this, 0, intent,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		//
		// notification.setLatestEventInfo(this, getString(R.string.app_name),
		// getString(R.string.notification_stopped), pendingIntent);
		// mNotifiManager.notify(NOTIFICATION_ID, notification);
	}

	private void sendStateBroadcast() {
		Intent intent = new Intent(RECORDER_SERVICE_BROADCAST_NAME);
		intent.putExtra(RECORDER_SERVICE_BROADCAST_STATE, mRecorder != null);
		sendBroadcast(intent);
	}

	private void sendErrorBroadcast(int error) {
		Intent intent = new Intent(RECORDER_SERVICE_BROADCAST_NAME);
		intent.putExtra(RECORDER_SERVICE_BROADCAST_ERROR, error);
		sendBroadcast(intent);
	}

	private void updateRemainingTime() {
		// long t = mRemainingTimeCalculator.timeRemaining();
		// if (t <= 0) {
		// localStopRecording();
		// return;
		// } else if (t <= 1800
		// && mRemainingTimeCalculator.currentLowerLimit() !=
		// RemainingTimeCalculator.FILE_SIZE_LIMIT) {
		// // less than half one hour
		// showLowStorageNotification((int) Math.ceil(t / 60.0));
		// }
		//
		// if (mRecorder != null && mNeedUpdateRemainingTime) {
		// mHandler.postDelayed(mUpdateRemainingTime, 500);
		// }
	}

	public static boolean isRecording() {
		return mRecorder != null;
	}

	public static String getFilePath() {
		return mFilePath;
	}

	public static long getStartTime() {
		return mStartTime;
	}

	public static void startRecording(Context context, int outputfileformat, String path, boolean highQuality) {
		Intent intent = new Intent(context, RecorderService.class);
		intent.putExtra(ACTION_NAME, ACTION_START_RECORDING);
		intent.putExtra(ACTION_PARAM_FORMAT, outputfileformat);
		intent.putExtra(ACTION_PARAM_PATH, path);
		intent.putExtra(ACTION_PARAM_HIGH_QUALITY, highQuality);
		context.startService(intent);
	}

	public static void stopRecording(Context context) {
		Intent intent = new Intent(context, RecorderService.class);
		intent.putExtra(ACTION_NAME, ACTION_STOP_RECORDING);
		context.startService(intent);
	}

	public static int getMaxAmplitude() {
		return mRecorder == null ? 0 : mRecorder.getMaxAmplitude();
	}

	@Override
	public void onError(MediaRecorder mr, int what, int extra) {
		sendErrorBroadcast(Recorder.INTERNAL_ERROR);
		localStopRecording();
	}
	
	private int getAmplitude() {
		if (mRecorder != null) {
			return mRecorder.getMaxAmplitude();
		} else {
			return 0;
		}
	}
}
