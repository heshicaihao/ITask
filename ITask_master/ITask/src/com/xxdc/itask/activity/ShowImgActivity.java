package com.xxdc.itask.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

import com.xxdc.itask.Config;
import com.xxdc.itask.ITaskApp;
import com.xxdc.itask.R;

public class ShowImgActivity extends Activity implements OnItemSelectedListener, ViewFactory, OnItemClickListener {
	// @ViewInject(R.id.gallery)
	private Gallery gallery;
	// @ViewInject(R.id.imageswitcher)
	private ImageView imageSwitcher;
	private ImageAdapter imageAdapter;
	private int mCurrentPos = -1;// 当前的item
	private HashMap<Integer, ImageView> mViewMap;

	private int[] resIds = new int[] { R.drawable.aa, R.drawable.icon_camera_1, R.drawable.icon_confirm_1, R.drawable.icon_confirm_2, R.drawable.icon_detail_1,
			R.drawable.icon_detail_2, R.drawable.icon_attach, R.drawable.icon_camera_1, R.drawable.icon_detail_2 };
	private ArrayList<String> urls;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showimg_activity);
		getData();
		initUI();
	}

	private void initUI() {
		imageAdapter = new ImageAdapter(this, urls.size());
		gallery = (Gallery) findViewById(R.id.gallery_showimg);
		gallery.setAdapter(imageAdapter);
		gallery.setOnItemSelectedListener(this);
		gallery.setSelection(0);// 设置一加载Activity就显示的图片为第一张
		gallery.setOnItemClickListener(this);
		imageSwitcher = (ImageView) findViewById(R.id.iv_img_showimg);
		// imageSwitcher.setFactory(this);

		// 设置动画效果 淡入淡出
		// imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_in));
		// imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
		// android.R.anim.fade_out));
	}

	private void getData() {
		urls = getIntent().getStringArrayListExtra("urls");
		System.out.println(urls.size());
	}

	public class ImageAdapter extends BaseAdapter {
		int mGalleryItemBackground;
		private Context mContext;
		private int mCount;// 一共多少个item

		public ImageAdapter(Context context, int count) {
			mContext = context;
			mCount = count;
			mViewMap = new HashMap<Integer, ImageView>(count);
			TypedArray typedArray = obtainStyledAttributes(R.styleable.Gallery);
			// 设置边框的样式
			mGalleryItemBackground = typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
		}

		public int getCount() {
			return urls.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageview = mViewMap.get(position % mCount);
			if (imageview == null) {
				imageview = new ImageView(mContext);
				String url = urls.get(position);
				if (!url.startsWith("http")) {
					url = Config.SERVER + "/Task" + url;
				}
				ITaskApp.getInstance().getBitmpUtils().display(imageview, url);
				imageview.setScaleType(ImageView.ScaleType.FIT_XY);
				imageview.setLayoutParams(new Gallery.LayoutParams(136, 88));
				imageview.setBackgroundResource(mGalleryItemBackground);
			}
			return imageview;
		}
	}

	// 滑动Gallery的时候，ImageView不断显示当前的item
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// imageSwitcher.setImageResource(resIds[position % resIds.length]);
	}

	// 设置点击Gallery的时候才切换到该图片
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mCurrentPos == position) {
			// 如果在显示当前图片，再点击，就不再加载。
			return;
		}
		mCurrentPos = position;
		String url = urls.get(position);
		if (!url.startsWith("http")) {
			url = Config.SERVER + "/Task" + url;
		}
		ITaskApp.getInstance().getBitmpUtils().display(imageSwitcher, url);
		System.out.println("urls.get(position)---->" + url);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public View makeView() {
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundColor(0xFF000000);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		return imageView;
	}

}