package com.xxdc.itask.activity;

import java.util.ArrayList;

import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;
import com.xxdc.itask.dto.NotepadDTO;
import com.xxdc.itask.dto.net.Callback;
import com.xxdc.itask.dto.response.RemoteResponse;
import com.xxdc.itask.fragment.ContextFragment;
import com.xxdc.itask.fragment.HistoryFragment;
import com.xxdc.itask.fragment.HomeFragment;
import com.xxdc.itask.fragment.MoreFragment;
import com.xxdc.itask.fragment.NotepadFragment;
import com.xxdc.itask.widget.DummyTabContent;
import com.xxdc.itask.widget.TabIndicator;
import com.xxdc.lib.update.AutoUpdate;
import com.xxdc.lib.update.RemoteDTO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

public class HomeActivity extends BaseActivity {
	@ViewInject(android.R.id.tabhost)
	private TabHost mTabHost;

	@ViewInject(android.R.id.tabcontent)
	private FrameLayout mTabcontent;

	@ViewInject(android.R.id.tabs)
	private TabWidget mTabWidget;
	private ArrayList<NotepadDTO> notePads = null;
	
	private long mExitTime;
	
	private CallBack callBack;
	

	public static void launch(Context c) {
		Intent intent = new Intent(c, HomeActivity.class);
		c.startActivity(intent);
	}
	
	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		try {
			callBack = (CallBack) fragment;
		} catch (Exception e) {
		}
		
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.home_actitity);
		registerTokenPastReceiver();
	}

	@Override
	protected void initUI() {
		initTab();
		fromHistoryItem();
		doPostRemotinInfo();
	}

	@SuppressWarnings("unchecked")
	private void fromHistoryItem() {
		Bundle bundle = getIntent().getBundleExtra("fromHitoryItem");
		if (null == bundle) {
			return;
		}
		notePads = (ArrayList<NotepadDTO>) bundle.get("notePads");
		if (null != notePads) {
			LogUtils.i("notePads=" + notePads.toString());
			// 如果这个Activity被一个特殊的Intent传递,如果有需要,把该数据也传给Fragment
			NotepadFragment noteFragment = new NotepadFragment();
			noteFragment.setArguments(getIntent().getBundleExtra("fromHitoryItem"));
			replaceFragment(noteFragment);
		}

	}

	private void initTab() {
		mTabHost.setup();
		// 首页
		TabHost.TabSpec spectHome = mTabHost.newTabSpec(HomeFragment.class.getName());
		spectHome.setIndicator(new TabIndicator(this, R.string.lechai, R.drawable.bottom_tab_home_icon_selector));
		spectHome.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(spectHome);

		// 差友
		TabHost.TabSpec spectContext = mTabHost.newTabSpec(ContextFragment.class.getName());
		spectContext.setIndicator(new TabIndicator(this, R.string.context, R.drawable.bottom_tab_contact_icon_selector));
		spectContext.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(spectContext);
		

		// 记事
		TabHost.TabSpec spectNotepad = mTabHost.newTabSpec(NotepadFragment.class.getName());
		spectNotepad.setIndicator(new TabIndicator(this, R.string.notepad, R.drawable.bottom_tab_notebook_icon_selector));
		spectNotepad.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(spectNotepad);
		
		// 历史
		TabHost.TabSpec spectHistory = mTabHost.newTabSpec(HistoryFragment.class.getName());
		spectHistory.setIndicator(new TabIndicator(this, R.string.history, R.drawable.bottom_tab_history_icon_selector));
		spectHistory.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(spectHistory);

		// 更多
		TabHost.TabSpec spectMore = mTabHost.newTabSpec(MoreFragment.class.getName());
		spectMore.setIndicator(new TabIndicator(this, R.string.more, R.drawable.bottom_tab_more_icon_selector));
		spectMore.setContent(new DummyTabContent(getBaseContext()));
		mTabHost.addTab(spectMore);

		mTabHost.setOnTabChangedListener(mTabChangeListener);
		replaceFragment(Fragment.instantiate(this, HomeFragment.class.getName()));
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - mExitTime > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void doPostRemotinInfo() {
		ITaskApp.getInstance().getHttpClient().getRemotionVersion(new Callback() {
			@Override
			public void onSuccess(Object o) {
				RemoteDTO remoteData = ((RemoteResponse) o).getResponse();
				new AutoUpdate(HomeActivity.this, remoteData.getVersion(), remoteData.getDownload());// 判断版本更新
				LogUtils.i(remoteData.getVersion() + "," + remoteData.getDownload());
			}
			
			@Override
			public void onStart() {
			}
			
			@Override
			public void onFailure(Object o) {
			}
		});
	}
	
	TabHost.OnTabChangeListener mTabChangeListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {
			replaceFragment(Fragment.instantiate(HomeActivity.this, tabId));
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode == Activity.RESULT_OK){
			callBack.callBackFragment(intent);
		}
	};
	
	public interface CallBack{
		void callBackFragment(Intent data);
	}

}
