package com.xxdc.itask.dialog;

import java.io.File;

import com.xxdc.itask.R;
import com.xxdc.itask.activity.HomeActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

public class PictureSelectorDialog extends Dialog {
	public final static int SELECT_PICTRUE = 1000;
	private TextView tv_selector;// 选择照片
	private TextView tv_carema;// 照相
	private TextView tv_cancel;// 取消
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
		setContentView(R.layout.dialog_picture_selector);
		tv_selector = (TextView) findViewById(R.id.picture_tv_selector);
		tv_carema = (TextView) findViewById(R.id.picture_tv_carema);
		tv_cancel = (TextView) findViewById(R.id.picture_tv_cancel);
		myClickListener = new MyClickListener();
		setClickListener();
	}

	public PictureSelectorDialog(Activity activity, int theme) {
		super(activity, theme);
		mActivity = activity;
		setupView();
	}
	
	public PictureSelectorDialog(Context f, int theme){
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
		tv_selector.setOnClickListener(myClickListener);
		tv_carema.setOnClickListener(myClickListener);
		tv_cancel.setOnClickListener(myClickListener);
	}

	public PictureSelectorDialog(Context context) {
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
			switch (arg0.getId()) {
			case R.id.picture_tv_selector:
				dismiss();
				Intent photoIntent = new Intent(Intent.ACTION_PICK);
				photoIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				mActivity.startActivityForResult(photoIntent, HomeActivity.SELECT_PICTRUE);
				// Intent photoIntent = new Intent(Intent.ACTION_GET_CONTENT);
				// photoIntent.setType("image/*");
				// photoIntent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()),
				// "image/*");
				break;
			case R.id.picture_tv_carema:
				dismiss();
				File file = new File(Environment.getExternalStorageDirectory(), "textphoto.jpg");
				Uri outputFileUri = Uri.fromFile(file);
				Intent caremaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				caremaIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
				mActivity.startActivityForResult(caremaIntent, HomeActivity.SELECT_PHOTO);
				break;
			case R.id.picture_tv_cancel:
				dismiss();
				break;
			default:
				break;
			}
		}
	}

}
