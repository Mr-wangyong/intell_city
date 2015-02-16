package com.mrwang.intellcity.utils;
/**
 * 专门存储Uri地址的工具类
 * @author Administrator
 *
 */
public class HMMApi {
	/**
	 * 原始的uri地址,要更改IP时修改此地址
	 */
	public final static String BASE_URL="http://192.168.0.103:8080/qbc";
	/**
	 * 拼接后完整的json地址,日常使用
	 */
	public final static String NEWS_CENTER_CATEGORIES=BASE_URL+"/categories.json";
}
