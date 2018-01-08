package com.xxdc.itask.adapter;

import com.xxdc.itask.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FenPeiAdapter extends ArrayListAdapter<String> {

	public FenPeiAdapter(Activity c) {
		super(c);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = mContext.getLayoutInflater().inflate(R.layout.fenpei_list_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_name.setText(mList.get(position));
		return view;
	}

	private static class ViewHolder {
		TextView tv_name;
	}
}
