package com.xxdc.itask.fragment;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.R;
import com.xxdc.itask.adapter.MyConSearchAdpter;
import com.xxdc.itask.dto.UserListForGroupDTO;

public class ContextSearchFragment extends BaseFragment {
	@ViewInject(R.id.lv_show_context_search)
	private ListView mShowLview;
	LayoutInflater inflater;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		return initView(inflater, R.layout.context_search_fragment, container);
	}

	@Override
	protected void initUI(View v) {
		if (null != getData()) {
			mShowLview.setAdapter(new MyConSearchAdpter(getData(), inflater, mActivity));
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserListForGroupDTO> getData() {
		Bundle bundle = getArguments();
		if (null != bundle) {
			ArrayList<?> list = bundle.getParcelableArrayList("search");
			List<UserListForGroupDTO> search = (List<UserListForGroupDTO>) list;
			System.out.println("search.size():" + search.size());
			return search;
		}
		return null;
	}
}
