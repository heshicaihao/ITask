package com.xxdc.itask.dialog;

import com.xxdc.itask.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomAlertDialog {

	private AlertDialog dialog;
	private LayoutInflater inflater;
	private TextView leftBtn, rightBtn;
	private LinearLayout titleLay, bottomLay;
	private TextView title, contentMsg;
	private Context context;
	private View dialogView;

	public CustomAlertDialog(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		dialogView = inflater.inflate(R.layout.exit_confirm_dialog, null);
		bottomLay = (LinearLayout) dialogView.findViewById(R.id.btnLayout);
		dialog = new AlertDialog.Builder(context).create();
	}

	public void show() {
		dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent arg2) {
				 return (keyCode == KeyEvent.KEYCODE_SEARCH);
			}
		});
		dialog.show();
		dialog.getWindow().setContentView(dialogView);
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}

	}

	public boolean isShowing() {
		if (dialog != null)

			return dialog.isShowing();
		return false;
	}

	public void setCancelable(boolean bool) {
		dialog.setCancelable(bool);

	}

	public void setMessage(String msg) {
		contentMsg = (TextView) dialogView.findViewById(R.id.dialogText);
		contentMsg.setText(msg);
		contentMsg.setVisibility(View.VISIBLE);
	}

	public void setTitle(String title) {
		this.title = (TextView) dialogView.findViewById(R.id.dialogTitle);
		this.title.setText(title);

	}

	public void setOnPositiveButton(String str, View.OnClickListener listener) {

		bottomLay.setVisibility(View.VISIBLE);
		leftBtn = (TextView) dialogView.findViewById(R.id.dialogLeftBtn);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setText(str);
		leftBtn.setOnClickListener(listener);
	}

	public void setOnNegativeButton(String str, View.OnClickListener listener) {
		bottomLay.setVisibility(View.VISIBLE);
		rightBtn = (TextView) dialogView.findViewById(R.id.dialogRightBtn);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setText(str);
		rightBtn.setOnClickListener(listener);

	}

}
