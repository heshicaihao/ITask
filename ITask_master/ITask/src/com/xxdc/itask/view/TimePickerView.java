package com.xxdc.itask.view;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xxdc.itask.R;

public class TimePickerView extends LinearLayout {
	
	@ViewInject(R.id.hour_text)
	private TextView mHourText;
	
	@ViewInject(R.id.minute_text)
	private TextView mMinuteText;

	public TimePickerView(Context context) {
		this(context, null);
	}

	public TimePickerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TimePickerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.time_picker_layout, this);
		ViewUtils.inject(this);
		Calendar c = Calendar.getInstance();
		setHour(c.get(Calendar.HOUR));
		setMinute(c.get(Calendar.MINUTE));
	}
	
	@OnClick(R.id.hour_up_btn)
	private void clickHourUp(View v) {
		setHour(getHour() + 1); 
	}

	@OnClick(R.id.hour_down_btn)
	private void clickHourDown(View v) {
		setHour(getHour() - 1); 
	}

	@OnClick(R.id.minute_up_btn)
	private void clickMinuteUp(View v) {
		setMinute(getMinute() + 1);
	}

	@OnClick(R.id.minute_down_btn)
	private void clickMinuteDown(View v) {
		setMinute(getMinute() - 1);
	}
	
	public void setHour(int hour) {
		String h = String.valueOf(hour);
		if (hour == 24) {
			h = "00";
		} else if (hour == -1) {
			h = "23";
		} else {
			if (h.length() == 1) {
				h = "0" + h;
			}
		}
		mHourText.setText(h);
	}
	
	public void setMinute(int minute) {
		String m = String.valueOf(minute);
		if (minute == 60) {
			m = "00";
			setHour(getHour() + 1);
		} else if(minute == -1) {
			m = "59";
			setHour(getHour() - 1);
		} else {
			if (m.length() == 1) {
				m = "0" + m;
			}
		}
		mMinuteText.setText(m);
	}
	
	public int getHour() {
		return Integer.valueOf(mHourText.getText().toString());
	}
	
	public int getMinute() {
		return Integer.valueOf(mMinuteText.getText().toString());
	}
}
