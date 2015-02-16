package com.mrwang.intellcity.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mrwang.intellcity.MainActivity;
import com.mrwang.intellcity.R;
import com.mrwang.intellcity.base.BasePager;
import com.mrwang.intellcity.bean.NewCenter;
import com.mrwang.intellcity.utils.GsonUtil;
import com.mrwang.intellcity.utils.HMMApi;
import com.mrwang.intellcity.utils.LogUtils;
import com.mrwang.intellcity.utils.SharedPreferencesUtil;
/**
 * 新闻中心的页面
 * @author Administrator
 *
 */
public class NewsCenterPager extends BasePager {
	//查找内容显示页
	@ViewInject(R.id.news_center_fl)
	public FrameLayout  news_center_fl;
	
	@ViewInject(R.id.ll_loading)
	public LinearLayout ll_loading;
	//存储Json解析数据的javabean
	private NewCenter newCenter;
	//侧边栏标题信息的数据集合
	private List<String> titleList;
	//内容显示页 页面对象的集合
	private ArrayList<BasePager> pagers;
	
	
	public NewsCenterPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		view = View.inflate(context, R.layout.news_center_frame, null);
		ViewUtils.inject(this, view);
		//初始化头布局
		initTitleBar();
		ll_loading.setVisibility(View.VISIBLE);
		return view;
	}

	
	/**
	 * 这里是整个数据传递的开始  第一次请求网络
	 * 用户点击了新闻中心,然后请求网络,获取数据,并将头信息设置到侧边栏中
	 */
	@Override
	public void initData() {
		String result = SharedPreferencesUtil.getStringData(context, HMMApi.NEWS_CENTER_CATEGORIES, "");
		LogUtils.i("地址:=   "+HMMApi.NEWS_CENTER_CATEGORIES);
		if (!TextUtils.isEmpty(result)) {
			//说明有数据 不是空的 直接解析,让用户先看到老的数据 提高用户体验
			processData(result);
		}
		//打印一下信息
		LogUtils.i("网络请求的结果数据="+result);
		
		//从网络请求数据
		getNewCenterData();
		
	}
	
	
	
	/**
	 * 从网络请求数据
	 */
	private void getNewCenterData() {
		getData(HttpMethod.GET, HMMApi.NEWS_CENTER_CATEGORIES, null, new RequestCallBack<String>() {
			//请求成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				LogUtils.i(responseInfo.result);
				//将文件存储起来
				SharedPreferencesUtil.saveStringData(context, HMMApi.NEWS_CENTER_CATEGORIES, responseInfo.result);
				//存储完毕 解析数据
				//存储完毕,这里重新加载一下数据,用户界面得到刷新,获取到最新的数据
				processData(responseInfo.result);
				
			}
			//请求失败
			@Override
			public void onFailure(HttpException error, String msg) {
				
			}
		});
	}

	/**
	 * 解析服务器获取的JSON数据
	 * @param result
	 */
	private void processData(String result) {
		newCenter = GsonUtil.jsonToBean(result, NewCenter.class);
		titleList = new ArrayList<String>();
		for (int i = 0; i < newCenter.data.size(); i++) {
			titleList.add(newCenter.data.get(i).title);
		}
		//这个时候要把数据传给侧边栏fragment 只有一个context即MainActivity可用
		//这个时候 用这个对象一层层的去找 然后把数据传输过去
		/**
		 * 给侧边栏设置标题数据
		 */
		((MainActivity)context).getMenuFragment().initMenu(titleList);
		
		/**
		 * 收到了头信息,这个时候设置每个标题所对应的页面
		 */
		pagers=new ArrayList<BasePager>();
		pagers.add(new NewPager(context,newCenter.data.get(0)));
		pagers.add(new TopicPager(context,newCenter.data.get(1)));
		pagers.add(new PicPager(context,newCenter.data.get(2)));
		pagers.add(new IntPager(context,newCenter.data.get(3)));
		//默认显示第一个条目
		switchPager(0);
	}
	
	/**
	 * 当侧拉栏目被点击时 调用此方法 加载数据
	 * @param position
	 */
	public void switchPager(int position) {
		//给内容显示页标题设置数据
		txt_title.setText(titleList.get(position));
		
		//点击之前  父布局先移除所有数据
		news_center_fl.removeAllViews();
		
		//再加载数据  先初始化UI
		news_center_fl.addView(pagers.get(position).getRootView());
		
		//再加载数据
		/**
		 * 这里用到多态,不清楚到底是哪个页面需要被加载, 
		 * 这里抽取公共父类的好处立刻显现出来
		 */
		BasePager basePager = pagers.get(position);
		basePager.initData();
	}
	
	/**
	 * 获取新闻条目的页面对象
	 * @return
	 */
	public NewsCenterPager getNewConterPager() {
		return (NewsCenterPager) pagers.get(1);
	}

}
