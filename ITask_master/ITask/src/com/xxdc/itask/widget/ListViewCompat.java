package com.xxdc.itask.widget;

import com.xxdc.itask.dto.UserTaskDTO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ExpandableListView;

public class ListViewCompat extends ExpandableListView {

	private static final String TAG = "ListViewCompat";

	private SlideView mFocusedItemView;

	public ListViewCompat(Context context) {
		super(context);
	}

	public ListViewCompat(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewCompat(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void shrinkListItem(int position) {
		View item = getChildAt(position);

		if (item != null) {
			try {
				((SlideView) item).shrink();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility") 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			int x = (int) event.getX();
			int y = (int) event.getY();
			int position = pointToPosition(x, y);

			if (position != INVALID_POSITION) {
				UserTaskDTO task = (UserTaskDTO) getItemAtPosition(position);
				mFocusedItemView = (SlideView) task.getSlideView();
				Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
			}
		}

		default:
			break;
			
		}
		if (mFocusedItemView != null && getCount() != 0) {
			boolean isScroll = mFocusedItemView.onRequireTouchEvent(event);
			if (isScroll) {
				event.setAction(MotionEvent.ACTION_CANCEL);
			}
		}
		return super.onTouchEvent(event);
	}

}
