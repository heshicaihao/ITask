package com.xxdc.itask.dialog;

import com.xxdc.itask.R;

import android.app.Dialog;
import android.content.Context;

public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.loading);
	}

	public LoadingDialog(Context context) {
		super(context,R.style.MyAlertDialog);
		setContentView(R.layout.loading);
		setCancelable(false);
	}

}
