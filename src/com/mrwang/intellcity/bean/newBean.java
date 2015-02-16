package com.mrwang.intellcity.bean;

import java.util.List;

public class newBean {
	// 数据对象
	public newBeanItem data;
	// 服务器返回值
	public String retcode;

	public class newBeanItem {
		// 评论总数量 带URL
		public String countcommenturl;
		// 到底部时上拉加载时 加载更多的数据
		public String more;
		// 总标题
		public String title;
		// ListView的新闻条目总集合
		public List<News> news;
		// ListView的话题
		public List<Topic> topic;
		// 头条的集合
		public List<Topnews> topnews;

	}

	/**
	 * 新闻
	 * @author Administrator
	 * 
	 */
	public class News {
		// 评论
		public String comment;
		// 查看更多评论
		public String commentlist;
		// 提意见
		public String commenturl;
		// 每条新闻的ID
		public String id;
		// 每条新闻所对应的图片地址url
		public String listimage;
		// 新闻的日期
		public String pubdate;
		// 新闻的标题
		public String title;
		// 新闻的类型
		public String type;
		// 点击新闻所对应的打开页面
		public String url;
		//判断当前新闻是否已读
		public boolean isRead;
	}

	/**
	 * 单条话题
	 * 
	 * @author Administrator
	 * 
	 */
	public class Topic {
		// 话题描述
		public String description;
		// 话题ID
		public String id;
		// 话题对应的图片URL
		public String listimage;
		// 话题分类
		public String sort;
		// 话题标题
		public String title;
		// 点击打开的Url
		public String url;
	}

	/**
	 * 单条头条 轮播图对应的数据
	 * 
	 * @author Administrator
	 * 
	 */
	public class Topnews {
		// 评论
		public String comment;
		// 查看更多评论
		public String commentlist;
		// 提意见
		public String commenturl;
		// 轮播图的ID
		public String id;
		// 轮播图的日期
		public String pubdate;
		// 轮播图的标题
		public String title;
		// 每条轮播图所对应的图片地址url
		public String topimage;
		// 轮播图的类型
		public String type;
		// 点击轮播图所对应的打开页面
		public String url;
		
		
	}
}
