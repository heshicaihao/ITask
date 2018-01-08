package com.xxdc.itask.dto.net;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.xxdc.itask.activity.BaseActivity;
import com.xxdc.itask.dto.response.Response;

public class MyRequestCallBack<T> extends RequestCallBack<T> {

	private Callback mCallBack;

	private Class<T> mClazz;

	private Context mContext;

	public MyRequestCallBack(Context context, Callback callback, Class<T> clazz) {
		mContext = context;
		mCallBack = callback;
		mClazz = clazz;
	}

	@Override
	public void onFailure(HttpException arg0, String arg1) {
		if (mCallBack != null) {
			LogUtils.i("连接服务器失败!");
			arg0.printStackTrace();
			mCallBack.onFailure(arg0);
		}
	}

	@Override
	public void onSuccess(ResponseInfo<T> responseInfo) {
		if (mCallBack != null) {
			try {
				LogUtils.i(responseInfo.result.toString());
				Gson gson = new Gson();
				Response response = gson.fromJson(responseInfo.result.toString(), Response.class);
				if (response.getStatus()) {
					mCallBack.onSuccess(gson.fromJson(responseInfo.result.toString(), mClazz));
					
				} else {
					String[] messages = response.getMessage().split(":");
					//代表token已过期的返回码  发送广播在基类中处理
					if (messages[0].equals("1401")) {
						Toast.makeText(mContext, messages[1], Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
						intent.setAction(BaseActivity.TOKEN_PAST);
						mContext.sendBroadcast(intent);
					} else {
						Toast.makeText(mContext, messages[messages.length - 1], Toast.LENGTH_LONG).show();
					}
					mCallBack.onFailure(response.getMessage());
				}
				
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				mCallBack.onFailure(null);
			}
		}
	}

	@Override
	public void onStart() {
		if (mCallBack != null) {
			mCallBack.onStart();
		}
	}
}
