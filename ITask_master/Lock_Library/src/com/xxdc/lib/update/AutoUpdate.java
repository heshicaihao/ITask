package com.xxdc.lib.update;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.xxdc.lib.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AutoUpdate {
	private static final String TAG = "AutoUpdate";
	 /* 下载包安装路径 */  
    private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private static final int DOWN_VERSION = 3;
	
//	private final String mServerDir;
	private static String mAppFile = "update.apk";
	private Context mContext = null;
	private Dialog mNoticeDialog;
	private Dialog mDownloadDialog;
	private ProgressBar mProgressBar;
	
	private boolean mInterceptFlag;
	
	private int mProgress;
	private int mVersionCode;
	
	private String mDownloadUrl;
	
	public AutoUpdate(Context context, int serverCode, String apkUrl) {
		mContext = context;
		mDownloadUrl = apkUrl;
		checkUpdateInfo(serverCode);
	}

	/**
	 * 外部接口调用
	 */
	public void checkUpdateInfo(int serverCode) {
		Log.v(TAG, "检查是否更新...");
		int localVersion;
		try {
			localVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
			if (localVersion < serverCode) {
				showNoticeDialog();
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 是否更新对话框
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setInverseBackgroundForced(true);
		builder.setTitle(R.string.version_update);
		builder.setPositiveButton(R.string.now_update, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});
		
		builder.setNegativeButton(R.string.after_update, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		mNoticeDialog = builder.create();
		mNoticeDialog.show();
//		mNoticeDialog.getWindow().setLayout(450, 547);
	}
	
	private void showDownloadDialog() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update, null);
		mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.version_update);
		builder.setView(v);
		builder.setNegativeButton(R.string.cancle, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mInterceptFlag = false;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		mInterceptFlag = true;
		new DownloadThread().start();
	}
	
	/**
	 * 安装apk
	 */
//	private void installApk() {
//		new Thread() {
//			@Override
//			public void run() {
//				String filePath = mContext.getFileStreamPath(mAppFile).toString();
//				String cmd = "pm install -r " + filePath;
//				Process process = null;
//				InputStream in = null;
//				try {
//					process = Runtime.getRuntime().exec(cmd + "\n");
//					int len = 0;
//					in = process.getInputStream();
//					byte[] bs = new byte[256];
//					while (-1 != (len = in.read(bs))) {
//						String state = new String(bs, 0, len).trim().toUpperCase();
//						if (state.equals("SUCCESS")) {
////							mHandler.sendEmptyMessage(INSTALL_OVER);
//							mContext.getFileStreamPath(mAppFile).deleteOnExit();
//							System.out.println("***********************************************************");
//							break;
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				} finally {
//					if (in != null) {
//						try {
//							in.close();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		}.start();
//		Toast.makeText(mContext, mContext.getResources().getString(R.string.install_start) + mAppFile, Toast.LENGTH_SHORT).show();
//	}
//	
	 /** 
     * 安装apk 
     * @param url 
     */  
    private void installApk(){  
    	File tempFile = new File("/mnt/internal_sd/install.sys");//安装权限
        Intent intent = new Intent();
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setAction(android.content.Intent.ACTION_VIEW);
	    File file = mContext.getFileStreamPath(mAppFile);
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
	    mContext.startActivity(intent);  
	    
	    mContext.getFileStreamPath(mAppFile).deleteOnExit();
	    tempFile.deleteOnExit();
		System.out.println("***********************************************************");
    } 
    
    
    private Handler mHandler = new Handler(){  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case DOWN_UPDATE:  
                mProgressBar.setProgress(mProgress);
//                mProgressTextView.setText(mProgress + "%");
                break;  
            case DOWN_OVER:
            	mDownloadDialog.dismiss();
                installApk();  
                break;  
            default:  
                break;  
            }  
        };  
    };

    /**下载apk*/
	class DownloadThread extends Thread {
		
		@Override
		public void run() {
			Log.v(TAG, "开始下载...");
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				URL url = new URL(mDownloadUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				Log.v(TAG, "apksize:" + length);
				is = conn.getInputStream();
				//必须为MODE_WORLD_READABLE模式，否则不能成功解析包
				fos = mContext.openFileOutput(mAppFile, Context.MODE_WORLD_READABLE);
				
				int count = 0;
				byte[] buf = new byte[1024];
				while (mInterceptFlag) {
					int numread = is.read(buf);
					count += numread;
					if (numread <= 0) {
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					Log.i("****", mProgress + "," + count + "****");
					mProgress = (int) (((float)count / length) * 100);
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					fos.write(buf, 0, numread);
				}
				fos.flush();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(mContext, "Apk File not find", Toast.LENGTH_SHORT).show();
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
					
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
