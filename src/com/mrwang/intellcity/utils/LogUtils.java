package com.mrwang.intellcity.utils;

import android.content.Context;
import android.util.Log;

public class LogUtils {
	private final static Boolean FLAG=true;
	private final static String TAG="标记在此!!!---";
	//private final static String TAG2;
	/**
	 * 打印Log 需要tag
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if (FLAG) {
			Log.i(tag, msg);
		}
	}
	/**
	 * 打印Log 不需要tag 默认为   标记在此!!!---
	 * @param msg
	 */
	public static void i(String msg){
		if (FLAG) {
			Log.i(TAG, msg);
		}
	}
	/**
	 *  用当前类名作为标签
	 * @param context
	 * @param msg
	 */
	public static void ii(Context context,String msg){
		if (FLAG) {
			Log.i(context.getClass().getSimpleName(), msg);
		}
	}
	
}
