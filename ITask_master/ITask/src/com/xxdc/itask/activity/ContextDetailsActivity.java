package com.xxdc.itask.activity;

import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.UserListForGroupDTO;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 联系人详情
 * @author Administrator
 *
 */
public class ContextDetailsActivity extends BaseActivity{
	@ViewInject(R.id.iv_head_context_detail)
	private ImageView mHead;
	@ViewInject(R.id.tv_name_context_detail)
	private TextView mName;
	@ViewInject(R.id.tv_mobile_context_detail)
	private TextView mMobile;
	@ViewInject(R.id.tv_email_context_detail)
	private TextView mEmail;
	@ViewInject(R.id.tv_tel_context_detail)
	private TextView mTel;
	@ViewInject(R.id.tv_cancle_context_detail)
	private TextView mCancle;
	@ViewInject(R.id.title_name)
	private TextView mTitle;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.context_detail_fragment);
	}
	@Override
	protected void initUI() {
		
		Bundle bundle = getIntent().getExtras();
		System.out.println(">>>>>>>>>bundle"+bundle);
		if (null != bundle) {
			UserListForGroupDTO user = (UserListForGroupDTO) bundle.getSerializable("user");
			String imgHeader = bundle.getString("headString");
			aq.id(mHead).image(imgHeader,true, true, 0, R.drawable.drawable);
			mTitle.setText(user.getRealName());
			mName.setText(user.getRealName());		
			mMobile.setText(user.getTel());
			mEmail.setText(user.getEmail());
			mTel.setText(user.getPhone());
		}
	}

}
