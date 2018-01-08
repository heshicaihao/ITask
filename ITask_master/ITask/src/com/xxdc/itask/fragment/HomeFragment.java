package com.xxdc.itask.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.AllotActivity;
import com.xxdc.itask.activity.FeedBackActivity;
import com.xxdc.itask.activity.HomeToShowNotepadActivity;
import com.xxdc.itask.activity.TaskAddActivity;
import com.xxdc.itask.activity.TaskDetailActivity;
import com.xxdc.itask.activity.TimerTaskActivity;
import com.xxdc.itask.adapter.HomeTaskExpandableAdapter;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.TaskListResponse;
import com.xxdc.itask.util.ITask;
import com.xxdc.itask.util.PreferenceHelper;

public class HomeFragment extends BaseFragment {
	public static int REFRESH_DATA = 2000;
	
	@ViewInject(R.id.swipe_view)
	private SwipeRefreshLayout mSwipeView;

	@ViewInject(R.id.list_view)
	private ExpandableListView mTaskListView;

	@ViewInject(R.id.shade)
	private View mShadeView;

	@ViewInject(R.id.title_text)
	private TextView mTitleText;

	private HomeTaskExpandableAdapter mHomeTaskAdapter;

	private PopupWindow mPopupWindow;
	
	private ListView mPullListView;

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return initView(inflater, R.layout.home_fragment, container);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REFRESH_DATA) {
			doPostGetTaskList();
		}
	}

	@Override
	protected void initUI(View v) {

		mSwipeView.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light,
				android.R.color.holo_green_light);
		// mSwipeView.setDistanceToTriggerSync(200);
		mHomeTaskAdapter = new HomeTaskExpandableAdapter(mActivity, mTaskListView);
		mHomeTaskAdapter.setHomeFragment(this);
		mTaskListView.setAdapter(mHomeTaskAdapter);
		mTaskListView.setGroupIndicator(null);
		mShadeView = mActivity.findViewById(R.id.shade);
		
		initPullsView();
		setListener();
		doPostGetTaskList();
	}

	/**
	 * 标题下拉菜单
	 * @param v
	 */
	@OnClick(R.id.title_text)
	private void clickTitle(View v) {
		mPopupWindow = new PopupWindow(mPullListView, v.getWidth(), LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.update();
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAsDropDown(v);
	}

	@OnClick(R.id.task_add_btn)
	private void clickTaskAdd(View v) {
		addTask(null);
	}

	public void addTask(UserTaskDTO task) {
		Intent i = new Intent(mActivity, TaskAddActivity.class);
		i.putExtra(TaskAddActivity.TASK_PARENT, task);
		startActivityForResult(i, REFRESH_DATA);
	}
	
	private void initPullsView() {
		mPullListView = (ListView) LayoutInflater.from(mActivity).inflate(R.layout.pulls_listview, null);
		SimpleAdapter adapter = new SimpleAdapter(mActivity, initPullsData(), R.layout.popupwindow_item, new String[]{"name"}, new int[]{R.id.text});
		mPullListView.setAdapter(adapter);
		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> data = (HashMap<String, String>) parent.getAdapter().getItem(position);
				mTitleText.setText(data.get("name"));
				switch (position) {
				case 0:
					setTaskListDate(0);
					break;
				case 3:
					setTaskListDate(2);
					break;
				case 4:
					setTaskListDate(1);
					break;
				case 5:
					setTaskListDate(3);
					break;
				default:
					break;
				}
				mPopupWindow.dismiss();
			}
			
		});
	}
	
	private List<? extends Map<String, ?>> initPullsData() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		String[] titles = getResources().getStringArray(R.array.home_title);
		for (String title : titles) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", title);
//			map.put(key, value)
			data.add(map);
		}
		return data;
	}

	private void setTaskListDate(int taskType) {
		try {
			DbUtils dbutils = ITaskApp.getInstance().getDbUtils();
			LinkedList<UserTaskDTO> parentTask = new LinkedList<UserTaskDTO>();
			List<UserTaskDTO> parentTask1 = dbutils.findAll(getTaskSelector(0, ITask.TASK_FLAG_NORMAL, Config.userId, taskType));
			List<UserTaskDTO> parentTask2 = dbutils.findAll(getTaskSelector(0, ITask.TASK_FLAG_DELETE, Config.userId, taskType));
			if (null != parentTask1 && parentTask1.size() != 0) {
				parentTask.addAll(parentTask1);
			}

			if (null != parentTask2 && parentTask2.size() != 0) {
				parentTask.addAll(parentTask2);
			}
			for (UserTaskDTO data : parentTask) {
				LinkedList<UserTaskDTO> childTask = new LinkedList<UserTaskDTO>();
				List<UserTaskDTO> childTask1 = dbutils.findAll(getTaskSelector(data.getUserTaskId(), ITask.TASK_FLAG_NORMAL, Config.userId, taskType));
				List<UserTaskDTO> childTask2 = dbutils.findAll(getTaskSelector(data.getUserTaskId(), ITask.TASK_FLAG_DELETE, Config.userId, taskType));
				if (null != childTask1 && childTask1.size() != 0) {
					childTask.addAll(childTask1);
				}

				if (null != childTask2 && childTask2.size() != 0) {
					childTask.addAll(childTask2);
				}
				data.setChildTask(childTask);
			}
			mHomeTaskAdapter.setList(parentTask);
			mTaskListView.setAdapter(mHomeTaskAdapter);
			int count = mTaskListView.getCount();
			for (int i = 0; i < count; i++) {
				mTaskListView.expandGroup(i);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
	
	private Selector getTaskSelector(long parentTaskId, int status, long userId, int taskType) {
		Selector selector;
		if (taskType == 0) {
			selector = Selector.from(UserTaskDTO.class).where("parentTaskId", "=", parentTaskId).and("status", "=", status).and("userId", "=", Config.userId);
		} else {
			selector = Selector.from(UserTaskDTO.class).where("parentTaskId", "=", parentTaskId).and("status", "=", status).and("userId", "=", Config.userId).and("taskType", "=", taskType);
		}
		return selector;
	}

	private void setListener() {
		mSwipeView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// doPostGetTaskList();
			}
		});
		mTaskListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// jumpDetail((UserTaskDTO)
				// mHomeTaskAdapter.getGroup(groupPosition));
				popupTools(v, (UserTaskDTO) mHomeTaskAdapter.getGroup(groupPosition));
				return true;
			}
		});

		mTaskListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// jumpDetail((UserTaskDTO)
				// mHomeTaskAdapter.getChild(groupPosition, childPosition));
				popupTools(v, (UserTaskDTO) mHomeTaskAdapter.getChild(groupPosition, childPosition));
				return true;
			}
		});
	}

	private void popupTools(View v, final UserTaskDTO userTask) {
		View view = mActivity.getLayoutInflater().inflate(R.layout.home_tools, null);
		mPopupWindow = new PopupWindow(view, v.getWidth(), 150, true);
		mPopupWindow.update();
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.showAsDropDown(v);

		// 定时器
		view.findViewById(R.id.add_timer).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TimerTaskActivity.luanch(HomeFragment.this, userTask);
				mPopupWindow.dismiss();
			}
		});

		// 回复
		view.findViewById(R.id.reply_img).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, FeedBackActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("detail", userTask);
				bundle.putString("flag", "2");
				intent.putExtra("task", bundle);
				startActivity(intent);
				mPopupWindow.dismiss();
			}
		});

		// 分配
		view.findViewById(R.id.allot_img).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent2 = new Intent(getActivity(), AllotActivity.class);
				intent2.putExtra("idName", userTask.getDoUsers());
				intent2.putExtra("taskId", userTask.getTaskId() + "");
				startActivity(intent2);
				mPopupWindow.dismiss();
			}
		});

		// 记事本
		view.findViewById(R.id.note_img).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null == userTask) {
					Toast.makeText(getActivity(), "请先创建您的记事本", 3000).show();
					return;
				}
				if (null != userTask.getNotepads() && userTask.getNotepads().size() > 0) {
					Toast.makeText(getActivity(), "您选择的任务暂时还没有记事本记录", 3000).show();
					return;
				}
				Intent intentNotePad = new Intent(getActivity(), HomeToShowNotepadActivity.class);
				Bundle bundleNote = new Bundle();
				bundleNote.putSerializable("notePads", userTask.getNotepads());
				intentNotePad.putExtra("userTaskId", userTask.getUserTaskId() + "");
				intentNotePad.putExtra("fromHitoryItem", bundleNote);
				startActivity(intentNotePad);
				mPopupWindow.dismiss();
			}
		});
		
		

//		// 详情
//		view.findViewById(R.id.detail_img).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				jumpDetail(userTask);
//				mPopupWindow.dismiss();
//			}
//		});
	}
	

	public void jumpDetail(UserTaskDTO userTask) {
		Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("history_item", userTask);
		intent.putExtra("history_item", bundle);
		startActivity(intent);
	}

	private void doPostGetTaskList() {
		ITaskApp.getInstance().getHttpClient().queryTaskList(PreferenceHelper.readString(mActivity, Config.FILE_CONFIG, Config.user), new Callback() {

			@Override
			public void onSuccess(Object o) {
				try {
					TaskListResponse response = (TaskListResponse) o;
					if (response != null) {
						for (UserTaskDTO task : response.getResponse().getUserTaskList()) {
							ITaskApp.getInstance().getDbUtils().saveOrUpdate(task);
							ITaskApp.getInstance().getDbUtils().saveOrUpdateAll(task.getFeebacks());
							ITaskApp.getInstance().getDbUtils().saveOrUpdateAll(task.getNotepads());
						}
						ITaskApp.getInstance().getDbUtils().saveAll(response.getResponse().getUserTaskList());
						PreferenceHelper.write(mActivity, Config.FILE_CONFIG, Config.user, String.valueOf(response.getResponse().getUpdateTime()));
						setTaskListDate(0);
					}
				} catch (DbException e) {
					e.printStackTrace();
				} finally {
					mLoadingDialog.dismiss();
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				setTaskListDate(0);
			}
		});

	}

}
