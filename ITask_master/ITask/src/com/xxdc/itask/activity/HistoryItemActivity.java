package com.xxdc.itask.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.GalleryAdapter;
import com.xxdc.itask.adapter.GalleryAdapter.OnItemClickLitener;
import com.xxdc.itask.dto.FeebackDTO;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.HistoryItemDetailResponse;
import com.xxdc.itask.entity.HistoryIDRString;
import com.xxdc.itask.service.PlayerService;

public class HistoryItemActivity extends BaseActivity implements OnClickListener {
	@ViewInject(R.id.tv_history_name)
	private TextView tv_history_name;// 标题名
	private UserTaskDTO task = null;// 传进来的任务对象
	private UserTaskDTO detail = null;
	@ViewInject(R.id.tv_Task_Sender)
	private TextView tv_Task_Sender;// 任务分配人
	@ViewInject(R.id.tv_Task_Do)
	private TextView tv_Task_Do;// 任务执行人
	@ViewInject(R.id.ll_Task_feedback)
	private LinearLayout ll_Task_feedback;// 反馈
	@ViewInject(R.id.ll_Task_notepad)
	private LinearLayout ll_Task_notepad;// 记事本
	public static ArrayList<FeebackDTO> taskFeebacks = null;
	@ViewInject(R.id.tv_Task_alert_time)
	private TextView tv_Task_alert_time;// 提醒时间
	@ViewInject(R.id.ll_fenpei)
	private LinearLayout ll_fenpei;// 分配
	@ViewInject(R.id.ll_Task_fujian)
	private LinearLayout ll_Task_fujian;// 附件
	@ViewInject(R.id.mRecyclerView)
	private RecyclerView mRecyclerView;// 底部图片显示的mRecyclerView
	private GalleryAdapter mAdapter;// 图片的适配器
	private List<String> mDatas;// 图片适配器的路径
	@ViewInject(R.id.iv_voice)
	private ImageView iv_voice;
	private String voiceUrl = "";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_history_item);
		getTask();// 获取任务
		initUI();// 实例化各个控件
		setListener();// 设置监听器
	}

	private void setListener() {
		ll_Task_feedback.setOnClickListener(this);
		ll_fenpei.setOnClickListener(this);
		ll_Task_notepad.setOnClickListener(this);
		iv_voice.setOnClickListener(this);
	}

	private void getTask() {
		Bundle bundle = getIntent().getBundleExtra("history_item");
		if (null != bundle) {
			task = (UserTaskDTO) bundle.get("history_item");
		} else {
			return;
		}
	}

	public static void launch(Context c) {
		Intent intent = new Intent(c, HistoryItemActivity.class);
		c.startActivity(intent);
	}

	@Override
	protected void initUI() {
		if (null != task) {
			tv_history_name.setText(task.getTaskName());
			setImage();
			getHistoryDetail();
		}
	}

	/**
	 * 设置底部图片的展示
	 */
	private void setImage() {
		mDatas = new ArrayList<String>();
		// 设置布局管理器
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		// 设置适配器
		mAdapter = new GalleryAdapter(this, mDatas);
		mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {
				jumpImageActivity(position);
			}
		});
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	/*
	 * 跳转到图片界面
	 */
	protected void jumpImageActivity(int position) {
		Intent intent = new Intent(this, ImageActivity.class);
		intent.putExtra("imageurl", mDatas.get(position));
		this.startActivity(intent);
	}

	/**
	 * 获取历史任务详情的接口
	 */
	private void getHistoryDetail() {
		ITaskApp.getInstance().getHttpClient().getHistoryTaskListItemDetail(Config.token, task.getUserTaskId() + "", new Callback() {

			@Override
			public void onSuccess(Object o) {
				Log.i("taskDetail", "查询详细任务成功");
				mLoadingDialog.dismiss();
				HistoryItemDetailResponse response = (HistoryItemDetailResponse) o;
				if (null != response && null != response.getResponse()) {
					detail = response.getResponse();
					Log.i("detail", "detail=" + detail.toString());
					saveHistoryIDRToDB(response);
					setData();
				}
			}

			@Override
			public void onStart() {
				Log.i("taskDetail", "查询详细任务开始");
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				Log.i("taskDetail", "查询详细任务失败");
				mLoadingDialog.dismiss();
				HistoryItemDetailResponse response = fineHistoryIDRFormDB();
				if (null != response && null != response.getResponse()) {
					detail = response.getResponse();
					setData();
				}
			}
		});

	}

	@SuppressWarnings("unused")
	protected void setData() {
		if (null == detail) {
			return;
		}
		tv_history_name.setText(detail.getTaskName());
		tv_Task_Do.setText(detail.getDoUsers());
		if (!("").equals(task.getSendUserRealName())) {
			tv_Task_Sender.setText(detail.getSendUserRealName());
		}
		if (null != detail.getTaskClock() + "" || detail.getTaskClock() == 0) {
			tv_Task_alert_time.setText(getDateTime(detail.getTaskClock() + ""));
		} else {
			tv_Task_alert_time.setText("未设定");
		}
		taskFeebacks = (ArrayList<FeebackDTO>) detail.getFeebacks();
		if (null != detail.getVoice()) {
			voiceUrl = detail.getVoice();
			LogUtils.i("voice=" + detail.getVoice());
		}
		if (null != taskFeebacks && taskFeebacks.size() > 0) {
			Log.i("feedBacks", "feedBacks=" + taskFeebacks.toString());
		}

		if (null != detail.getPhoto()) {
			LogUtils.i("photo=" + detail.getPhoto());
			mDatas.add(detail.getPhoto());
			mAdapter.notifyDataSetChanged();
		}
	}

	@SuppressLint("SimpleDateFormat")
	private String getDateTime(String time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date(Long.parseLong(time));
		String date2 = df.format(date);
		return date2;
	}

	private void saveHistoryIDRToDB(HistoryItemDetailResponse response) {
		try {
			Gson gson = new Gson();
			System.out.println("HistoryIDRString------>" + gson.toJson(response));
			HistoryIDRString hidrs = new HistoryIDRString();
			hidrs.setHistoryID(gson.toJson(response));
			ITaskApp.getInstance().getDbUtils().saveOrUpdate(hidrs);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private HistoryItemDetailResponse fineHistoryIDRFormDB() {
		HistoryItemDetailResponse response = null;
		try {
			List<HistoryIDRString> lists = ITaskApp.getInstance().getDbUtils().findAll(Selector.from(HistoryIDRString.class));
			if (null != lists && lists.size() > 0) {
				String noteLists = lists.get(0).getHistoryID();
				Gson gson = new Gson();
				response = (HistoryItemDetailResponse) gson.fromJson(noteLists, HistoryItemDetailResponse.class);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_Task_feedback:// 反馈
			Intent intent = new Intent(this, FeedBackActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("detail", detail);
			bundle.putString("flag", "1");
			intent.putExtra("task", bundle);
			startActivity(intent);
			break;
		case R.id.ll_fenpei:// 分配
			// if (null == detail) {
			// Toast.makeText(this, "请先创建任务", 3000).show();
			//
			// } else {
			// Intent intentFenPei = new Intent(this, FenPeiActivity.class);
			// intentFenPei.putExtra("fenpei", detail.getDoUsers());
			// startActivity(intentFenPei);
			// }
			Intent intent2 = new Intent(HistoryItemActivity.this, AllotActivity.class);
			intent2.putExtra("idName", detail.getDoUsers());
			intent2.putExtra("taskId", detail.getTaskId()+"");
			startActivity(intent2);
			break;
		case R.id.ll_Task_notepad:// 记事本
			if (null == detail) {
				Toast.makeText(this, "请先创建您的记事本", 3000).show();
				return;
			}
			if (null == detail.getNotepads()) {
				Toast.makeText(this, "您选择的任务暂时还没有记事本记录", 3000).show();
				return;
			}
			Intent intentNotePad = new Intent(this, HomeActivity.class);
			Bundle bundleNote = new Bundle();
			bundleNote.putSerializable("notePads", detail.getNotepads());
			intentNotePad.putExtra("fromHitoryItem", bundleNote);
			startActivity(intentNotePad);
			break;
		case R.id.iv_voice:
			if (!("").equals(voiceUrl)) {
				PlayerService.startPlay(this, voiceUrl);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onRestart() {
		HistoryItemActivity.this.finish();
		super.onRestart();
	}
}
