package com.xxdc.itask.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.R;

public class TabIndicator extends RelativeLayout{
	@ViewInject(R.id.bottom_tab_title)
	private TextView mTabText;
	
	@ViewInject(R.id.bottom_tab_icon)
	private ImageView mTabImage;
	
	public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public TabIndicator(Context context,int strId, int imgId) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.bottom_tab_indicator, this);
		ViewUtils.inject(this);
		mTabText.setText(strId);
		mTabImage.setBackgroundResource(imgId);
	}
	
}
