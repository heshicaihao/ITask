package com.xxdc.itask.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class HashMapUtils {
	public static String getKey(HashMap<?, ?> hashmap){
		String key = null;
		Set<?> set = hashmap.keySet();
		Iterator<?> it=set.iterator();
		 while(it.hasNext()){
	        	String key1 = (String) it.next();
	        	key = key1;
	        }
		 return key;
	}
	
	public static String getValue(HashMap<?, ?> hashmap,String key){
		String value = null;
		value = (String) hashmap.get(key);
		 return value;
	}
}
