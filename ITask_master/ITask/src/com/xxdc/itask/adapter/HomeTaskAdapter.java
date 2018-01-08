package com.xxdc.itask.adapter;

import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.UserTaskDTO;

public class HomeTaskAdapter extends ArrayListAdapter<UserTaskDTO> implements OnClickListener {
	/**
	 * TASK_TYPE = 1;别人发给我的 TASK_TYPE = 2;我发给别人的 TASK_TYPE = 3;我自己的
	 */
	private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private MediaPlayer mPlayer;
	public HomeTaskAdapter(Activity c) {
		super(c);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = mContext.getLayoutInflater().inflate(R.layout.home_task_list_item, null);
			// view.setLayoutParams(new
			// AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
			// 60));

			holder = new ViewHolder();
			holder.taskTypeImg = (ImageView) view.findViewById(R.id.task_type_img);
			holder.childAddImg = (ImageView) view.findViewById(R.id.child_add_img);
//			holder.voiceImg = (ImageButton) view.findViewById(R.id.voice_img);
			holder.photoImg = (ImageButton) view.findViewById(R.id.photo_img);
			holder.taskNameText = (TextView) view.findViewById(R.id.task_name);
			holder.taskDateText = (TextView) view.findViewById(R.id.task_date);
			view.setTag(holder);
			holder.voiceImg.setOnClickListener(this);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		setTaskType(holder, mList.get(position).getTaskType());
		holder.taskNameText.setText(mList.get(position).getTaskName());
		holder.taskDateText.setText(mFormat.format(mList.get(position).getTaskClock()));
		
		holder.voiceImg.setTag(mList.get(position).getVoice());
		ITaskApp.getInstance().getBitmpUtils().display(holder.photoImg, mList.get(position).getPhoto());
		return view;
	}
	
	private void setTaskType(ViewHolder holder, int taskType) {
		switch(taskType) {
		case 1:
			holder.taskTypeImg.setImageResource(R.drawable.home_task_feedback_selector);
			break;
		case 2:
			holder.taskTypeImg.setImageResource(R.drawable.home_task_feedout_selector);
			break;
		default:
			holder.taskTypeImg.setImageResource(R.drawable.home_task_feedout_selector);
			break;
		}
	}

	private static class ViewHolder {
		ImageView taskTypeImg;
		ImageView childAddImg;
		ImageButton voiceImg;
		ImageButton photoImg;
		TextView taskNameText;
		TextView taskDateText;
	}

	@Override
	public void onClick(View v) {
//		switch(v.getId()) {
//		case R.id.voice_img:
//			playRecord((String) v.getTag());
//			break;
//		}
	}
	
	private void playRecord(String url) {
		if (mPlayer == null) {
			mPlayer = new MediaPlayer();
		}
		
		try {
			mPlayer.setDataSource(url);
			mPlayer.prepare();
			mPlayer.start();
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
