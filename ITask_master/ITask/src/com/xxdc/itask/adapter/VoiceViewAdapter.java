package com.xxdc.itask.adapter;

import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.service.PlayerService;

public class VoiceViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> voices;

	public VoiceViewAdapter(LayoutInflater inflater, ArrayList<String> voices) {
		this.inflater = inflater;
		this.voices = voices;
	}

	@Override
	public int getCount() {
		return voices.size();
	}

	@Override
	public Object getItem(int position) {
		return voices;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ImageItemHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.notepad_listview_item_item, null);
			holder = new ImageItemHolder();
			ViewUtils.inject(holder, view);
			view.setTag(holder);
		} else {
			holder = (ImageItemHolder) view.getTag();
		}
		holder.imgItem.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				Toast.makeText(ITaskApp.getInstance().getApplicationContext(), "被点击" + position, Toast.LENGTH_SHORT).show();
				PlayerService.startPlay(ITaskApp.getInstance().getApplicationContext(), ((ArrayList<String>) getItem(position)).get(position));
			}
		});

		return view;
	}

	private class ImageItemHolder {
		@ViewInject(R.id.tv_voice_note_2item)
		private ImageView imgItem;
	}
}
