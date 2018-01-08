package com.xxdc.itask.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.xxdc.itask.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FriendApplyAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<HashMap<String, String>> lists;
	private CallBack callBack;

	public FriendApplyAdapter() {

	}

	public FriendApplyAdapter(Context context, ArrayList<HashMap<String, String>> lists, CallBack callBack) {
		this.context = context;
		this.lists = lists;
		this.callBack = callBack;
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			LayoutInflater flater = LayoutInflater.from(context);
			view = flater.inflate(R.layout.myfriend_apply_item, null);
			holder.mButton = (Button) view.findViewById(R.id.bt_agree);
			holder.mName = (TextView) view.findViewById(R.id.apply_name);
			holder.mMessage = (TextView) view.findViewById(R.id.apply_msg);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		HashMap<String, String> map = lists.get(position);
		holder.mName.setText(map.get("name"));
		holder.mMessage.setText(map.get("message"));
		holder.mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				lists.remove(position);
				notifyDataSetChanged();
				Toast.makeText(context, "已添加", Toast.LENGTH_LONG).show();
				if(lists.size() == 0){
					if(callBack != null){
						callBack.callBack();
					}
				}
			}
		});
		return view;
	}

	private static class ViewHolder {
		Button mButton;
		TextView mName;
		TextView mMessage;
	}
	
	public interface CallBack{
		void callBack();
	}

}
