package com.xxdc.itask.dialog;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.util.LogUtils;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.TaskAddActivity;
import com.xxdc.itask.media.RecordListener;
import com.xxdc.itask.service.RecorderService;
import com.xxdc.itask.util.ITask;

/**
 * 
 * @author fch
 * @date 2015年1月22日
 * @version 1.0
 */
public class RecorderDialog extends Dialog {
	private static final int RECORD_ON = 1; // 正在录音
	private static final int RECORD_OFF = 0; // 不在录音

	private ImageView mRecordImg;

	private TextView mMessage;

	private RecordCallback mCallback;

	private ImageButton mVoiceButton;

	private int mRecordState = 0; // 录音状态
	
	private float mDownY;
	
	private boolean mIsCancle = false;
	
	private File mRecordFile = null;

	public RecorderDialog(Context context, RecordCallback callback) {
		super(context, R.style.MyAlertDialog);
		this.mCallback = callback;
		setContentView(R.layout.recorder_dialog);
		initView();
	}

	/**
	 * @param context
	 * @param voiceButton
	 * 控制按钮
	 */
	public RecorderDialog(Context context) {
		super(context, R.style.MyAlertDialog);
		setContentView(R.layout.recorder_dialog);
		initView();
	}

	/**
	 * @param context
	 * @param voiceButton
	 *            控制按钮
	 */
	public RecorderDialog(Context context, View voiceButton, RecordCallback callback) {
		super(context, R.style.MyAlertDialog);
		setContentView(R.layout.recorder_dialog);
		mVoiceButton = (ImageButton) voiceButton;
		mCallback = callback;
		initView();
	}

	public interface RecordCallback {
		void complete(File file);

		void cancle();

	}

	@SuppressLint("ClickableViewAccessibility")
	private void initView() {
		mRecordImg = (ImageView) findViewById(R.id.record_dialog_img);
		mMessage = (TextView) findViewById(R.id.record_dialog_txt);
		mVoiceButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (mRecordState != RECORD_ON) {
						mDownY = event.getY();
						mRecordState = RECORD_ON;
						if (mRecordFile != null) {
							mRecordFile.delete();
						}
						mRecordFile = new File(getContext().getExternalFilesDir(Config.RECORDER), System.currentTimeMillis() + ".amr");
						LogUtils.i(mRecordFile.getAbsolutePath());
						mCallback.complete(mRecordFile);
						RecorderService.startRecording(getContext(), MediaRecorder.AudioSource.MIC, mRecordFile.getAbsolutePath(), false);
						show();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					float moveY = event.getY();
					if (mDownY - moveY > 50) {
						mMessage.setText(R.string.release_cancel_record);
						mIsCancle = true;
					} else if (mDownY - moveY < 20) {
						mMessage.setText(R.string.move_cacel_record);
						mIsCancle = false;
					}
					LogUtils.i(moveY - mDownY + "");
					break;
				case MotionEvent.ACTION_UP:
					if (mRecordState == RECORD_ON) {
						mRecordState = RECORD_OFF;
						if (isShowing()) {
							dismiss();
						}
						RecorderService.stopRecording(getContext());
					}
					
					break;
				}

				return false;
			}
		});
		ITaskApp.getInstance().setRecordListener(mRecordListener);
	}

	// 录音Dialog图片随声音大小切换
	void setDialogImage(double voiceValue) {
		if (voiceValue < 600.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 600.0 && voiceValue < 1000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 1000.0 && voiceValue < 1200.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 1200.0 && voiceValue < 1400.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1400.0 && voiceValue < 1600.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 1600.0 && voiceValue < 1800.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 1800.0 && voiceValue < 2000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 2000.0 && voiceValue < 3000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 3000.0 && voiceValue < 4000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 4000.0 && voiceValue < 6000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 6000.0 && voiceValue < 8000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 8000.0 && voiceValue < 10000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 10000.0 && voiceValue < 12000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 12000.0) {
			mRecordImg.setImageResource(R.drawable.record_animate_14);
		}
	}
	
	public File getRecordFile() {
		return mRecordFile;
	}

	private RecordListener mRecordListener = new RecordListener() {

		@Override
		public void onAmplitude(int amplitude) {
			setDialogImage(amplitude);
		}

		@Override
		public void stop() {
			LogUtils.i(mIsCancle + "");
			if (mIsCancle) {
				mRecordFile.delete();
				mCallback.cancle();
			} else {
				mCallback.complete(mRecordFile);
			}
		}
	};
}
