package com.xxdc.itask.activity;

import hirondelle.date4j.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CalendarHelper;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.UserTaskDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.fragment.HomeFragment;
import com.xxdc.itask.view.TimePickerView;

public class TimerTaskActivity extends BaseActivity {

	// @ViewInject(R.id.calendar_view)
	// private CalendarView mCalendarView;

	// @ViewInject(R.id.save_btn)
	// private Button mSaveBtn;
	
	@ViewInject(R.id.timePicker)
	private TimePickerView mTimePicker;

	private CaldroidFragment mCaldroidFragment;
	
	private Date mSelecteDate = new Date(System.currentTimeMillis());
	
	private UserTaskDTO mTask;
	
	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");

	/**
	 * 上次被选中的view
	 */
	private View mPrevSelectedView = null;

	public static void luanch(Fragment fragment, UserTaskDTO task) {
		Intent i = new Intent(fragment.getActivity(), TimerTaskActivity.class);
		i.putExtra("task", task);
		fragment.startActivityForResult(i, HomeFragment.REFRESH_DATA);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_task_activity);
		//初始化日期
		mTask = (UserTaskDTO) getIntent().getSerializableExtra("task");
		Calendar cal = Calendar.getInstance();
		if (mTask.getTaskClock() != 0) {
			cal.setTimeInMillis(mTask.getTaskClock());
		}
		mTimePicker.setHour(cal.get(Calendar.HOUR));
		mTimePicker.setMinute(cal.get(Calendar.MINUTE));
		mSelecteDate = cal.getTime();
		
		// If Activity is created after rotation
		mCaldroidFragment = new CaldroidFragment();
		if (savedInstanceState != null) {
			mCaldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
		} else {
			Bundle args = new Bundle();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, true);
			ArrayList<DateTime> selectedDate = new ArrayList<DateTime>();
			selectedDate.add(CalendarHelper.convertDateToDateTime(cal.getTime()));
			args.putStringArrayList(CaldroidFragment.SELECTED_DATES,  CalendarHelper.convertToStringList(selectedDate));
			// Uncomment this to customize startDayOfWeek
			// args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
			// CaldroidFragment.TUESDAY); // Tuesday

			// Uncomment this line to use Caldroid in compact mode
			// args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

			mCaldroidFragment.setArguments(args);

		}
		// setCustomResourceForDates();
		replaceFragment(R.id.calendar_view, mCaldroidFragment);
		setListener();
	}

	@Override
	protected void initUI() {
	}
	
	@OnClick(R.id.sure_btn)
	private void clickOK(View v) {
		int hour = mTimePicker.getHour();
		int minute = mTimePicker.getMinute();
		
		Calendar c = Calendar.getInstance();
		c.setTime(mSelecteDate);
		c.set(Calendar.HOUR, hour);
		c.set(Calendar.MINUTE, minute);
		LogUtils.i(formatter.format(c.getTime()));
		doPostSetclock(String.valueOf(mTask.getUserTaskId()), String.valueOf(c.getTimeInMillis()));
	}

	// @OnClick(R.id.save_btn)
	// private void clickSave(View v) {
	// }

	private void setListener() {
		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				// Toast.makeText(getApplicationContext(),
				// formatter.format(date), Toast.LENGTH_SHORT).show();
				if (mPrevSelectedView != null) {
					mPrevSelectedView.setBackgroundResource(R.color.white);
				}
				
				if (CalendarHelper.convertDateToDateTime(new Date()).equals(CalendarHelper.convertDateToDateTime(date))){
					mPrevSelectedView = null;
				} else {
					mPrevSelectedView = view;
					view.setBackgroundResource(R.color.blue);
				}
				mSelecteDate = date;
			}

			@Override
			public void onChangeMonth(int month, int year) {
//				String text = "month: " + month + " year: " + year;
//				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLongClickDate(Date date, View view) {
//				Toast.makeText(getApplicationContext(), "Long click " + formatter.format(date), Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onCaldroidViewCreated() {
				// if (mCaldroidFragment.getLeftArrowButton() != null) {
				// Toast.makeText(getApplicationContext(),
				// "Caldroid view is created", Toast.LENGTH_SHORT).show();
				// }
			}

		};
		mCaldroidFragment.setCaldroidListener(listener);
	}

	private void setCustomResourceForDates() {
		Calendar cal = Calendar.getInstance();

		// Min date is last 7 days
		cal.add(Calendar.DATE, -18);
		Date blueDate = cal.getTime();

		// Max date is next 7 days
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 16);
		Date greenDate = cal.getTime();

		if (mCaldroidFragment != null) {
			mCaldroidFragment.setBackgroundResourceForDate(R.color.blue, blueDate);
			mCaldroidFragment.setBackgroundResourceForDate(R.color.green, greenDate);
			mCaldroidFragment.setTextColorForDate(R.color.white, blueDate);
			mCaldroidFragment.setTextColorForDate(R.color.white, greenDate);
		}
	}
	
	private void doPostSetclock(String taskId, String date) {
		ITaskApp.getInstance().getHttpClient().setClock(taskId, date, new Callback() {
			
			@Override
			public void onSuccess(Object o) {
//				Intent intent = new Intent();
//				intent.putExtra("hello", "hello");
//				setResult(HomeFragment.REFRESH_DATA, intent);
				finish();
			}
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFailure(Object o) {
				
			}
		});
	}
	
	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
}
