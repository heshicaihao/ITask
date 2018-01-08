package com.xxdc.itask.fragment;

import com.xxdc.itask.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MoreFragment extends BaseFragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return initView(inflater, R.layout.home_fragment, container);
	}
	
	@Override
	protected void initUI(View v) {
		
	}
}
