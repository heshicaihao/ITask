package com.xxdc.itask.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ArrayListAdapter<T> extends BaseAdapter {

	protected Activity mContext;
	protected ArrayList<T> mList;

	public ArrayListAdapter(Activity c) {
		this.mContext = c;
	}

	public void setList(ArrayList<T> list) {
		mList = list;
		notifyDataSetChanged();
	}

	public void addItem(T t) {
		mList.add(t);
	}

	public void addItems(ArrayList<T> list) {
		for (T t : list) {
			mList.add(t);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return (mList != null) ? mList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (mList != null) ? mList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	abstract public View getView(int position, View convertView,
			ViewGroup parent);

	public ArrayList<T> getList() {
		return mList;
	}
}
