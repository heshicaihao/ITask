package com.xxdc.itask.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.FenPeiAdapter;

public class FenPeiActivity extends BaseActivity {
	private ArrayList<String> name_list = null;
	@ViewInject(R.id.list_fenpei)
	private ListView list_fenpei;
	FenPeiAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_fenpei);
		initUI();
	}

	@Override
	protected void initUI() {
		getFenPeiRen();
		adapter = new FenPeiAdapter(this);
		list_fenpei.setAdapter(adapter);
		adapter.setList(name_list);
	}

	private ArrayList<String> getFenPeiRen() {
		name_list = new ArrayList<String>();
		String data = getIntent().getStringExtra("fenpei");
		if (!("").equals(data) && null != data) {
			for (int i = 0; i < data.split(",").length; i++) {
				name_list.add(data.split(",")[i]);
			}
		}
		return name_list;
	}

}
