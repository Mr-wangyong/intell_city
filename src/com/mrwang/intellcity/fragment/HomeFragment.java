package com.mrwang.intellcity.fragment;

import java.util.ArrayList;
import java.util.List;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mrwang.intellcity.R;
import com.mrwang.intellcity.base.BaseFragment;
import com.mrwang.intellcity.base.BasePager;
import com.mrwang.intellcity.pager.FunctionPager;
import com.mrwang.intellcity.pager.GovAffairsPager;
import com.mrwang.intellcity.pager.NewsCenterPager;
import com.mrwang.intellcity.pager.SettingPager;
import com.mrwang.intellcity.pager.SmartServicePager;
import com.mrwang.intellcity.utils.LogUtils;
import com.mrwang.intellcity.view.MyViewPager;
import com.mrwang.intellcity.view.LazyViewPager.OnPageChangeListener;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeFragment extends BaseFragment {
	// 查找底部的按钮组
	@ViewInject(R.id.main_radio)
	private RadioGroup main_radio;
	// 查找内容显示的自定义控件 其实就是一个禁止拦截滑动事件 并且不预加载的ViewPager
	@ViewInject(R.id.layout_content)
	private MyViewPager layout_content;
	private List<BasePager> pagerList;

	@Override
	public View initView(LayoutInflater inflater) {
		view = inflater.inflate(R.layout.frag_home, null);
		ViewUtils.inject(this, view);
		return view;
	}
	/**
	 * 真正的开头,从这里开始,连接网络,
	 * 1.先是用户点击了下面的按钮组,第一次访问网络获取数据,
	 * 2.将访问网络获取的数据标题传递给相应的测拉栏,默认显示第一页的内容
	 * 3.用户点击侧拉栏,再次将访问网络,将数据传递到页面上
	 */
	@Override
	public void initData(Bundle savedInstanceState) {
		main_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_function:// 首页
					// 切换到第一个界面
					layout_content.setCurrentItem(0);
					break;
				case R.id.rb_news_center:// 新闻中心
					layout_content.setCurrentItem(1);
					break;
				case R.id.rb_smart_service:// 智慧服务
					layout_content.setCurrentItem(2);
					break;
				case R.id.rb_gov_affairs:// 政务指南
					layout_content.setCurrentItem(3);
					break;
				case R.id.rb_setting:// 设置中心
					layout_content.setCurrentItem(4);
					break;
				}
			}
		});
		// 设置默认显示首页
		main_radio.check(R.id.rb_function);

		// 给ViewPager添加数据
		pagerList = new ArrayList<BasePager>();
		pagerList.add(new FunctionPager(getActivity()));
		pagerList.add(new NewsCenterPager(getActivity()));
		pagerList.add(new SmartServicePager(getActivity()));
		pagerList.add(new GovAffairsPager(getActivity()));
		pagerList.add(new SettingPager(getActivity()));

		// 给Viewpager设置适配器
		layout_content.setAdapter(new MyAdapter());
		
		//给viewPager设置页面监听器
		layout_content.setOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * 当页面被选中的时候 应该加载该页面的数据
			 */
			@Override
			public void onPageSelected(int position) {
				//获取该页面
				BasePager basePager = pagerList.get(position);
				//让页面加载数据
				basePager.initData();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});

	}
	
	/**
	 * 数据适配器
	 * @author Administrator
	 *
	 */
	class MyAdapter extends PagerAdapter {
		/**
		 * 页面个数
		 */
		@Override
		public int getCount() {
			return pagerList.size();
		}

		/**
		 * 是否使用缓存
		 */
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		/**
		 * 初始化页面
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View rootView = pagerList.get(position).getRootView();
			//这里让首页没法滑动
			if (position==0) {
				LogUtils.i("不能侧滑");
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			}else {
				LogUtils.i("可以侧滑");
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			}
			
			((MyViewPager)container).addView(rootView);
			return rootView;
		}

		/**
		 * 销毁页面
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((MyViewPager)container).removeView((View)object);
		}
	}
	/**
	 * 获取新闻中心的页面对象
	 * @return
	 */
	public NewsCenterPager getNewConterPager() {
		return (NewsCenterPager) pagerList.get(1);
	}

}
