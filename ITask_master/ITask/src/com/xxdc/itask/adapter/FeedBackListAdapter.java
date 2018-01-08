package com.xxdc.itask.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;
import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.ImageActivity;
import com.xxdc.itask.dto.FeebackDTO;
import com.xxdc.itask.service.PlayerService;

public class FeedBackListAdapter extends ArrayListAdapter<FeebackDTO> implements OnClickListener {
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Context context;
	private BitmapUtils bitmapUtils;

	public FeedBackListAdapter(Activity c) {
		super(c);
		this.context = c;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			view = mContext.getLayoutInflater().inflate(R.layout.feedback_list_item, null);
			holder = new ViewHolder();
			holder.tv_feedback_time = (TextView) view.findViewById(R.id.tv_feedback_time);
			holder.tv_feedback_detail = (TextView) view.findViewById(R.id.tv_feedback_detail);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.iv_feedback_photo = (ImageView) view.findViewById(R.id.iv_feedback_photo);
			holder.iv_feedback_mic = (ImageView) view.findViewById(R.id.iv_feedback_mic);
			holder.iv_feedback_fujian = (ImageView) view.findViewById(R.id.iv_feedback_fujian);
			holder.ll_feedback = (LinearLayout) view.findViewById(R.id.ll_feedback);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (null != mList.get(position)) {// 判断当前位置是否为空
			String time = getTime(mList.get(position).getFeeTime());
			if (null != time) {
				holder.tv_feedback_time.setText(time);
			}
			if (null != mList.get(position).getUserName()) {
				holder.tv_name.setText(mList.get(position).getUserName());
			}
			if (null != mList.get(position).getContext()) {
				holder.tv_feedback_detail.setText(mList.get(position).getContext());
			}
			if (null != mList.get(position)) {
				if (null != mList.get(position).getImgs() && mList.get(position).getImgs().size() > 0) {
					if ((Config.SERVER).equals(mList.get(position).getImgs().get(0).trim())
							|| (Config.SERVER + "/Task/ws/photo?path=").equals(mList.get(position).getImgs().get(0).trim())
							|| (Config.SERVER + "/Tasknull").equals(mList.get(position).getImgs().get(0).trim())) {
						holder.iv_feedback_photo.setVisibility(View.GONE);
					} else {
						LogUtils.i("adapter imageurl=" + mList.get(position).getImgs().get(0));
						holder.ll_feedback.setVisibility(View.VISIBLE);
						holder.iv_feedback_photo.setVisibility(View.VISIBLE);
						ITaskApp.getInstance().getBitmpUtils().display(holder.iv_feedback_photo, mList.get(position).getImgs().get(0));
					}
				} else {
					holder.ll_feedback.setVisibility(View.GONE);
				}
				if (null != mList.get(position).getVoices() && mList.get(position).getVoices().size() > 0) {
					if (!("").equals(mList.get(position).getVoices().get(0))) {
						if ((Config.SERVER + "/Task/ws/voice?path=").equals(mList.get(position).getVoices().get(0))) {
							holder.iv_feedback_mic.setVisibility(View.GONE);
						} else {
							LogUtils.i("adapter voiceurl=" + mList.get(position).getVoices().get(0));
							holder.ll_feedback.setVisibility(View.VISIBLE);
							holder.iv_feedback_mic.setVisibility(View.VISIBLE);
						}
					}
					if ((Config.SERVER + "/Task/ws/voice?path=").equals(mList.get(position).getVoices().get(0))
							&& (Config.SERVER + "/Task/ws/photo?path=").equals(mList.get(position).getImgs().get(0).trim())) {
						holder.ll_feedback.setVisibility(View.GONE);
					}
				}
			} else {
				holder.ll_feedback.setVisibility(View.GONE);
			}
			holder.iv_feedback_photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ImageActivity.class);
					// intent.putExtra("imageurl", mList.get(position).get);
					intent.putExtra("imageurl", mList.get(position).getImgs().get(0));
					context.startActivity(intent);
				}
			});
			holder.iv_feedback_mic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.i("播放录音", "开始播放录音");
					PlayerService.startPlay(context, mList.get(position).getVoices().get(0));
				}
			});
		}
		return view;
	}

	@SuppressLint("SimpleDateFormat")
	private String getTime(Long feeTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(feeTime));
	}

	private static class ViewHolder {
		TextView tv_feedback_time;
		TextView tv_feedback_detail;
		TextView tv_name;
		ImageView iv_feedback_photo;
		ImageView iv_feedback_mic;
		ImageView iv_feedback_fujian;
		LinearLayout ll_feedback;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_feedback_photo:
			break;
		default:
			break;
		}

	}
}
