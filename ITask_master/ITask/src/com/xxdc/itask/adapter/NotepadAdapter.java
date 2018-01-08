package com.xxdc.itask.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.HomeActivity;
import com.xxdc.itask.activity.ShowImgActivity;
import com.xxdc.itask.dto.NotepadDTO;
import com.xxdc.itask.dto.response.NoteListResponse;
import com.xxdc.itask.util.DataUtils;

public class NotepadAdapter extends BaseAdapter {
	private NoteListResponse response;
	private List<NotepadDTO> notepadList;
	private LayoutInflater inflater;
	private Context mActivity;

	public NotepadAdapter(NoteListResponse response, LayoutInflater inflater, Context mActivity) {
		this.response = response;
		this.inflater = inflater;
		this.mActivity = mActivity;
		initData();
	}

	public NotepadAdapter(List<NotepadDTO> notepadList, LayoutInflater inflater, Context mActivity) {
		this.notepadList = notepadList;
		this.inflater = inflater;
		this.mActivity = mActivity;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		List<String> imgUrls = notepadList.get(position).getImages();
		int len = imgUrls.size();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.notepad_listview_item, null);
			viewHolder.mTitleTview = (TextView) convertView.findViewById(R.id.tv_title_note_item);
			viewHolder.mTimeTview = (TextView) convertView.findViewById(R.id.tv_time_note_item);
			viewHolder.mContextTview = (TextView) convertView.findViewById(R.id.tv_context_note_item);
			viewHolder.mImgFir = (ImageView) convertView.findViewById(R.id.iv_img1_note_item);
			viewHolder.mImgSec = (ImageView) convertView.findViewById(R.id.iv_img2_note_item);
			viewHolder.mImgThi = (ImageView) convertView.findViewById(R.id.iv_img3_note_item);
			viewHolder.mImgTotal = (TextView) convertView.findViewById(R.id.tv_total_note_item);
			viewHolder.voiceView = (HorizontalListView) convertView.findViewById(R.id.lv_voice_note_item);
			//setTag(viewHolder, imgUrls, len);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mImgFir.setVisibility(View.GONE);
		viewHolder.mImgSec.setVisibility(View.GONE);
		viewHolder.mImgThi.setVisibility(View.GONE);
		viewHolder.mImgTotal.setVisibility(View.GONE);
		setImgAndListener(viewHolder, imgUrls, len, position);

		if (null != notepadList.get(0).getVoices() && notepadList.get(position).getVoices().size() > 0) {
			viewHolder.voiceView.setVisibility(View.VISIBLE);
			viewHolder.voiceView.setAdapter(new VoiceViewAdapter(inflater, notepadList.get(position).getVoices()));
		} else {
			viewHolder.voiceView.setVisibility(View.GONE);
		}

		viewHolder.mTitleTview.setText(((NotepadDTO) getItem(position)).getTitle());
		viewHolder.mTimeTview.setText(DataUtils.getData(((NotepadDTO) getItem(position)).getUpdateTime() / 1000 + ""));
		viewHolder.mContextTview.setText(((NotepadDTO) getItem(position)).getContext());
		return convertView;
	}

	private void setImgAndListener(ViewHolder viewHolder, List<String> imgUrls, int len, int position) {
		if (len > 0 && len < 2) {
			viewHolder.mImgFir.setVisibility(View.VISIBLE);
			viewHolder.mImgSec.setVisibility(View.GONE);
			viewHolder.mImgThi.setVisibility(View.GONE);
			viewHolder.mImgTotal.setVisibility(View.GONE);
			viewHolder.mImgFir.setTag(setUrl0(imgUrls));
			if (viewHolder.mImgFir.getTag() != null && ((String) viewHolder.mImgFir.getTag()).equals(setUrl0(imgUrls))) {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgFir, setUrl0(imgUrls));
				viewHolder.mImgFir.setOnClickListener(new ImgViewListener(position));
			} else {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgFir, "");
			}
		} else if (len == 2) {
			viewHolder.mImgFir.setVisibility(View.VISIBLE);
			viewHolder.mImgSec.setVisibility(View.VISIBLE);
			viewHolder.mImgThi.setVisibility(View.GONE);
			viewHolder.mImgTotal.setVisibility(View.GONE);
			viewHolder.mImgFir.setTag(setUrl0(imgUrls));
			viewHolder.mImgSec.setTag(setUrl1(imgUrls));
			if (viewHolder.mImgFir.getTag() != null && ((String) viewHolder.mImgFir.getTag()).equals(setUrl0(imgUrls))) {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgFir, setUrl0(imgUrls));
				viewHolder.mImgFir.setOnClickListener(new ImgViewListener(position));
			} else {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgFir, "");
			}

			if (viewHolder.mImgSec.getTag() != null && ((String) viewHolder.mImgSec.getTag()).equals(setUrl1(imgUrls))) {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgSec, setUrl1(imgUrls));
				viewHolder.mImgSec.setOnClickListener(new ImgViewListener(position));
			} else {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgSec, "");
			}
		} else if (len == 3) {
			viewHolder.mImgFir.setVisibility(View.VISIBLE);
			viewHolder.mImgSec.setVisibility(View.VISIBLE);
			viewHolder.mImgThi.setVisibility(View.VISIBLE);
			viewHolder.mImgTotal.setVisibility(View.GONE);
			viewHolder.mImgFir.setTag(setUrl0(imgUrls));
			viewHolder.mImgSec.setTag(setUrl1(imgUrls));
			viewHolder.mImgThi.setTag(setUrl2(imgUrls));

			if (viewHolder.mImgFir.getTag() != null && ((String) viewHolder.mImgFir.getTag()).equals(setUrl0(imgUrls))) {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgFir, setUrl0(imgUrls));
				viewHolder.mImgFir.setOnClickListener(new ImgViewListener(position));
			} else {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgFir, "");
			}

			if (viewHolder.mImgSec.getTag() != null && ((String) viewHolder.mImgSec.getTag()).equals(setUrl1(imgUrls))) {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgSec, setUrl1(imgUrls));
				viewHolder.mImgSec.setOnClickListener(new ImgViewListener(position));
			} else {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgSec, "");
			}

			if (viewHolder.mImgThi.getTag() != null && ((String) viewHolder.mImgThi.getTag()).equals(setUrl2(imgUrls))) {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgThi, setUrl2(imgUrls));
				viewHolder.mImgThi.setOnClickListener(new ImgViewListener(position));
			} else {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgThi, "");
			}

		} else if (len > 3) {
			viewHolder.mImgTotal.setVisibility(View.VISIBLE);
			viewHolder.mImgSec.setVisibility(View.VISIBLE);
			viewHolder.mImgFir.setVisibility(View.GONE);
			viewHolder.mImgThi.setVisibility(View.GONE);
			viewHolder.mImgSec.setTag(setUrl1(imgUrls));
			if (viewHolder.mImgSec.getTag() != null && ((String) viewHolder.mImgSec.getTag()).equals(setUrl1(imgUrls))) {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgSec, setUrl1(imgUrls));
				viewHolder.mImgSec.setOnClickListener(new ImgViewListener(position));
			} else {
				ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImgSec, "");
			}

			viewHolder.mImgTotal.setText("共(" + len + ")图片");
		}
	}

	private String setUrl0(List<String> imgUrls) {
		String url0 = imgUrls.get(0);
		if (!url0.startsWith("http")) {
			url0 = Config.SERVER + "/Task" + url0;
		}
		return url0;
	}

	private String setUrl1(List<String> imgUrls) {
		String url1 = imgUrls.get(1);
		if (!url1.startsWith("http")) {
			url1 = Config.SERVER + "/Task" + url1;
		}
		return url1;
	}

	private String setUrl2(List<String> imgUrls) {
		String url2 = imgUrls.get(2);
		if (!url2.startsWith("http")) {
			url2 = Config.SERVER + "/Task" + url2;
		}
		return url2;
	}

	private class ViewHolder {
		TextView mTitleTview;// 标题
		TextView mTimeTview;// 时间
		TextView mContextTview;// 内容
		ImageView mImgFir;// 第一张图片
		ImageView mImgSec;// 第二张图片
		ImageView mImgThi;// 第三张图片
		TextView mImgTotal;// 总图片数量
		HorizontalListView voiceView;
	}

	private class ImgViewListener implements OnClickListener {
		private int position;

		public ImgViewListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_img1_note_item:
			case R.id.iv_img2_note_item:
			case R.id.iv_img3_note_item:
				Intent intent = new Intent(mActivity, ShowImgActivity.class);
				intent.putStringArrayListExtra("urls", (ArrayList<String>) ((NotepadDTO) getItem(position)).getImages());
				mActivity.startActivity(intent);
				break;
			}

		}
	}
}
