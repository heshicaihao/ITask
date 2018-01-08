package com.xxdc.itask.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.xxdc.itask.R;
import com.xxdc.itask.activity.AllotActivity;
import com.xxdc.itask.activity.HomeActivity;
import com.xxdc.itask.dto.GroupUsersListDTO;
import com.xxdc.itask.dto.UserListForGroupDTO;
import com.xxdc.itask.util.StringUtils;

/**
 * 分配差友适配器
 * 
 * @author Administrator
 *
 */
public class AllotUserContactAdapter extends BaseAdapter {
	private List<UserListForGroupDTO> userDatas;
	private List<UserListForGroupDTO> groupUsersList;
	private List<GroupUsersListDTO> responses;
	private int flag;
	private AllotActivity mActivity;
	private AQuery aq;
	private CallBack callBack;

	public AllotUserContactAdapter(Activity mActivity, List<UserListForGroupDTO> responses, int flag, CallBack callback) {
		this.userDatas = responses;
		this.flag = flag;
		this.callBack = callback;
		this.mActivity = (AllotActivity) mActivity;
		aq = new AQuery(mActivity);
		initData();
	}

	private void initData() {
		groupUsersList = new ArrayList<UserListForGroupDTO>();
		List<UserListForGroupDTO> users = userDatas;
		for (UserListForGroupDTO userList : users) {

			Integer userId = userList.getUserId();
			String userName = userList.getUserName();
			String realName = userList.getRealName();
			String email = userList.getEmail();
			String tel = userList.getTel();
			String phone = userList.getPhone();
			String photo = userList.getPhoto();

			StringUtils.handleNull(userName, realName, email, tel, phone, photo);
			groupUsersList.add(new UserListForGroupDTO(userId, userName, realName, email, tel, phone, photo));
		}

	}

	@Override
	public int getCount() {
		return groupUsersList.size();
	}

	@Override
	public Object getItem(int position) {
		return groupUsersList;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			view = mActivity.getLayoutInflater().inflate(R.layout.list_allot_item, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) view.findViewById(R.id.tv_name_context_item);
			holder.imgHeader = (ImageView) view.findViewById(R.id.iv_head_context_item);
			holder.cbCheck = (CheckBox) view.findViewById(R.id.cb_choice_context_item);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tvName.setText(userDatas.get(position).getRealName());
		// checkBox状态显示
		boolean nowStatus = userDatas.get(position).isCheck();
		System.out.println(">>>>>>>>>nowStatus:"+nowStatus);
		holder.cbCheck.setChecked(nowStatus);
		aq.id(holder.imgHeader).image(userDatas.get(position).getPhoto(), true, true, 0, R.drawable.drawable);
		holder.cbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (callBack != null) {
					callBack.callBackMethod(buttonView, position, userDatas.get(position).getUserId() + ":" + userDatas.get(position).getRealName());
				}

			}
		});
		return view;
	}

	private class ViewHolder {
		private ImageView imgHeader;
		private TextView tvName;
		private CheckBox cbCheck;
	}

	public interface CallBack {
		abstract void callBackMethod(View v, int p, String name);
	}
}
