package com.xxdc.itask.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.ImageActivity;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.fragment.HomeFragment;
import com.xxdc.itask.service.PlayerService;
import com.xxdc.itask.util.ITask;
import com.xxdc.itask.util.StringUtils;

public class HistoryTaskAdapter extends TaskListAdapter {
	public HistoryTaskAdapter(Context context, ExpandableListView listview) {
		super(context, listview);
	}

	public HistoryTaskAdapter(Context context, ArrayList<UserTaskDTO> list, ExpandableListView listview) {
		super(context, list, listview);
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		return handleView(convertView, R.layout.home_task_list_item, getGroup(groupPosition), false, mList);
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		return handleView(convertView, R.layout.home_task_list_child_item, getChild(groupPosition, childPosition), true, getChildList(groupPosition));
	}

	private View handleView(View convertView, int viewId, UserTaskDTO task, boolean isChild, LinkedList<UserTaskDTO> list) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(viewId, null);
			holder = new ViewHolder();
			holder.taskTypeImg = (ImageView) view.findViewById(R.id.task_type_img);
			// view.findViewById(R.id.voice_img);
			holder.taskNameText = (TextView) view.findViewById(R.id.task_name);
			holder.taskDateText = (TextView) view.findViewById(R.id.task_date);
			holder.clockImg = (ImageView) view.findViewById(R.id.clock_img);
			holder.voiceImg = (ImageView) view.findViewById(R.id.voice_img);
			holder.imageImg = (ImageView) view.findViewById(R.id.image_img);
			holder.detailImg = (ImageView) view.findViewById(R.id.detail_img);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		/** 手势滑动处理 */
//		setSlideCallback(slideView, task, isChild, list);
		setTaskType(holder, task.getTaskType());
//		setTaskStatus(holder, task);
		/** task内容 */
		if (StringUtils.isEmpty(task.getTaskName())) {
			holder.taskNameText.setText(R.string.no_title);
		} else {
			holder.taskNameText.setText(task.getTaskName());
		}
		/** 节束日期 */
		holder.taskDateText.setText(mFormat.format(task.getEndDate()));

		setListener(holder, task, isChild);
		return view;
	}

	private void setListener(ViewHolder holder, final UserTaskDTO task, boolean isChild) {
		holder.clockImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// url为空不显示播放图标
		if (StringUtils.isEmpty(task.getVoice())) {
			holder.voiceImg.setBackgroundResource(R.drawable.voice1);
		} else {
			holder.voiceImg.setBackgroundResource(R.drawable.voice);
			holder.voiceImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					PlayerService.startPlay(mContext, task.getVoice());
				}
			});
		}

		/** 图片 */
		if (StringUtils.isEmpty(task.getPhoto())) {
			holder.imageImg.setBackgroundResource(R.drawable.drawable);
		} else {
			ITaskApp.getInstance().getBitmpUtils().display(holder.imageImg, task.getPhoto());
			holder.imageImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mHomeFragment.getActivity(), ImageActivity.class);
					intent.putExtra("imageurl", task.getPhoto());
					mHomeFragment.startActivity(intent);
				}
			});
		}

		holder.detailImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mHomeFragment.jumpDetail(task);
			}
		});

//		if (!isChild) {
//			holder.childAddImg.setBackgroundResource(R.drawable.add_task_selector);
//			holder.childAddImg.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					mHomeFragment.addTask(task);
//				}
//			});
//		}
	}

	/**
	 * TASK_TYPE = 1,别人发给我的 ;TASK_TYPE = 2,我发给别人的 ;TASK_TYPE = 3:我自己的
	 */
	private void setTaskType(ViewHolder holder, int taskType) {
		switch (taskType) {
		case 1:
			holder.taskTypeImg.setBackgroundResource(R.drawable.task_receiver);
			break;
		case 2:
			holder.taskTypeImg.setBackgroundResource(R.drawable.task_send);
			break;
		default:
			holder.taskTypeImg.setBackgroundResource(R.drawable.task_edit);
			break;
		}
	}

	private void setTaskStatus(ViewHolder holder, UserTaskDTO userTask) {
		switch (userTask.getStatus()) {
		case ITask.TASK_FLAG_NORMAL:
			holder.taskNameText.getPaint().setFlags(0);
			break;
		case ITask.TASK_FLAG_DELETE:
			holder.taskNameText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			break;
		}
	}

	private class ViewHolder {
		ImageView taskTypeImg;
		ImageView childAddImg;
		ImageView clockImg;
		ImageView voiceImg;
		ImageView imageImg;
		ImageButton photoImg;
		ImageView detailImg;
		TextView taskNameText;
		TextView taskDateText;
	}

	public void setHomeFragment(HomeFragment homeFragment) {
		this.mHomeFragment = homeFragment;
	}
}
