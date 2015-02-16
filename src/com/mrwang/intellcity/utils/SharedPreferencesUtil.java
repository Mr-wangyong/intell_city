package com.mrwang.intellcity.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * SharedPreferences保存数据的工具类
 * @author Administrator
 *
 */
public class SharedPreferencesUtil {
	public static String CONFIG="config";
	public static SharedPreferences sp;
	/**
	 * 存储String字符串
	 * @param context 上下文
	 * @param key 字符串名称
	 * @param value 字符串内容
	 */
	public static void saveStringData(Context context,String key,String value){
		if (sp==null) {
			sp=context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	/**
	 * 获取存储的字符串
	 * @param context 上下文
	 * @param key 要获取的字符串名称
	 * @param defValue 获取失败时返回的值
	 * @return
	 */
	public static String getStringData(Context context,String key,String defValue){
		if (sp==null) {
			sp=context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
	
	/**
	 * 存储布尔类型的值
	 * @param context 上下文
	 * @param key 要存储数据的名称
	 * @param value 要存储数据的内容
	 */
	public static void saveBooleanData(Context context,String key,Boolean value){
		if (sp==null) {
			sp=context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	/**
	 * 获取存储的布尔型的值
	 * @param context 上下文
	 * @param key 要获取的布尔类型值名称
	 * @param defValue 获取失败时返回的布尔值
	 * @return
	 */
	public static boolean getBooleanData(Context context,String key,Boolean defValue){
		if (sp==null) {
			sp=context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	}

}
