package com.xxdc.itask.adapter;

import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.xxdc.itask.R;

public class NotepadPicGVAdapter extends BaseAdapter {
	private List<Bitmap> photos;
	private Activity context;

	public NotepadPicGVAdapter(List<Bitmap> photos, Activity context) {
		this.context = context;
		this.photos = photos;
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = context.getLayoutInflater().inflate(R.layout.notepadadd_listview_item, null);
			viewHolder.mPicIv = (ImageView) convertView.findViewById(R.id.iv_pic_noteadd_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Drawable drawable = new BitmapDrawable(photos.get(position));
		viewHolder.mPicIv.setBackgroundDrawable(drawable);

		return convertView;
	}

	class ViewHolder {
		ImageView mPicIv;
	}
}
