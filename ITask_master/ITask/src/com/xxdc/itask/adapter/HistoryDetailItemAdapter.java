package com.xxdc.itask.adapter;

import com.xxdc.itask.R;
import com.xxdc.itask.dto.NotepadDTO;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryDetailItemAdapter extends ArrayListAdapter<NotepadDTO> {

	public HistoryDetailItemAdapter(Activity c) {
		super(c);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = mContext.getLayoutInflater().inflate(R.layout.list_notepads_item, null);
			holder = new ViewHolder();
			holder.taskContext = (TextView) view.findViewById(R.id.tv_note_context);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.taskContext.setText(mList.get(position).getContext());
		return view;
	}

	private static class ViewHolder {
		TextView taskContext;
	}
}
