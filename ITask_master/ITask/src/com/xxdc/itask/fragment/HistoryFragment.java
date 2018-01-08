package com.xxdc.itask.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.HistoryItemActivity;
import com.xxdc.itask.adapter.HistoryTaskAdapter;
import com.xxdc.itask.adapter.HomeTaskExpandableAdapter;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.FindHistoryResponse;
import com.xxdc.itask.dto.response.HistoryTaskResponse;
import com.xxdc.itask.widget.RefreshLayout;
import com.xxdc.itask.widget.RefreshLayout.OnLoadListener;

/**
 * 历史任务模块
 * 
 * @author yxb
 * Modifier fch
 * 
 */
public class HistoryFragment extends BaseFragment implements View.OnClickListener {
	private ArrayList<UserTaskDTO> historyList = null;
	private HistoryTaskAdapter adapter;
	
	@ViewInject(R.id.list_history)
	private ExpandableListView list_history;
	
	@ViewInject(R.id.swipe_view)
	private SwipeRefreshLayout mSwipeView;
	@ViewInject(R.id.et_timeStart)
	private EditText et_timeStart;
	@ViewInject(R.id.et_timeEnd)
	private EditText et_timeEnd;
	@ViewInject(R.id.iv_search)
	private ImageView iv_search;
	@ViewInject(R.id.ll_start)
	private LinearLayout ll_start;
	@ViewInject(R.id.ll_end)
	private LinearLayout ll_end;
	@ViewInject(R.id.et_timeMonth)
	private EditText et_timeMonth;
	@ViewInject(R.id.et_keyword)
	private EditText et_keyword;
	@ViewInject(R.id.iv_reduce)
	private ImageView iv_reduce;
	@ViewInject(R.id.iv_add)
	private ImageView iv_add;
	@ViewInject(R.id.iv_search_do)
	private ImageView iv_search_do;
	@ViewInject(R.id.iv_back)
	private ImageView iv_back;
	@ViewInject(R.id.rl_search)
	private RelativeLayout rl_search;
	@ViewInject(R.id.search_month)
	private LinearLayout search_month;
	private int offset = 1;
	private int offsetFind = 1;
	private Map<Long, String> childTasks = new HashMap<Long, String>(0);
	private String parentTaskName = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return initView(inflater, R.layout.history_fragment, container);
	}

	@Override
	protected void initUI(View v) {
		adapter = new HistoryTaskAdapter(getActivity(), list_history);
		list_history.setAdapter(adapter);
		mSwipeView.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_blue_light, android.R.color.holo_blue_light,
				android.R.color.holo_blue_light);
		setListener();
		setDate();
		// getHistory();
		final String year = et_timeMonth.getText().toString().trim().split("-")[0];
		final String month = et_timeMonth.getText().toString().trim().split("-")[1];
		String offset = 1 + "";
		getHistoryTest(year, month, offset);
	}

	@SuppressLint("SimpleDateFormat")
	private void setDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String strDate = sdf.format(date);
		et_timeStart.setText(strDate);
		et_timeEnd.setText(strDate);
		et_timeMonth.setText(strDate.substring(0, 7));
	}

	@Override
	public void onResume() {
		super.onResume();
		// if (iv_search.getVisibility() == View.GONE) {
		// iv_search.setVisibility(View.VISIBLE);
		// }
	}

	private void setListener() {
		list_history.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub
				jumpHistory(adapter.getGroup(groupPosition));
				return true;
			}
		});
		
		list_history.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				jumpHistory(adapter.getChild(groupPosition, childPosition));
				return true;
			}
		});
		
		list_history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// HistoryItemActivity.launch(getActivity());
			}
		});
		// setRefresh();
		mSwipeView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (iv_search.getVisibility() == View.VISIBLE) {
					String strDate = et_timeMonth.getText().toString().trim() + "-01";
					// et_timeMonth.setText(addDay(strDate, 1).substring(0, 7));
					// ++offset;
					getHistoryTest(strDate.split("-")[0], strDate.split("-")[1], offset + "");
				}
				if (iv_search.getVisibility() == View.GONE) {
					// ++offsetFind;
					getHistoryByTime(offsetFind);
				}
				// mSwipeView.setRefreshing(false);
				// new Handler().postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				// mSwipeView.setRefreshing(false);
				// }
				// }, 2000);
			}
		});
		et_timeStart.setOnClickListener(this);
		et_timeEnd.setOnClickListener(this);
		iv_search.setOnClickListener(this);
		iv_add.setOnClickListener(this);
		iv_reduce.setOnClickListener(this);
		iv_search_do.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}
	
	private void jumpHistory(UserTaskDTO task) {
		Intent intent = new Intent(getActivity(), HistoryItemActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("history_item", task);
		intent.putExtra("history_item", bundle);
		startActivity(intent);
	}

	private void setRefresh() {
		// 设置下拉刷新监听器
		mSwipeView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				Toast.makeText(getActivity(), "refresh", Toast.LENGTH_SHORT).show();

				mSwipeView.postDelayed(new Runnable() {

					@Override
					public void run() {
						// 更新数据
						// datas.add(new Date().toGMTString());
						adapter.notifyDataSetChanged();
						// 更新完后调用该方法结束刷新
						mSwipeView.setRefreshing(false);
					}
				}, 1000);
			}
		});

		// 加载监听器
		// mSwipeView.setOnLoadListener(new OnLoadListener() {
		//
		// @Override
		// public void onLoad() {
		//
		// Toast.makeText(getActivity(), "load", Toast.LENGTH_SHORT).show();
		//
		// mSwipeView.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // datas.add(new Date().toGMTString());
		// adapter.notifyDataSetChanged();
		// // 加载完后调用该方法
		// mSwipeView.setLoading(false);
		// }
		// }, 1500);
		//
		// }
		// });

	}
	
	private void setHistoryListData() {
		
	}

	private void getHistoryTest(final String year, final String month, String offset) {

		if (("").equals(Config.token)) {
			return;
		}
		String token = Config.token;
		// String token = "NzEzfDM2eXVmbmI0cGJ8YWRtaW4xfDEyMw";
		String limit = 20 + "";
		ITaskApp.getInstance().getHttpClient().getHistoryTaskListTest(token, year, month, offset, limit, childTasks, parentTaskName, new Callback() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onSuccess(Object o) {
				if (mSwipeView.isRefreshing()) {
					mSwipeView.setRefreshing(false);
				}
				mLoadingDialog.dismiss();
				Log.i("history", " 获取历史列表成功");
				historyList = ((HistoryTaskResponse) o).getResponse();
				if (null != historyList) {
					LinkedList<UserTaskDTO> parentList = new LinkedList<UserTaskDTO>();
					for (UserTaskDTO task : historyList) {
						if (task.getParentTaskId() == 0) {
							parentList.add(task);
						}
					}
					
					for (UserTaskDTO parent : parentList) {
						LinkedList<UserTaskDTO> childList = new LinkedList<UserTaskDTO>();
						for (UserTaskDTO task : historyList) {
							if (task.getParentTaskId() == parent.getUserTaskId()) {
								childList.add(task);
							}
						}
						parent.setChildTask(childList);
					}
					adapter.setList(parentList);
					Log.i("history", "historyList=" + historyList.toString());
					createHistoryDataBase(historyList);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String date = sdf.format(new Date());
					String currentYear = date.split("-")[0];
					String currentMonth = date.split("-")[1];
					Log.i("date", "date=" + date);
					if (Integer.parseInt(year) < Integer.parseInt(currentYear)
							|| (Integer.parseInt(year) == Integer.parseInt(currentYear) && Integer.parseInt(month) < Integer.parseInt(currentMonth))) {
						Log.i("date", "应该创建数据库");
					}
				} else {
					Log.i("history", "historyList=null");
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
				Log.i("history", " 获取历史列表开始");

			}

			@Override
			public void onFailure(Object o) {
				if (mSwipeView.isRefreshing()) {
					mSwipeView.setRefreshing(false);
				}
				mLoadingDialog.dismiss();
				Log.i("history", " 获取历史列表失败");

			}
		});

	}

	private void createHistoryDataBase(List<UserTaskDTO> historyList) {
		DbUtils db = ITaskApp.getInstance().getDbUtils();
		// User user = new User(); //这里需要注意的是User对象必须有id属性，或者有通过@ID注解的属性
		try {
			db.saveOrUpdateAll(historyList);
			Log.i("date", "数据保存成功");
			// for (int i = 0; i < historyList.size(); i++) {
			// Log.i("date", "数据保存成功");
			// }
		} catch (DbException e) {
			e.printStackTrace();
		} // 使用saveBindingId保存实体时会为实体的id赋值
			// 查找
			// UserTaskDTO entity = db.findById(UserTaskDTO.class,
			// UserTaskDTO.getId());
		try {
			// 通过类型查找
			List<UserTaskDTO> list = db.findAll(UserTaskDTO.class);
			// Log.i("查询成功", "historyList=" + list.toString());
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private void getHistory() {
		String u = Config.user;
		String p = Config.pwd;
		String pagesize = 1 + "";
		String startTime = null;
		String endTime = null;
		String keyWord = null;
		ITaskApp.getInstance().getHttpClient().getHistoryTaskList(u, p, pagesize, startTime, endTime, keyWord, new Callback() {

			@Override
			public void onSuccess(Object o) {
				Log.i("history", " 获取历史列表成功");
				mLoadingDialog.dismiss();

			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
				Log.i("history", " 获取历史列表开始");

			}

			@Override
			public void onFailure(Object o) {
				mLoadingDialog.dismiss();
				Log.i("history", " 获取历史列表失败");

			}
		});

	}

	/**
	 * 显示日期选择器
	 */
	public void showDatePicker(Context context, final EditText time) {
		Calendar c = Calendar.getInstance();
		// 直接创建一个DatePickerDialog对话框实例，并将它显示出来
		new DatePickerDialog(context,
		// 绑定监听器
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
						String m = (month + 1) + "";
						String d = dayOfMonth + "";
						if ((month + 1) / 10 == 0) {
							m = "0" + m;
						}
						if (dayOfMonth / 10 == 0) {
							d = "0" + d;
						}
						time.requestFocus();
						time.setText(year + "-" + m + "-" + d);
					}
				}
				// 设置初始日期
				, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
	}

	public void onClick(View v) {
		String strDate = et_timeMonth.getText().toString().trim() + "-01";
		switch (v.getId()) {
		case R.id.et_timeStart:
			showDatePicker(getActivity(), et_timeStart);
			break;
		case R.id.et_timeEnd:
			showDatePicker(getActivity(), et_timeEnd);
			break;
		case R.id.iv_search:
			if (rl_search.getVisibility() == View.GONE) {
				rl_search.setVisibility(View.VISIBLE);
				iv_search.setVisibility(View.GONE);
				// ll_end.setVisibility(View.VISIBLE);
			} else {
				// rl_search.setVisibility(View.GONE);
				// ll_end.setVisibility(View.GONE);
			}
			if (search_month.getVisibility() == View.VISIBLE) {
				search_month.setVisibility(View.GONE);
			}
			if (iv_back.getVisibility() == View.GONE) {
				iv_back.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.iv_add:
			String date = addDay(strDate, 1).substring(0, 7);
			et_timeMonth.setText(date);
			int offset = 1;
			getHistoryTest(date.split("-")[0], date.split("-")[1], offset + "");
			break;
		case R.id.iv_reduce:
			String date2 = addDay(strDate, -1).substring(0, 7);
			et_timeMonth.setText(date2);
			int offset2 = 1;
			getHistoryTest(date2.split("-")[0], date2.split("-")[1], offset2 + "");
			break;
		case R.id.iv_search_do:
			getHistoryByTime(offsetFind);
			// if (rl_search.getVisibility() == View.VISIBLE) {
			// iv_search.setVisibility(View.VISIBLE);
			// rl_search.setVisibility(View.GONE);
			// }
			// if (iv_back.getVisibility() == View.VISIBLE) {
			// iv_back.setVisibility(View.GONE);
			// }
			break;
		case R.id.iv_back:
			if (iv_search.getVisibility() == View.GONE) {
				iv_search.setVisibility(View.VISIBLE);
				rl_search.setVisibility(View.GONE);
				search_month.setVisibility(View.VISIBLE);
				iv_back.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}

	}

	private void getHistoryByTime(int offsetFind) {
		// String u = Config.user;
		// String p = Config.pwd;
		String token = Config.token;
		String keyword = "";
		if (null != (et_keyword.getText().toString().trim())) {
			keyword = et_keyword.getText().toString().trim();
		}
		long startDate = getDate(et_timeStart.getText().toString().trim());
		long endDate = getDate(et_timeEnd.getText().toString().trim());
		int offset = offsetFind;
		int limit = 20;
		ITaskApp.getInstance().getHttpClient().findHistoryTaskListItem(token, keyword, startDate, endDate, offset, limit, new Callback() {

			@Override
			public void onSuccess(Object o) {
				if (mSwipeView.isRefreshing()) {
					mSwipeView.setRefreshing(false);
				}
				mLoadingDialog.dismiss();
				Log.i("findHistory", "根据条件查询历史任务成功");
				if (null != ((HistoryTaskResponse) o).getResponse()) {
					historyList = ((HistoryTaskResponse) o).getResponse();
//					adapter.setList(historyList);
				}
			}

			@Override
			public void onStart() {
				mLoadingDialog.show();
				Log.i("findHistory", "根据条件查询历史任务开始");

			}

			@Override
			public void onFailure(Object o) {
				if (mSwipeView.isRefreshing()) {
					mSwipeView.setRefreshing(false);
				}
				mLoadingDialog.dismiss();
				Log.i("findHistory", "根据条件查询历史任务失败");

			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private long getDate(String time) {
		if (null == time || ("").equals(time)) {
			Toast.makeText(getActivity(), "请选择正确的日期", 3000).show();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 继续转换得到秒数的long型
		long lTime = dt.getTime();
		Log.i("long time", "ITime=" + lTime + "");
		return lTime;
	}

	// 实现日期加一个月的方法
	@SuppressLint("SimpleDateFormat")
	public static String addDay(String s, int n) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(s));
			// cd.add(Calendar.DATE, n);// 增加一天
			cd.add(Calendar.MONTH, n);// 增加一个月
			return sdf.format(cd.getTime());

		} catch (Exception e) {
			return null;
		}
	}

}
