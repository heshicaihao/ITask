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

import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.ImageActivity;
import com.xxdc.itask.activity.TaskDetailActivity;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.fragment.HomeFragment;
import com.xxdc.itask.service.PlayerService;
import com.xxdc.itask.util.ITask;
import com.xxdc.itask.util.StringUtils;
import com.xxdc.itask.widget.SlideView;
import com.xxdc.itask.widget.SlideView.OnSlideListener;
import com.xxdc.itask.widget.SlideView.OperationCallBack;

public class HomeTaskExpandableAdapter extends TaskListAdapter implements OnSlideListener {
	private SlideView mLastSlideViewWithStatusOn;

	public HomeTaskExpandableAdapter(Context context, ExpandableListView listview) {
		super(context, listview);
	}

	public HomeTaskExpandableAdapter(Context context, ArrayList<UserTaskDTO> list, ExpandableListView listview) {
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
		SlideView slideView = (SlideView) convertView;
		ViewHolder holder;
		if (slideView == null) {
			slideView = new SlideView(mContext);
			View view = LayoutInflater.from(mContext).inflate(viewId, null);
			holder = new ViewHolder();
			holder.taskTypeImg = (ImageView) view.findViewById(R.id.task_type_img);
			if (!isChild) {
				holder.childAddImg = (ImageView) view.findViewById(R.id.child_add_img);
				holder.childAddImg.setVisibility(View.VISIBLE);
			}
			// view.findViewById(R.id.voice_img);
			holder.taskNameText = (TextView) view.findViewById(R.id.task_name);
			holder.taskDateText = (TextView) view.findViewById(R.id.task_date);
			holder.clockImg = (ImageView) view.findViewById(R.id.clock_img);
			holder.voiceImg = (ImageView) view.findViewById(R.id.voice_img);
			holder.imageImg = (ImageView) view.findViewById(R.id.image_img);
			holder.detailImg = (ImageView) view.findViewById(R.id.detail_img);
			view.setTag(holder);
			slideView.setContentView(view);
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}

		/** 手势滑动处理 */
		setSlideCallback(slideView, task, isChild, list);
		setTaskType(holder, task.getTaskType());
		setTaskStatus(holder, task);
		/** task内容 */
		if (StringUtils.isEmpty(task.getTaskName())) {
			holder.taskNameText.setText(R.string.no_title);
		} else {
			holder.taskNameText.setText(task.getTaskName());
		}
		/** 节束日期 */
		holder.taskDateText.setText(mFormat.format(task.getEndDate()));

		task.setSlideView(slideView);

		setListener(holder, task, isChild);
		return slideView;
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
				TaskDetailActivity.launch(mContext, task);
			}
		});

		if (!isChild) {
			holder.childAddImg.setBackgroundResource(R.drawable.add_task_selector);
			holder.childAddImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mHomeFragment.addTask(task);
				}
			});
		}
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

	@Override
	public void onSlide(View view, int status) {
		if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}

		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}

	private void setSlideCallback(SlideView slideView, final UserTaskDTO task, final boolean isChild, final LinkedList<UserTaskDTO> list) {
		slideView.setCallback(new OperationCallBack() {

			@Override
			public void rightToLeft() {
				try {
					int index = list.indexOf(task);
					switch (task.getStatus()) {
					case ITask.TASK_FLAG_DELETE:
						task.setStatus(ITask.TASK_FLAG_NORMAL);
						ITaskApp.getInstance().getDbUtils().update(task, "status");
						doPostUpdateTaskStatus(task);
						list.remove(task);
						list.addFirst(task);
						break;
					}
					mExpandableListView.setAdapter(HomeTaskExpandableAdapter.this);
					if (!isChild) {
						mExpandableListView.setSelectedGroup(index);
					}
					expandAllGroup();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void leftToRight() {
				try {
					int index = list.indexOf(task);
					switch (task.getStatus()) {
					case ITask.TASK_FLAG_NORMAL:
						task.setStatus(ITask.TASK_FLAG_DELETE);
						list.remove(task);
						list.addLast(task);
						break;
					case ITask.TASK_FLAG_DELETE:
						task.setStatus(ITask.TASK_FLAG_FNISH);
						list.remove(task);
						break;
					}
					ITaskApp.getInstance().getDbUtils().update(task, "status");
					doPostUpdateTaskStatus(task);
					mExpandableListView.setAdapter(HomeTaskExpandableAdapter.this);
					if (!isChild) {
						mExpandableListView.setSelectedGroup(index);
					}
					expandAllGroup();
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void doPostUpdateTaskStatus(final UserTaskDTO task) {
		ITaskApp.getInstance().getHttpClient().updateTaskStatus(task.getStatus(), task.getUserTaskId(), new Callback() {

			@Override
			public void onSuccess(Object o) {
				LogUtils.i(task.getTaskName() + ", 修改为" + task.getStatus() + "成功");
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFailure(Object o) {
				LogUtils.i(task.getTaskName() + ", 修改为" + task.getStatus() + "失败");
			}
		});
	}

}
