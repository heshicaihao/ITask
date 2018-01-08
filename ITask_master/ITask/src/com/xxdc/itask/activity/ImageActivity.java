package com.xxdc.itask.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;

public class ImageActivity extends BaseActivity {
	@ViewInject(R.id.iv_image)
	private ImageView iv_image;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_image);
	}

	@Override
	protected void initUI() {
		String url = getIntent().getStringExtra("imageurl");
		ITaskApp.getInstance().getBitmpUtils().display(iv_image, url);
	}

}
