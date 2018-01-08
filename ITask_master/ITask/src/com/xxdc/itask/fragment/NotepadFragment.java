package com.xxdc.itask.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.HomeActivity.CallBack;
import com.xxdc.itask.activity.ImagePagerActivity;
import com.xxdc.itask.activity.NotepadItemDetailsActivity;
import com.xxdc.itask.adapter.GridNomalAdapter;
import com.xxdc.itask.adapter.NotepadNewAdapter;
import com.xxdc.itask.dialog.CustomAlertDialog;
import com.xxdc.itask.dialog.PictureSelectorDialog;
import com.xxdc.itask.dialog.RecorderDialog;
import com.xxdc.itask.dto.NotepadDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.NoteListResponse;
import com.xxdc.itask.entity.NoteListResponseString;
import com.xxdc.itask.util.BitmapUtil;
import com.xxdc.itask.util.StringUtils;

public class NotepadFragment extends BaseFragment implements CallBack {
	@ViewInject(R.id.lv_note_notepad)
	private ListView mNoteLview;
	private LayoutInflater inflater;
	private ArrayList<NotepadDTO> notePads = null;
	private PopupWindow noteWindow;
	private PictureSelectorDialog mPictureSelector;
	View layout;
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
	private ArrayList<HashMap<String, String>> photo_list = new ArrayList<HashMap<String, String>>();
	Bitmap photo;
	// 语音动画控制器
	Timer mTimer = null;
	// 语音动画控制任务
	TimerTask mTimerTask = null;
	// 记录语音动画图片
	int index = 1;
	private RecorderDialog mRecorderDialog;

	@SuppressWarnings("unchecked")
	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i("onResume");
		Bundle bundle = getArguments();
		if (null != bundle) {
			notePads = (ArrayList<NotepadDTO>) bundle.get("notePads");
			mNoteLview.setAdapter(new NotepadNewAdapter(notePads, inflater, mActivity, new NotepadNewAdapter.CallBack() {

				@Override
				public void callBackMethod(View v, int p) {
					System.out.println(">>>>>>>>>条目被点击");

				}
			}));
		} else {
			dopostGetNoteList();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		return initView(inflater, R.layout.notepad_fragment, container);
	}

	private void dopostGetNoteList() {
		ITaskApp.getInstance().getHttpClient().getNotes(1 + "", 3, new Callback() {
			@Override
			public void onSuccess(Object o) {
				mLoadingDialog.dismiss();
				NoteListResponse response = (NoteListResponse) o;
				if (null != response && null != response.getResponse().getNotepadList()) {
					saveNotepadsToDB(response);
					setAdapter(response);
				} else {
					Toast.makeText(mActivity, "记事本为空！", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				NoteListResponse response = fineNotepadsFormDB();
				if (null != response && null != response.getResponse().getNotepadList()) {
					setAdapter(response);
				}
			}
		});

	}

	private void saveNotepadsToDB(NoteListResponse response) {
		try {
			Gson gson = new Gson();
			System.out.println("NoteListResponseString------>" + gson.toJson(response));
			NoteListResponseString nlrs = new NoteListResponseString();
			nlrs.setNoteListResponseString(gson.toJson(response));
			ITaskApp.getInstance().getDbUtils().saveOrUpdate(nlrs);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private NoteListResponse fineNotepadsFormDB() {
		NoteListResponse response = null;
		try {
			List<NoteListResponseString> lists = ITaskApp.getInstance().getDbUtils().findAll(Selector.from(NoteListResponseString.class));
			if (null != lists && lists.size() > 0) {
				String noteLists = lists.get(0).getNoteListResponseString();
				Gson gson = new Gson();
				response = (NoteListResponse) gson.fromJson(noteLists, NoteListResponse.class);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void initUI(View v) {
		initViews();
		initCreatePop();
		imgList = new ArrayList<String>();
		mRecorderDialog = new RecorderDialog(mActivity, mVoice, new RecorderDialog.RecordCallback() {

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

	}

	@OnClick(R.id.iv_add_notepad)
	private void addNotepadClick(View v) {
		if (noteWindow != null) {
			// 显示窗口
			if (mRecorderFile == null) {
				voi_img.setBackgroundResource(R.drawable.voice_blue_1);
			}
			noteWindow.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
		}

	}

	/**
	 * 初始化记事本输入框
	 */
	@SuppressWarnings("deprecation")
	private void initCreatePop() {
		gridList = new ArrayList<Drawable>();
		layout = inflater.inflate(R.layout.popwindow_notepad_add, null);
		mVoice = (ImageButton) layout.findViewById(R.id.img_voice);
		mEdit = (EditText) layout.findViewById(R.id.et_context);
		grid_img = (GridView) layout.findViewById(R.id.grid_img);
		voi_img = (ImageView) layout.findViewById(R.id.voice_img);
		mClose = (ImageView) layout.findViewById(R.id.iv_close);
		noteWindow = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		noteWindow.setFocusable(true);
		noteWindow.setOutsideTouchable(true);
		noteWindow.update();
		noteWindow.setBackgroundDrawable(new BitmapDrawable());
		mDialog = new CustomAlertDialog(mActivity);
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
					mPictureSelector = new PictureSelectorDialog(mActivity, R.style.MyAlertDialog);
					mPictureSelector.show();
				} else {
					Toast.makeText(mActivity, "记事本最多可添加5张图片", Toast.LENGTH_SHORT).show();
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
				if (!StringUtils.isEmpty(msg) && mRecorderDialog.getRecordFile() != null && gridList.size() > 0) {
					noteWindow.dismiss();
					String voice = null;
					if (mRecorderDialog.getRecordFile() != null) {
						voice = mRecorderDialog.getRecordFile().getName();
					}
					if (!mEdit.getText().toString().trim().equals("")) {
						mEdit.setText("");
					}
					mRecorderFile = null;
					imgCount = 5;
					gridList.clear();
					mClose.setVisibility(View.GONE);
					Toast.makeText(mActivity, "新的记事本已创建成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mActivity, "记事本内容不能为空", Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private void setAdapter(final NoteListResponse response) {
		mNoteLview.setAdapter(new NotepadNewAdapter(response, inflater, mActivity, new NotepadNewAdapter.CallBack() {

			@Override
			public void callBackMethod(View v, int p) {
				Intent intent = new Intent(mActivity, NotepadItemDetailsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("note", response.getResponse().getNotepadList().get(p));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		}));
	}

	// 将图库返回的数据做处理显示 并提交
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setPicToEdit(Intent datas) {
		Bundle bundle = datas.getExtras();
		if (bundle != null) {
			photo = bundle.getParcelable("data");
			String image = Base64.encodeToString(BitmapUtil.Bitmap2Bytes(photo), Base64.DEFAULT);
			imgList.add("\"" + System.currentTimeMillis() + ".png" + "\"" + ":" + "\"" + image + "\"");
			Drawable drawable = new BitmapDrawable(photo);
			gridList.add(drawable);
			GridNomalAdapter mAdapter = new GridNomalAdapter(mActivity, grid_img, gridList, new GridNomalAdapter.CallBack() {

				@Override
				public void callBack(int position) {
					imgCount++;
				}
			});
			grid_img.setAdapter(mAdapter);
			imgCount--;
			grid_img.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(mActivity, ImagePagerActivity.class);
					intent.putExtra("photos", photo_list);
					intent.putExtra("position", position);
					startActivity(intent);

				}
			});
			Toast.makeText(mActivity, "你还可以添加" + imgCount + "张图片", Toast.LENGTH_SHORT).show();
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

	@Override
	public void callBackFragment(Intent retIntent) {
		if (retIntent != null) {
			setPicToEdit(retIntent);
		}

	}

}
