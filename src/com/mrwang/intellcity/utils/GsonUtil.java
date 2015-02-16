package com.mrwang.intellcity.utils;

import com.google.gson.Gson;

/**
 * 解析json的工具类
 * @author Administrator
 *
 */
public class GsonUtil {
	/**
	 * 解析json到javaBean
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> T jsonToBean(String json,Class<T> c){
		Gson gson=new Gson();
		return gson.fromJson(json, c);
	}
}
