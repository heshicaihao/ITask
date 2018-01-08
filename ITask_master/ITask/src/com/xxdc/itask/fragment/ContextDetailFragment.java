package com.xxdc.itask.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.UserListForGroupDTO;


public class ContextDetailFragment extends BaseFragment {
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

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i("onResume");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return initView(inflater, R.layout.context_detail_fragment, container);
	}

	@SuppressLint("NewApi")
	@Override
	protected void initUI(View v) {
		Bundle bundle = getArguments();
		if (null != bundle) {
			UserListForGroupDTO user = (UserListForGroupDTO) bundle.getSerializable("user");
			String imgHeader = bundle.getString("headString");
			aq.id(mHead).image(imgHeader,true, true, 0, R.drawable.drawable);
			mName.setText(user.getRealName());
			mMobile.setText(user.getTel());
			mEmail.setText(user.getEmail());
			mTel.setText(user.getPhone());
		}
	}
	

}
