package com.xxdc.itask.dialog;

import java.io.File;

import com.xxdc.itask.R;
import com.xxdc.itask.activity.HomeActivity;




import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;


public class SureDeleteDialog extends Dialog {
	public final static int SELECT_PICTRUE = 1000;
	private TextView tv_delete;// 删除图片
	private TextView tv_check;// 查看大图
	private MyClickListener myClickListener;

	private Activity mActivity;
	private Context mFragment;

	/**
	 * void
	 * 
	 * @Description:初始化控件
	 * @throws
	 */
	private void setupView() {
		setContentView(R.layout.dialog_delete_selector);
		tv_delete = (TextView) findViewById(R.id.picture_delete);
		tv_check = (TextView) findViewById(R.id.picture_look);
		myClickListener = new MyClickListener();
		setClickListener();
	}

	public SureDeleteDialog(Activity activity, int theme) {
		super(activity, theme);
		mActivity = activity;
		setupView();
	}
	
	public SureDeleteDialog(Context f, int theme){
		super(f, theme);
		mFragment = f;
		setupView();
	}
	
	

	/**
	 * void
	 * 
	 * @Description:设置监听事件
	 * @throws
	 */
	private void setClickListener() {
		tv_delete.setOnClickListener(myClickListener);
		tv_check.setOnClickListener(myClickListener);
	}

	public SureDeleteDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @ClassName: MyClickListener
	 * @Description: 实现监听事件
	 * @author zs
	 * @date 2014-10-20 下午4:28:09
	 * 
	 */
	private class MyClickListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			
		}
	}

}
