package com.xxdc.itask.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.dialog.PictureSelectorDialog;
import com.xxdc.itask.dialog.RecorderDialog;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.fragment.HomeFragment;
import com.xxdc.itask.service.PlayerService;
import com.xxdc.itask.util.FileUtils;
import com.xxdc.itask.util.StringUtils;

public class TaskAddActivity extends BaseActivity {
	public final static String TASK_PARENT = "task_parent";

	@ViewInject(R.id.task_content)
	private EditText mTaskContentEdit;

	@ViewInject(R.id.parent_text)
	private TextView mTaskParentText;

	@ViewInject(R.id.voice_btn)
	private ImageButton mVoiceButton;
	
	@ViewInject(R.id.voice_picture_layout)
	private LinearLayout mPictrueVoiceLayout;

	private PictureSelectorDialog mPictureSelector;

	private UserTaskDTO mUserTaskDTO;

	private RecorderDialog mRecorderDialog;

	public static void launch(Activity a, String taskParentId) {
		Intent i = new Intent(a, TaskAddActivity.class);
		i.putExtra(TASK_PARENT, taskParentId);
		a.startActivityForResult(i, HomeFragment.REFRESH_DATA);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.home_task_add);
	}

	@Override
	protected void initUI() {
		mUserTaskDTO = (UserTaskDTO) getIntent().getSerializableExtra(TASK_PARENT);
		if (mUserTaskDTO != null) {
			mTaskParentText.setText(mUserTaskDTO.getTaskName());
		}
		
		mRecorderDialog = new RecorderDialog(this, mVoiceButton, new RecorderDialog.RecordCallback() {
			
			@Override
			public void complete(File file) {
				//录音文件只能有一个
				View view = mPictrueVoiceLayout.getChildAt(0);
				if (view == null || view.getTag() == null) {
					ImageView imageView = (ImageView) LayoutInflater.from(TaskAddActivity.this).inflate(R.layout.task_add_select_file, null);
					LayoutParams params = new LayoutParams(80, 80); 
					params.leftMargin = 60;
					imageView.setImageResource(R.drawable.voice);
					imageView.setTag(file.getAbsolutePath());
					imageView.setOnClickListener(mVoicePlayListener);
					mPictrueVoiceLayout.addView(imageView, 0, params);
				} else if (view.getTag() != null){
					view.setTag(file.getAbsolutePath());
				}
			}
			
			@Override
			public void cancle() {
				
			}
		});
	}

	@OnClick(R.id.photo_img)
	private void clickPhoto(View v) {
		cleanUploadImgUrl();
		mPictureSelector = new PictureSelectorDialog(this, R.style.MyAlertDialog);
		mPictureSelector.show();
	}
	
	@Override
	protected void selectPictrueComplete(File pictrueFile) {
		ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.task_add_select_file, null);
		LayoutParams params = new LayoutParams(80, 80); 
		params.leftMargin = 60;
		mPictrueVoiceLayout.addView(imageView, params);
		ITaskApp.getInstance().getBitmpUtils().display(imageView, pictrueFile.getAbsolutePath());
	}

	@OnClick(R.id.add_sure_btn)
	private void clickAddTask(View v) {
		addSureTask();
	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	/**
	 * 添加事项
	 * 
	 * @param taskParentId
	 *            事项的父id,0表示根结点
	 */
	private void addSureTask() {
		String parentId = null;
		if (mUserTaskDTO != null) {
			parentId = String.valueOf(mUserTaskDTO.getUserTaskId());
		}
		LogUtils.i("父ID:" + parentId);
		String taskContent = mTaskContentEdit.getText().toString().trim();
		if (StringUtils.isEmpty(taskContent) && mRecorderDialog.getRecordFile() ==  null && getUploadImgFile() == null) {
			Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
			return;
		}
		String voice = null;
		if (mRecorderDialog.getRecordFile() != null) {
			voice = mRecorderDialog.getRecordFile().getName();
		}
		String photo = null;
		if (this.getUploadImgFile() != null) {
			photo = this.getUploadImgFile().getName();
		}
		doPostCommitTask(taskContent, parentId, voice, photo, FileUtils.base64ToString(mRecorderDialog.getRecordFile()), FileUtils.base64ToString(this.getUploadImgFile()));
	}

	/**
	 * 上传事项
	 * 
	 * @param content
	 * @param voice
	 * @param photo
	 */
	private void doPostCommitTask(String content, String parentTaskId, String voice, String photo, String voiceFile, String photoFile) {
		ITaskApp.getInstance().getHttpClient().createTask(content, parentTaskId, voice, photo, voiceFile, photoFile, new Callback() {

			@Override
			public void onSuccess(Object o) {
				// mShadeView.setVisibility(View.GONE);
				// mAddPopupWindow.dismiss();
				// doPostGetTaskList();
				cleanUploadImgFile();
				finish();
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFailure(Object o) {
			}
		});
	}
	
	private OnClickListener mVoicePlayListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			PlayerService.startPlay(TaskAddActivity.this, v.getTag().toString());
		}
	};
}
