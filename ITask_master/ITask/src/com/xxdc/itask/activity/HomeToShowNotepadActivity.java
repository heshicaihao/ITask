package com.xxdc.itask.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.GridNomalAdapter;
import com.xxdc.itask.adapter.NotepadAdapter;
import com.xxdc.itask.dialog.CustomAlertDialog;
import com.xxdc.itask.dialog.PictureSelectorDialog;
import com.xxdc.itask.dialog.RecorderDialog;
import com.xxdc.itask.dto.NotepadDTO;
import com.xxdc.itask.util.BitmapUtil;

public class HomeToShowNotepadActivity extends BaseActivity {
	@ViewInject(R.id.lv_note_notepad)
	private ListView mNoteLview;
	private LayoutInflater inflater;
	private String userTaskId;
	private PopupWindow noteWindow;
	private PictureSelectorDialog mPictureSelector;
	private EditText mEdit;
	private ImageView voi_img;
	private ImageView mClose;
	private int imgCount = 5;
	private File mRecorderFile = null;
	private List<String> imgList;
	private GridView grid_img;
	private List<Drawable> gridList;
	private CustomAlertDialog mDialog;
	private MediaPlayer player;
	private ImageButton mVoice;
	List<NotepadDTO> notepadList;
	View layout;
	Bitmap photo;
	// 语音动画控制器
	Timer mTimer = null;
	// 语音动画控制任务
	TimerTask mTimerTask = null;
	// 记录语音动画图片
	int index = 1;
	private RecorderDialog mRecorderDialog;
	private ArrayList<HashMap<String, String>> photo_list = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.notepad_fragment);
		initViews();
	}

	@Override
	protected void initUI() {
		inflater = LayoutInflater.from(HomeToShowNotepadActivity.this);
		imgList = new ArrayList<String>();
		notepadList = new ArrayList<NotepadDTO>();
		initCreatePop();
		mRecorderDialog = new RecorderDialog(activity, mVoice, new RecorderDialog.RecordCallback() {

			@Override
			public void complete(File file) {
				mRecorderFile = file;
				voi_img.setBackgroundResource(R.drawable.voice_blue_3);
				mClose.setVisibility(View.VISIBLE);
			}

			@Override
			public void cancle() {

			}
		});
		userTaskId = getIntent().getStringExtra("userTaskId");
		if (null == userTaskId) {
			userTaskId = "";
		}
		getDate();
	}

	@SuppressWarnings("unchecked")
	private void getDate() {
		Bundle bundle = getIntent().getBundleExtra("fromHitoryItem");
		if (null == bundle) {
			return;
		}
		List<NotepadDTO> notepadList = (List<NotepadDTO>) bundle.get("notePads");
		if (null != notepadList && notepadList.size() > 0) {
			setAdapter(notepadList);
		} else {
			Toast.makeText(this, "没有记事本，请先添加", Toast.LENGTH_SHORT).show();
			return;
		}
	}

	private void setAdapter(List<NotepadDTO> notepadList) {
		mNoteLview.setAdapter(new NotepadAdapter(notepadList, inflater, activity));
	}

	@OnClick(R.id.iv_add_notepad)
	private void addNotepadClick(View v) {
		if (noteWindow != null) {
			// 显示窗口
			if (mRecorderFile == null) {
				voi_img.setBackgroundResource(R.drawable.voice_blue_1);
			}
			noteWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}

	/**
	 * 初始化记事本输入框
	 */
	@SuppressWarnings("deprecation")
	private void initCreatePop() {
		gridList = new ArrayList<Drawable>();
		layout = inflater.inflate(R.layout.popwindow_notepad_add, null);
		mEdit = (EditText) layout.findViewById(R.id.et_context);
		mVoice = (ImageButton) layout.findViewById(R.id.img_voice);
		grid_img = (GridView) layout.findViewById(R.id.grid_img);
		voi_img = (ImageView) layout.findViewById(R.id.voice_img);
		mClose = (ImageView) layout.findViewById(R.id.iv_close);
		noteWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		noteWindow.setFocusable(true);
		noteWindow.setOutsideTouchable(true);
		noteWindow.update();
		noteWindow.setBackgroundDrawable(new BitmapDrawable());
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
				voi_img.setBackgroundResource(R.drawable.voice_blue_1);
				mClose.setVisibility(View.GONE);

			}
		});
		// 删除现有录音
		mClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRecorderFile != null) {
					mDialog.show();
				}
			}
		});
		// 添加新的图片
		layout.findViewById(R.id.add_photo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (imgCount > 0) {
					mPictureSelector = new PictureSelectorDialog(activity, R.style.MyAlertDialog);
					mPictureSelector.show();
				} else {
					Toast.makeText(activity, "记事本最多可添加5张图片", Toast.LENGTH_SHORT).show();
					return;
				}

			}
		});
		// 播放已有音频文件
		voi_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mRecorderFile != null) {
					if (player == null) {
						player = new MediaPlayer();
					}
					String path = mRecorderFile.getAbsolutePath();
					try {
						player.setDataSource(path);
						player.prepare();
						player.start();
						System.out.println(">>>>>正在播放音频文件");
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
		// 创建记事本
		layout.findViewById(R.id.do_post).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = mEdit.getText().toString().trim();
				if (!msg.equals("")) {
					noteWindow.dismiss();
					if (!mEdit.getText().toString().equals("")) {
						mEdit.setText("");
					}
					mRecorderFile = null;
					imgCount = 5;
					gridList.clear();
					mClose.setVisibility(View.GONE);

					ArrayList<String> images = new ArrayList<String>();

					NotepadDTO response = new NotepadDTO();
					response.setContext(msg);
					response.setCreateTime(155220200L);
					response.setNotepadId(20140205L);
					response.setTitle("记事本1");
					response.setUpdateTime(20142020L);
					response.setType(1);
					response.setImages(images);
					response.setUserId(20145220L);
					notepadList.add(response);
					mNoteLview.setAdapter(new NotepadAdapter(notepadList, inflater, activity));
					Toast.makeText(activity, "新的记事本已创建成功", Toast.LENGTH_SHORT).show();
					System.out.println(">>>>>>>>>>记事本的大小" + notepadList.size());
				} else {
					Toast.makeText(activity, "记事本内容不能为空", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case PIC_TO_VIEW:
			if (intent != null) {
				setPicToEdit(intent);
			}
			break;
		}
	}

	@SuppressWarnings("deprecation")
	private void setPicToEdit(Intent datas) {
		Bundle bundle = datas.getExtras();
		if (bundle != null) {
			photo = bundle.getParcelable("data");
			String image = Base64.encodeToString(BitmapUtil.Bitmap2Bytes(photo), Base64.DEFAULT);
			imgList.add("\"" + System.currentTimeMillis() + ".png" + "\"" + ":" + "\"" + image + "\"");
			Drawable drawable = new BitmapDrawable(photo);
			gridList.add(drawable);
			GridNomalAdapter mAdapter = new GridNomalAdapter(activity, grid_img, gridList, new GridNomalAdapter.CallBack() {

				@Override
				public void callBack(int position) {
					imgCount++;
				}
			});
			grid_img.setAdapter(mAdapter);
			
			grid_img.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(activity, ImagePagerActivity.class);
					intent.putExtra("photos", photo_list);
					intent.putExtra("position", position);
					startActivity(intent);

				}
			});
			imgCount--;
			Toast.makeText(activity, "你还可以添加" + imgCount + "张图片", Toast.LENGTH_SHORT).show();
		}

	}

	private void initViews() {
		for (int i = 0; i < 5; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("photo", com.xxdc.itask.Config.TestImageUrl);
			photo_list.add(map);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			if (player != null) {
				player.stop();
				player.release();
				player = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
