package com.xxdc.itask.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.devsmart.android.ui.HorizontalListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.NotepadPicGVAdapter;
import com.xxdc.itask.dialog.PictureSelectorDialog;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.CreatNotepadResponse;
import com.xxdc.itask.entity.FileURLS;
import com.xxdc.itask.util.BitmapUtil;

public class NotepadAddActivity extends BaseActivity {
	@ViewInject(R.id.tv_cancle_note_add)
	private TextView mCancleTview;
	@ViewInject(R.id.tv_save_note_add)
	private TextView mSaveTview;
	@ViewInject(R.id.et_title_note_add)
	private EditText mTitleEview;
	@ViewInject(R.id.et_context_note_add)
	private EditText mContextEview;
	@ViewInject(R.id.lv_pic_note_item)
	private HorizontalListView picView;
	@ViewInject(R.id.gv_voice_note_add)
	private GridView mVoiceGview;
	@ViewInject(R.id.iv_pic_notepad_add)
	private ImageView mPicIview;
	@ViewInject(R.id.iv_mic_notepad_add)
	private ImageView mMicIview;
	@ViewInject(R.id.iv_file_notepad_add)
	private ImageView mFileIview;
	private PictureSelectorDialog mPictureSelector;
	private List<Bitmap> photos;
	private List<String> files;
	private int[] resIdVoices = new int[] { R.drawable.icon_mic_2, R.drawable.icon_mic_2, R.drawable.icon_mic_2, R.drawable.icon_mic_2, R.drawable.icon_mic_2 };
	private String userTaskId;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.notepad_add_activity);
	}

	@Override
	protected void initUI() {

		// ***************************仅为测试用例*******录音图片显示********************
		ArrayList<HashMap<String, Object>> a2 = new ArrayList<HashMap<String, Object>>();

		for (int j = 0; j < resIdVoices.length; j++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("icon", resIdVoices[j]);// 添加图像资源的ID
			a2.add(map);
		}
		mVoiceGview.setLayoutParams(new LinearLayout.LayoutParams(300 * (resIdVoices.length) + (resIdVoices.length - 1) * 10, 300));
		mVoiceGview.setNumColumns(resIdVoices.length);
		SimpleAdapter sb = new SimpleAdapter(this, a2, R.layout.notepad_add_gridview_item2, new String[] { "icon" }, new int[] { R.id.iv_voice_gridview_item });
		mVoiceGview.setAdapter(sb);

		// *************************************************************************
		files = new ArrayList<String>();
		photos = new ArrayList<Bitmap>();
		userTaskId = getIntent().getStringExtra("userTaskId");
		if (null == userTaskId) {
			userTaskId = "";
		}
	}

	/**
	 * 选择图片
	 * 
	 * @param v
	 */
	@OnClick(R.id.iv_pic_notepad_add)
	private void clickPhoto(View v) {
		cleanUploadImgFile();
		mPictureSelector = new PictureSelectorDialog(this, R.style.MyAlertDialog);
		mPictureSelector.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		System.out.println("requestCode="+requestCode+"resultCode="+resultCode+"intent="+intent);
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
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			photos.add(photo);
			System.out.println("-----photos------->"+photos.size());
			picView.setAdapter(new NotepadPicGVAdapter(photos, NotepadAddActivity.this));
			String image = Base64.encodeToString(BitmapUtil.Bitmap2Bytes(photo), Base64.DEFAULT);
			files.add("\"" + System.currentTimeMillis() + ".png" + "\"" + ":" + "\"" + image + "\"");
		}
	}

	@OnClick(R.id.tv_save_note_add)
	private void saveNotepad(View v) {
		String title = mTitleEview.getText().toString().trim();
		String context = mContextEview.getText().toString().trim();
		if (TextUtils.isEmpty(title)) {
			Toast.makeText(NotepadAddActivity.this, "请输入记事本标题！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(context)) {
			Toast.makeText(NotepadAddActivity.this, "请输入记事本内容！", Toast.LENGTH_SHORT).show();
			return;
		}

		doPostCreateNotepad(title, context, userTaskId);
	}
	
	@OnClick(R.id.tv_cancle_note_add)
	private void onCancle(){

	}

	private void doPostCreateNotepad(String title, String context, String userTaskId) {
		String fileURL = fileStrings();
		ITaskApp.getInstance().getHttpClient().createNotepad(title, context, 2, fileURL, userTaskId, new Callback() {

			@Override
			public void onSuccess(Object o) {
				mLoadingDialog.dismiss();
				CreatNotepadResponse response = (CreatNotepadResponse) o;
				if (null != response && response.getStatus()) {
					Toast.makeText(NotepadAddActivity.this, "上传记事本成功！", Toast.LENGTH_SHORT).show();
					NotepadAddActivity.this.finish();
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				Toast.makeText(NotepadAddActivity.this, "上传记事本失败！", Toast.LENGTH_SHORT).show();
			}
		});
	}

	// private String fileStrings() {
	// List<String> fileURLS = FileURLS.getFileURLS();
	// String fileURL = "";
	// if (null != fileURLS && fileURLS.size() > 0) {
	// for (String string : fileURLS) {
	// fileURL += "," + string;
	// }
	// fileURL = fileURL.substring(1);
	// System.out.println("fileURL--------->" + fileURL.substring(1));
	// }
	// return fileURL;
	// }

	private String fileStrings() {
		String fileURL = "";
		if (null != files && files.size() > 0) {
			for (String string : files) {
				fileURL += "," + string;
			}
			fileURL = fileURL.substring(1);
			System.out.println("fileURL--------->" + fileURL.substring(1));
		}
		return fileURL;
	}

	@Override
	protected void onDestroy() {
		FileURLS.getFileURLS().clear();
		super.onDestroy();
	}
}
