package com.xxdc.itask.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.GridImageAdapter;
import com.xxdc.itask.dialog.CustomAlertDialog;
import com.xxdc.itask.dialog.PictureSelectorDialog;
import com.xxdc.itask.dialog.RecorderDialog;
import com.xxdc.itask.dto.NotepadDTO;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 单个记事本详情
 * 
 * @author Administrator
 *
 */
public class NotepadItemDetailsActivity extends BaseActivity {
	@ViewInject(R.id.tv_title_note_item)
	TextView mContentTitle;
	@ViewInject(R.id.tv_content_item)
	TextView mContentText;
	@ViewInject(R.id.grid_img)
	GridView mGridImg;
	@ViewInject(R.id.tv_save_note_eidt)
	TextView mSave;
	@ViewInject(R.id.iv_close)
	ImageView mClose;
	@ViewInject(R.id.voice_icon)
	ImageButton mVoice;
	@ViewInject(R.id.voice_play)
	LinearLayout mLayout;
	@ViewInject(R.id.voice_time)
	TextView mVoiceTime;
	private List<Drawable> images;
	private PictureSelectorDialog mPictureSelector;
	private CustomAlertDialog mDialog;
	private File mRecorderFile = null;
	private RecorderDialog mRecorderDialog;
	private ArrayList<HashMap<String, String>> photo_list = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.notepad_item_details);
		initViews();
	}

	@Override
	protected void initUI() {
		Bundle bundle = getIntent().getExtras();
		images = new ArrayList<Drawable>();
		Drawable resources = activity.getResources().getDrawable(R.drawable.love_type_reporter);
		Drawable resourcesAdd = activity.getResources().getDrawable(R.drawable.add_photoed);
		images.add(resources);
		images.add(resources);
		images.add(resources);
		images.add(resourcesAdd);
		if (bundle != null) {
			NotepadDTO note = (NotepadDTO) bundle.getSerializable("note");
			mContentTitle.setText("标题:" + note.getTitle());
			mContentText.setText("内容:" + note.getContext());
			mGridImg.setAdapter(new GridImageAdapter(activity, mGridImg, images, new GridImageAdapter.CallBack() {

				@Override
				public void callBack(int position) {
					
				}
			}));
		}
		mGridImg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= 5) {
					Toast.makeText(activity, "最多可以添加5张图片哦", Toast.LENGTH_LONG).show();
					return;
				}
				if (position == images.size() - 1) {
					mPictureSelector = new PictureSelectorDialog(NotepadItemDetailsActivity.this, R.style.MyAlertDialog);
					mPictureSelector.show();
				}
				if (position < images.size() - 1) {
					Intent intent = new Intent(activity, ImagePagerActivity.class);
					intent.putExtra("photos", photo_list);
					intent.putExtra("position", position);
					startActivity(intent);
				}	

			}
		});

		mDialog = new CustomAlertDialog(activity);
		mDialog.setCancelable(false);
		mDialog.setOnNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.setOnPositiveButton("重录", new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				mRecorderFile = null;
				mVoiceTime.setText("");
				mVoice.setBackgroundResource(R.drawable.voice_blue_1);
				mClose.setVisibility(View.GONE);
			}
		});
		mRecorderDialog = new RecorderDialog(activity, mVoice, new RecorderDialog.RecordCallback() {

			@Override
			public void complete(File file) {
				mRecorderFile = file;
				mVoice.setBackgroundResource(R.drawable.voice_blue_3);
				mVoiceTime.setText("   20''");
				mVoiceTime.setVisibility(View.VISIBLE);
				mClose.setVisibility(View.VISIBLE);
			}

			@Override
			public void cancle() {

			}
		});

	}

	@OnClick(R.id.iv_close)
	private void mOnClose(View v) {
		if (mDialog != null) {
			mDialog.show();
		}
	}

	@OnClick(R.id.voice_play)
	private void mOnPlay(View v) {
		Toast.makeText(activity, "播放语音", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		System.out.println("requestCode=" + requestCode + "resultCode=" + resultCode + "intent=" + intent);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PIC_TO_VIEW:
			if (intent != null) {
				setPicToView(intent);
			}
			break;
		}
	}

	// 将进行剪裁后的图片显示到UI界面上
	@SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			images.add(images.size() - 1, drawable);
			mGridImg.setAdapter(new GridImageAdapter(activity, mGridImg, images, new GridImageAdapter.CallBack() {

				@Override
				public void callBack(int position) {

				}
			}));
		}
	}

	private void initViews() {
		for (int i = 0; i < 5; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("photo", com.xxdc.itask.Config.TestImageUrl);
			photo_list.add(map);
		}
	}

	@OnClick(R.id.tv_save_note_eidt)
	private void mOnSave(View v) {
		finish();
	}
}
