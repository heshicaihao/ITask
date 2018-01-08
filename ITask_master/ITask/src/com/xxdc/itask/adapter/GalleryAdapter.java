package com.xxdc.itask.adapter;

import java.util.List;

import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

	private LayoutInflater mInflater;
	private List<String> mDatas;

	/**
	 * ItemClick的回调接口
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public GalleryAdapter(Context context, List<String> datats) {
		mInflater = LayoutInflater.from(context);
		mDatas = datats;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View arg0) {
			super(arg0);
		}

		ImageView mImg;
		TextView mTxt;
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	/**
	 * 创建ViewHolder
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View view = mInflater.inflate(R.layout.recyclerview_item, viewGroup, false);
		ViewHolder viewHolder = new ViewHolder(view);

		viewHolder.mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
		return viewHolder;
	}

	/**
	 * 设置值
	 */
	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
		ITaskApp.getInstance().getBitmpUtils().display(viewHolder.mImg, mDatas.get(i));
		// viewHolder.mImg.setImageResource(mDatas.get(i));
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {
			viewHolder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
				}
			});

		}
	}

}
