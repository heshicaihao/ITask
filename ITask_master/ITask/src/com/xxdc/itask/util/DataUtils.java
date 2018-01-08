package com.xxdc.itask.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtils{
	/**
	 * @param data  Long型
	 */
	public static String getData(String data){
		 Date currTime = new Date(Long.parseLong(data)*1000);
		 SimpleDateFormat yymmdd = new SimpleDateFormat("yyyy-MM-dd");//这里格式就是年月天
		 SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");//这里格式就是小时，分钟，秒
		 String v_ymd=yymmdd.format(currTime);
		 String v_hms=hhmmss.format(currTime);
		 return v_ymd+"   "+v_hms;
	}
	
	public static String getData(String data,boolean showYear,boolean showSeconed){
		 Date currTime = new Date(Long.parseLong(data)*1000);
		 StringBuffer sb = new StringBuffer();
		
		 SimpleDateFormat yymmdd = new SimpleDateFormat("yyyy-MM-dd");//这里格式就是年月天
		 SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss");//这里格式就是小时，分钟，秒
		if(showYear){
			sb.append(yymmdd.format(currTime)).append("\t");
			
		} 
		if(showSeconed){
			sb.append(hhmmss.format(currTime));
		}
		 
		 return sb.toString();
	}
	
	public static String getMonth(int month){
		switch (month) {
		case 1:
			return "Jan";			
		case 2:
			return "Feb";
		case 3:
			return "Mar";
		case 4:
			return "Apr";
		case 5:
			return "May";
		case 6:
			return "Jun";
		case 7:
			return "Jul";
		case 8:
			return "Aug";
		case 9:
			return "Sep";
		case 10:
			return "Oct";
		case 11:
			return "Nov";
		default:
			return "Dec";
		}
	}
}
