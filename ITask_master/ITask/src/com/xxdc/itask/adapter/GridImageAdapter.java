package com.xxdc.itask.adapter;

import java.util.List;

import com.xxdc.itask.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridImageAdapter extends BaseAdapter {
	private Context context;
	private List<Drawable> lists;
	private CallBack callBack;
	private int ROW_NUMBER = 3;
	GridView mGridView;

	public GridImageAdapter() {

	}

	public GridImageAdapter(Context context, GridView mGv, List<Drawable> lists, CallBack callBack) {
		this.context = context;
		this.lists = lists;
		this.callBack = callBack;
		this.mGridView = mGv;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater flater = LayoutInflater.from(context);

			view = flater.inflate(R.layout.gridview_image, null);
			holder.mClose = (ImageView) view.findViewById(R.id.iv_close);
			holder.mImg = (ImageView) view.findViewById(R.id.iv_image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.mImg.setBackground(lists.get(position));
		if (lists.size() - 1 == position) {
			holder.mClose.setVisibility(View.GONE);
		} else {
			holder.mClose.setVisibility(View.VISIBLE);
		}
		holder.mClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callBack != null) {
					lists.remove(position);
					notifyDataSetChanged();
					callBack.callBack(position);
				}
			}
		});
		AbsListView.LayoutParams param = new AbsListView.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, 420 / ROW_NUMBER);
		view.setLayoutParams(param);
		return view;
	}

	private static class ViewHolder {
		ImageView mImg;
		ImageView mClose;
	}

	public interface CallBack {
		void callBack(int position);
	}

}
