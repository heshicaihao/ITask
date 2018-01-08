package com.xxdc.itask.activity;

import java.util.ArrayList;
import java.util.HashMap;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.FriendApplyAdapter;
import com.xxdc.itask.adapter.FriendApplyAdapter.CallBack;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 好友申请列表
 * 
 * @author Administrator
 *
 */
public class FriendApplyActivity extends BaseActivity {
	@ViewInject(R.id.friend_apply_list)
	private ListView mListView;
	@ViewInject(R.id.empty_view)
	private TextView mEmptyView;
	private ArrayList<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.myfriend_apply_activity);
	}

	@Override
	protected void initUI() {
		list = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 5; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Gray");
			map.put("message", "附加信息:申请加我为好友");
			list.add(map);
		}
		mListView.setAdapter(new FriendApplyAdapter(activity, list, new CallBack() {

			@Override
			public void callBack() {
				mEmptyView.setVisibility(View.VISIBLE);

			}
		}));
	}

}
