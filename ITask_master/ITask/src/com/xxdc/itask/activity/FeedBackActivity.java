package com.xxdc.itask.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.FeedBackListAdapter;
import com.xxdc.itask.dialog.LoadingDialog;
import com.xxdc.itask.dialog.PictureSelectorDialog;
import com.xxdc.itask.dialog.RecorderDialog;
import com.xxdc.itask.dialog.RecorderDialog.RecordCallback;
import com.xxdc.itask.dto.FeebackDTO;
import com.xxdc.itask.dto.HistoryItemDetailDto;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.net.UrlConsts;
import com.xxdc.itask.dto.response.FileResponse;
import com.xxdc.itask.dto.response.SubmitFeedbackResponse;
import com.xxdc.itask.service.PlayerService;
import com.xxdc.itask.service.RecorderService;
import com.xxdc.itask.view.XListView;
import com.xxdc.itask.view.XListView.IXListViewListener;

public class FeedBackActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.list_feedback)
	private XListView list_feedback;// 反馈内容的容器
	private ArrayList<FeebackDTO> feebacks = null;// 反馈的内容
	private FeedBackListAdapter adapter = null;// 反馈适配器
	private UserTaskDTO detail = null;// 反馈详情
	@ViewInject(R.id.tv_history_name)
	private TextView tv_history_name;// 任务的名字
	@ViewInject(R.id.et_input)
	private EditText et_input;// 反馈输入框
	@ViewInject(R.id.tv_submit)
	private TextView tv_submit;// 提交反馈按钮
	@ViewInject(R.id.rl_finish)
	private RelativeLayout rl_finish;// 完成单选框
	@ViewInject(R.id.rl_goback)
	private RelativeLayout rl_goback;// 退回单选框
	private String feeStatus = 1 + "";// 提交反馈的状态,默认已完成
	@ViewInject(R.id.ll_edit)
	private LinearLayout ll_edit;// 添加编辑的linearLayout
	@ViewInject(R.id.iv_photo)
	private ImageView iv_photo;// 添加图片的按钮
	@ViewInject(R.id.iv_mic)
	private ImageView iv_mic;// 添加声音的按钮
	@ViewInject(R.id.iv_fujian)
	private ImageView iv_fujian;// 添加附件的按钮
	@ViewInject(R.id.btn_finish)
	private Button btn_finish;// 完成单选按钮
	@ViewInject(R.id.btn_goback)
	private Button btn_goback;// 退回单选按钮
	private PictureSelectorDialog mPictureSelector;// 图片选择框
	private File imgFile;// 要上传的文件
	private Bitmap photo;// 从选择图片界面获得的图片
	private String imageUrl;// 图片的显示路径
	private String imagePath;// 图片存放的路径
	protected LoadingDialog mLoadingDialog;// 还滑成功加载服务器数据时,弹出框
	private ArrayList<String> imgs = new ArrayList<String>();// 存放图片路径的容器
	private ArrayList<String> voices = new ArrayList<String>();// 存放声音路径的容器
	private ArrayList<String> attachs = new ArrayList<String>();// 存放附件路径的容器
	private File mRecorderFile = null;// 录音文件

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_feedback);
		ViewUtils.inject(this);
		mLoadingDialog = new LoadingDialog(this);
		initUI();
		setListener();
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		tv_submit.setOnClickListener(this);
		iv_photo.setOnClickListener(this);
		rl_finish.setOnClickListener(this);
		rl_goback.setOnClickListener(this);
		btn_finish.setOnClickListener(this);
		btn_goback.setOnClickListener(this);
		iv_mic.setOnClickListener(this);
		list_feedback.setPullLoadEnable(true);
		list_feedback.setPullRefreshEnable(true);
		/**
		 * 设置下拉刷新,下拉加载的监听
		 */
		list_feedback.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				Log.i("onRefresh", "onRefresh执行了");
				loadMore();
			}

			@Override
			public void onLoadMore() {
				Log.i("loadMore", "loadMore执行了");
				loadMore();
			}
		});
	}

	protected void initUI() {
		adapter = new FeedBackListAdapter(this);
		list_feedback.setAdapter(adapter);
		getTask();
		// adapter.setList(HistoryItemActivity.taskFeebacks);
	}

	/**
	 * 获取任务
	 */
	private void getTask() {
		Bundle bundle = getIntent().getBundleExtra("task");
		if (null != bundle) {
			detail = (UserTaskDTO) bundle.getSerializable("detail");
			if (null == detail) {
				Toast.makeText(this, "您选择的任务暂时还没有反馈记录", 3000).show();
				feebacks = null;
			} else {
				feebacks = (ArrayList<FeebackDTO>) detail.getFeebacks();
				if (null != feebacks && feebacks.size() > 0) {
					Log.i("feedBackActivity", "feebacks=" + feebacks.toString());
					adapter.setList(feebacks);
					// list_feedback.setSelection(list_feedback.getBottom());
					// list_feedback.toBottom();
					tv_history_name.setText(detail.getTaskName());
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_submit:
			submit();
			break;
		case R.id.rl_finish:
			btn_finish.setBackgroundResource(R.drawable.bg_choice_2);
			btn_goback.setBackgroundResource(R.drawable.bg_choice_1);
			feeStatus = 1 + "";
			break;
		case R.id.rl_goback:
			btn_finish.setBackgroundResource(R.drawable.bg_choice_1);
			btn_goback.setBackgroundResource(R.drawable.bg_choice_2);
			feeStatus = 2 + "";
			break;
		case R.id.btn_finish:
			btn_finish.setBackgroundResource(R.drawable.bg_choice_2);
			btn_goback.setBackgroundResource(R.drawable.bg_choice_1);
			feeStatus = 1 + "";
			break;
		case R.id.btn_goback:
			btn_finish.setBackgroundResource(R.drawable.bg_choice_1);
			btn_goback.setBackgroundResource(R.drawable.bg_choice_2);
			feeStatus = 2 + "";
			break;
		case R.id.iv_photo:// 拍照
			mPictureSelector = new PictureSelectorDialog(this, R.style.MyAlertDialog);
			mPictureSelector.show();
			break;
		case R.id.iv_mic:// 录音
			clickVoice();
			// noTimeCancelRecord();
			// deleteRecordFile();
			break;
		case R.id.iv_fujian:// 附件
			break;
		default:
			break;
		}

	}

	private void clickVoice() {
		final File file = new File(this.getExternalFilesDir(Config.RECORDER), System.currentTimeMillis() + ".amr");
		Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
		RecorderService.startRecording(this, MediaRecorder.AudioSource.MIC, file.getAbsolutePath(), false);
		new RecorderDialog(this, new RecordCallback() {

			@Override
			public void complete(File file) {
				mRecorderFile = file;
				PlayerService.startPlay(FeedBackActivity.this, mRecorderFile.getAbsolutePath());
				Log.i("voiceurl", "voiceUrl=" + mRecorderFile.getAbsolutePath());
				uploadVoice();

			}

			@Override
			public void cancle() {
				// mActivity.deleteFile(file.getAbsolutePath());
				boolean isDelete = file.delete();
				Toast.makeText(FeedBackActivity.this, isDelete + "", Toast.LENGTH_LONG).show();
			}
		}).show();
	}

	protected void uploadVoice() {
		ITaskApp.getInstance().getHttpClient().uploadFile(mRecorderFile, new Callback() {

			@Override
			public void onSuccess(Object o) {
				Log.i("上传录音", "上传录音成功");
				String voiceUrl = ((FileResponse) o).getResponse();
				Log.i("上传录音", "voiceUrl=" + voiceUrl);
				voices.add(voiceUrl);

			}

			@Override
			public void onStart() {
				Log.i("上传录音", "上传录音开始");

			}

			@Override
			public void onFailure(Object o) {
				Log.i("上传录音", "上传录音失败");

			}
		});

	}

	// private void noTimeCancelRecord() {
	// // btnTalk.setClickable(false);
	// // btnTalk.setBackgroundResource(R.drawable.btn_bottom_talking);
	// btnTalk.setBackgroundResource(R.drawable.record_btn_bg);
	// voice_timer_text.setText(String.format("%02d:%02d", 0 / 60, 0 % 60));
	// recordNullity(getResources().getString(R.string.voice_recor_error));
	// layoutVoice.setVisibility(View.VISIBLE);
	// mHandler.postDelayed(new Runnable() {
	// @Override
	// public void run() {
	// // btnTalk.setClickable(true);
	// // btnTalk.setBackgroundResource(R.drawable.btn_bottom_talk);
	// btnTalk.setBackgroundResource(R.drawable.record_btn_bg);
	// layoutVoice.setVisibility(View.GONE);
	// }
	// }, 500);
	// }
	// 删除这个文件
	// private void deleteRecordFile() {
	// try {
	// if (null != fileSavePath && !"".equals(fileSavePath)) {
	// File file = new File(fileSavePath);
	// if (file.exists()) {
	// file.delete();
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode != RESULT_OK) {
			return;
		}
		if (requestCode == 1001) {
			File file = new File(Environment.getExternalStorageDirectory(), "textphoto.jpg");
			Uri outputFileUri = Uri.fromFile(file);
			startPhotoZoom(outputFileUri);
		} else if (requestCode == 1000) {
			startPhotoZoom(intent.getData());
		} else if (requestCode == 1002) {
			if (intent != null) {
				setPicToView(intent);
			}
		}
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1.0);
		intent.putExtra("aspectY", 1.0);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 1002);
	}

	private void setPicToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			// strBitmap = BitmapUtil.convertIconToString(photo);
			// String imagePath = getExternalCacheDir().getAbsolutePath() +
			// "/abc.png";
			String path2 = "";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				imagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/LeChai/" + detail.getTaskId() + "_" + System.currentTimeMillis()
						+ ".png";
				path2 = detail.getTaskId() + System.currentTimeMillis() + ".png";
				imgFile = new File(imagePath);
				if (!imgFile.getParentFile().exists()) {
					imgFile.getParentFile().mkdirs();
				}
			} else {
				Toast.makeText(this, "SD卡不存在", 3000).show();
			}
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(imgFile);
				photo.compress(CompressFormat.PNG, 100, fos);
				// upLoadImg(imgFile, photo);

				// String uploadHost = UrlContent.GET_UPLOAD_IMAGE_URL;
				// String uploadHost =
				// "http://192.168.10.105:8080/Task/ws/photo";
				String uploadHost = UrlConsts.UPLOAD_FILE;
				Log.i("upload", "upload_url=" + uploadHost);
				RequestParams params = new RequestParams();
				// String
				// username=PreferenceUtils.getPrefString(ManagerActivity.this,
				// "username", "");
				// String
				// pwd=PreferenceUtils.getPrefString(ManagerActivity.this,
				// "pwd", "");
				String token = Config.token;
				String taskId = detail.getTaskId() + "";
				Log.i("upload", "  加密后的token=" + token);
				params.addBodyParameter("token", token);
				params.addBodyParameter("taskId", taskId);
				params.addBodyParameter("file", new File(imagePath));
				// params.addBodyParameter("imagePath", path2);
				uploadMethod(params, uploadHost);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * 上传图片
	 * 
	 * @param params
	 * @param uploadHost
	 */
	public void uploadMethod(final RequestParams params, final String uploadHost) {
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, uploadHost, params, new RequestCallBack<String>() {
			public void onStart() {
				LogUtils.i("上传图片开始");
				// msgTextview.setText("conn...");
			}

			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
					LogUtils.i("正在上传");
					// msgTextview.setText("upload: " + current + "/"+
					// total);
				} else {
					// msgTextview.setText("reply: " + current + "/"+
					// total);
				}
			}

			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.i("upload", "上传头像成功");
				// iv_user.setImageBitmap(photo);
				Log.i("upload", responseInfo.result.toString());
				Gson gson = new Gson();
				FileResponse file = gson.fromJson(responseInfo.result.toString(), new TypeToken<FileResponse>() {
				}.getType());
				// imageUrl = file.getResponse();
				imageUrl = imagePath.split("/")[imagePath.split("/").length - 1];
				LogUtils.i("imageUrl=" + imageUrl);
				if (!("").equals(imageUrl)) {
					imgs.add(imageUrl);
				}
				// msgTextview.setText("reply: " + responseInfo.result);
			}

			public void onFailure(HttpException error, String msg) {
				Log.i("upload", "userImageUrl 上传头像失败");
				// msgTextview.setText(error.getExceptionCode() + ":" +
				// msg);
			}
		});
	}

	private void submit() {
		if (null == detail) {
			Toast.makeText(this, "请新建任务", 3000).show();
			return;
		}
		String token = Config.token;
		String taskId = detail.getTaskId() + "";
		String context = et_input.getText().toString();
		if (("").equals(context)) {
			Toast.makeText(this, "反馈内容不能为空", 3000).show();
			return;
		}
		if (null != imgs) {
			LogUtils.i("imgs=" + imgs.toString());
		}
		String type = 1 + "";
		// Gson gson=new Gson();
		// String imgs_g=gson.toJson(imgs);
		// LogUtils.i("imgs_g"+imgs_g.toString());
		final int[] location = getPosition();
		ITaskApp.getInstance().getHttpClient()
				.submitFeedBack(token, taskId, context, feeStatus, type, imgs.toString(), voices.toString(), attachs.toString(), new Callback() {

					@Override
					public void onSuccess(Object o) {
						mLoadingDialog.dismiss();
						et_input.getText().clear();
						imgs.removeAll(imgs);
						Toast.makeText(getApplicationContext(), "提交反馈成功", Toast.LENGTH_SHORT).show();
						LogUtils.i("提交反馈成功");
						feebacks = ((SubmitFeedbackResponse) o).getResponse();
						adapter.setList(feebacks);
						// 设置listView滚动底部的三个方法
						// list_feedback.scrollTo(0,
						// list_feedback.getBottom());
						// list_feedback.setSelection(list_feedback.getBottom());
						// list_feedback.setSelection(list_feedback.getCount()-1);
					}

					@Override
					public void onStart() {
						mLoadingDialog.show();
						LogUtils.i("提交反馈开始");
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Object o) {
						mLoadingDialog.dismiss();
						Toast.makeText(getApplicationContext(), "提交反馈失败", Toast.LENGTH_SHORT).show();
						LogUtils.i("提交反馈失败");
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * 获取容器的位置
	 * 
	 * @return
	 */
	protected int[] getPosition() {
		int[] location = new int[2];
		list_feedback.getLocationOnScreen(location);
		return location;
	}

	private void loadMore() {
		String token = Config.token;
		String taskId = detail.getTaskId() + "";
		String context = "哈哈哈";
		String type = 1 + "";
		ITaskApp.getInstance().getHttpClient()
				.submitFeedBack(token, taskId, context, feeStatus, type, imgs.toString(), voices.toString(), attachs.toString(), new Callback() {

					@Override
					public void onSuccess(Object o) {
						Log.i("下拉刷新", "下拉刷新成功");
						feebacks = ((SubmitFeedbackResponse) o).getResponse();
						adapter.setList(feebacks);
						list_feedback.stopRefresh();
						list_feedback.stopLoadMore();
						// TODO Auto-generated method stub

					}

					@Override
					public void onStart() {
						Log.i("下拉刷新", "下拉刷新开始");
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailure(Object o) {
						Log.i("下拉刷新", "下拉刷新失败");
						// TODO Auto-generated method stub

					}
				});

	}
}
