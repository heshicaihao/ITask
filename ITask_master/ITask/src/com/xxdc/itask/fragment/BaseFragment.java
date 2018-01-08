package com.xxdc.itask.fragment;


import com.androidquery.AQuery;
import com.lidroid.xutils.ViewUtils;
import com.xxdc.itask.dialog.LoadingDialog;
import com.xxdc.itask.activity.BaseActivity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public static final int SELECT_PICTRUE = 1000;

	public static final int PIC_TO_VIEW = 1002;

	protected BaseActivity mActivity;
	protected LoadingDialog mLoadingDialog;
	protected FragmentManager mFragmentManager;
	protected AQuery aq;
	/**
	 * 
	 * 缓存视图
	 */
	protected View mView;
	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (BaseActivity) activity;
		aq = new AQuery(mActivity);
	}

	protected View initView(LayoutInflater inflater, int layoutId, ViewGroup container) {
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		} else {
			mView = inflater.inflate(layoutId, container, false);
			ViewUtils.inject(this, mView);
			mLoadingDialog = mActivity.getLoadingDialog();
			mFragmentManager = mActivity.getFM();
			initUI(mView);
		}
		return mView;
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}


	protected abstract void initUI(View v);
	
	@Override
	public void onDestroy() {
		try {
			mLoadingDialog.dismiss();
			System.out.println(">>>>>>>>>>fragment的onDestory");
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
}
