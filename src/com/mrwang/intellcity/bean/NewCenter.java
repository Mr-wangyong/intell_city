package com.mrwang.intellcity.bean;

import java.util.List;

/**
 * 新闻中心的javaBean
 * @author Administrator
 *
 */
public class NewCenter {
	//Json包含的数据
	public List<NewCenterItem> data;
	//Json额外的数据
	public List<String> extend;
	//服务器返回的状态吗
	public String retcode;
	/**
	 * data节点下的内容
	 * @author Administrator
	 *
	 */
	public class NewCenterItem{
		public List<Children> children;
		public String id;
		public String title;
		public String type;
		public String url;
		public String url1;
		public String dayurl;
		public String excurl;
		public String weekurl;
	}
	/**
	 * children节点下的内容
	 * @author Administrator
	 *
	 */
	public class Children{
		public String id;
		public String title;
		public String type;
		public String url;
	}
}
