package com.mrwang.intellcity.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mrwang.intellcity.R;
import com.mrwang.intellcity.base.BasePager;
import com.mrwang.intellcity.bean.newBean;
import com.mrwang.intellcity.utils.GsonUtil;
import com.mrwang.intellcity.utils.HMMApi;
import com.mrwang.intellcity.utils.SharedPreferencesUtil;
import com.mrwang.intellcity.view.pullrefreshview.PullToRefreshListView;

public class NewItemPager02 extends BasePager {

	private String url;
	/**
	 * 顶部轮播图区域所在的布局
	 */
	private View layout_roll_view;
	// 轮播图片所在布局
	@ViewInject(R.id.ll_top_news_viewpager)
	private LinearLayout ll_top_news_viewpager;
	// 轮播标题布局
	@ViewInject(R.id.top_news_title)
	private TextView top_news_title;
	// 轮播的状态点布局
	@ViewInject(R.id.dots_ll)
	private LinearLayout dots_ll;
	/**
	 * 自定义下拉ListView控件的布局
	 */
	@ViewInject(R.id.lv_item_news)
	private PullToRefreshListView ptr_lv_item_news;
	//轮播图标题的集合
	private List<String> titleList;
	//轮播图图片URL地址集合
	private List<String> urlImgList;
	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param url
	 */
	public NewItemPager02(Context context, String url) {
		super(context);
		this.url = url;
		titleList=new ArrayList<String>();
		urlImgList=new ArrayList<String>();
	}

	@Override
	public View initView() {
		layout_roll_view = View.inflate(context, R.layout.layout_roll_view,
				null);
		ViewUtils.inject(this, layout_roll_view);
		view = View.inflate(context, R.layout.frag_item_news, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		String result = SharedPreferencesUtil.getStringData(context,
				HMMApi.BASE_URL + url, "");
		if (!TextUtils.isEmpty(result)) {
			processData(result);
		}
		getNewItempager(url);
	}

	/**
	 * 从网络获取数据
	 * 
	 * @param url
	 */
	private void getNewItempager(final String url) {
		if (TextUtils.isEmpty(url)) {
			
		}else {
			getData(HttpMethod.GET, HMMApi.BASE_URL+url, null, new RequestCallBack<String>() {
				
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					SharedPreferencesUtil.saveStringData(context, HMMApi.BASE_URL+url, responseInfo.result);
					processData(responseInfo.result);
				}
				
				@Override
				public void onFailure(HttpException error, String msg) {
					
				}
			});
		}
	}

	/**
	 * 解析Json
	 * 
	 * @param result
	 */
	private void processData(String result) {
		newBean bean = GsonUtil.jsonToBean(result, newBean.class);
		if (bean.retcode.equals("200")) {
			//兵分两路 一路解析轮播图 一路解析ListView
				if (bean.data.topnews.size()>0) {
					//说明有轮播图
					urlImgList.clear();
					titleList.clear();
					for (int i = 0; i < bean.data.topnews.size(); i++) {
						urlImgList.add(bean.data.topnews.get(i).topimage);
						titleList.add(bean.data.topnews.get(i).title);
					}
					//
					
					
				}
				
				if (bean.data.news.size()>0) {
					//说明有条目
				}
		}
	}

}
