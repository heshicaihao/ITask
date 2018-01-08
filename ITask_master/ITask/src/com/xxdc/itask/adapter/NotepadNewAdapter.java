package com.xxdc.itask.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxdc.itask.R;
import com.xxdc.itask.activity.NotepadItemDetailsActivity;
import com.xxdc.itask.dto.NotepadDTO;
import com.xxdc.itask.dto.response.NoteListResponse;
import com.xxdc.itask.util.DataUtils;

public class NotepadNewAdapter extends BaseAdapter {
	private NoteListResponse response;
	private List<NotepadDTO> notepadList;
	private LayoutInflater inflater;
	private Context mActivity;
	private CallBack callBack;

	public NotepadNewAdapter(NoteListResponse response, LayoutInflater inflater, Context mActivity, CallBack callBack) {
		this.response = response;
		this.inflater = inflater;
		this.mActivity = mActivity;
		this.callBack = callBack;
		initData();
	}

	public NotepadNewAdapter(List<NotepadDTO> notepadList, LayoutInflater inflater, Context mActivity, CallBack callback) {
		this.notepadList = notepadList;
		this.inflater = inflater;
		this.mActivity = mActivity;
		this.callBack = callback;
	}

	private void initData() {
		notepadList = response.getResponse().getNotepadList();
	}

	@Override
	public int getCount() {
		return notepadList.size();
	}

	@Override
	public Object getItem(int position) {
		return notepadList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		List<String> imgUrls = notepadList.get(position).getImages();
		int len = imgUrls.size();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.notepad_listview_new_item, null);
			viewHolder.mTitleTview = (TextView) convertView.findViewById(R.id.tv_title_note_item);
			viewHolder.mTimeTview = (TextView) convertView.findViewById(R.id.tv_time_note_item);
			viewHolder.mIconImg = (TextView) convertView.findViewById(R.id.note_img);
			viewHolder.mIconVoic = (TextView) convertView.findViewById(R.id.note_tv);
			viewHolder.mLayout = (RelativeLayout) convertView.findViewById(R.id.layout_note);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mTitleTview.setText(((NotepadDTO) getItem(position)).getTitle());
		viewHolder.mTimeTview.setText(DataUtils.getData(((NotepadDTO) getItem(position)).getUpdateTime() / 1000 + ""));
		if (notepadList.get(position).getImages().size() > 0) {
			viewHolder.mIconImg.setVisibility(View.VISIBLE);
		}else{
			viewHolder.mIconImg.setVisibility(View.GONE);
		}
		if (notepadList.get(position).getVoices().size() > 0) {
			viewHolder.mIconVoic.setVisibility(View.VISIBLE);
		}else{
			viewHolder.mIconVoic.setVisibility(View.GONE);
		}
	
		viewHolder.mLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (callBack != null) {
					callBack.callBackMethod(v, position);
				}
				
			}
		});
		return convertView;
	}

	private class ViewHolder {
		TextView mTitleTview;// 标题
		TextView mTimeTview;// 时间
		TextView mIconImg;//图片标志
		TextView mIconVoic;//语音标志
		RelativeLayout mLayout;
	}
	
	public interface CallBack {
		abstract void callBackMethod(View v, int p);
	}
	
}
