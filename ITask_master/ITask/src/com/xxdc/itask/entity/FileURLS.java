package com.xxdc.itask.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存上传图片成功后服务武器返回来的图片地址
 * 
 * @author Administrator
 *
 */
public class FileURLS {
	private static List<String> fileURLS = new ArrayList<String>();

	public static List<String> getFileURLS() {
		if (null == fileURLS) {
			fileURLS = new ArrayList<String>();
		}
		return fileURLS;
	}

	public static void setFileURLS(List<String> fileURLS) {
		FileURLS.fileURLS = fileURLS;
	}
}
