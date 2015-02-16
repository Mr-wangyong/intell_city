package com.mrwang.intellcity.pager;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mrwang.intellcity.R;
import com.mrwang.intellcity.base.BasePager;
import com.mrwang.intellcity.bean.NewCenter.NewCenterItem;
import com.mrwang.intellcity.view.pagerindicator.TabPageIndicator;

/**
 * 新闻中心侧边栏 对应的新闻页面
 * 
 * @author Administrator
 * 
 */
public class NewPager extends BasePager {
	/**
	 * 底部整个页面的ViewPager
	 */
	@ViewInject(R.id.pager)
	private ViewPager pager;
	// 内容页的自定义指针控件,可以伴随viewPager滑动而滑动
	@ViewInject(R.id.indicator)
	private TabPageIndicator indicator;
	// 最右边切换的按钮
	@ViewInject(R.id.iv_edit_cate)
	private ImageView iv_edit_cate;

	// 从网络解析回来的 新闻中心模块-新闻 条目 的数据
	private NewCenterItem newCenterItem;

	// 定义两个集合 一个存储头信息 用于指针控件使用
	private List<String> titleList = new ArrayList<String>();
	// 一个用于存储每个详情页面对象
	private List<BasePager> pagerList = new ArrayList<BasePager>();

	private MyAdapter myAdapter;
	//滑动的当前条目
	private int currentItem=0;

	public NewPager(Context context, NewCenterItem newCenterItem) {
		super(context);
		this.newCenterItem = newCenterItem;
	}
	
	@Override
	public View initView() {
		view = View.inflate(context, R.layout.frag_news, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		/*
		 * 底部viewpager 需要去显示一个界面， 并且当前界面继承至BasePager，
		 * 显示具体内容封装在centerItem对应children节点中url所指向的地址
		 * 
		 * 修复一个bug 当用户切换到侧拉栏的其他item再回来的时候 数据会重新加载到集合中,造成数据重复
		 */
		// 先把每个节点的数据存储起来
		if (titleList.isEmpty() && pagerList.isEmpty()) {
			for (int i = 0; i < newCenterItem.children.size(); i++) {
				titleList.add(newCenterItem.children.get(i).title);
				pagerList.add(new NewItemPager(context, newCenterItem.children
						.get(i).url));
			}
		}
		
		
		/**
		 * 给pager设置适配器
		 */
		if (myAdapter == null) {
			myAdapter = new MyAdapter();
			pager.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}
		// 指针控件与viewpager绑定
		indicator.setViewPager(pager);
		// 设置指针控件的起始页
		indicator.setCurrentItem(0);

		/**
		 * 默认当第0页加载数据
		 */
		BasePager basePager = pagerList.get(0);
		basePager.initData();
		
		/**
		 * 点击按钮滑动侧拉条目
		 */
		iv_edit_cate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (currentItem==(pagerList.size()-1)) {
					//说明是最后一个条目拉 再点击 回到第一个条目
					currentItem=0;
					switchPager(0);
				}else {
					switchPager(++currentItem);
				}
			}
		});
		
		/**
		 * 当用户滑动页面的时候
		 */
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			// 当滑动停止的时候
			@Override
			public void onPageSelected(int arg0) {
				switchPager(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}
	
	/**
	 * 切换页面
	 * @param arg0
	 */
	private void switchPager(int position) {
		// 这里修复一个bug 只有当滑动到最左边的时候才能拉出侧边栏
		if (position == 0) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		currentItem=position;
		indicator.setCurrentItem(position);
		// 获取当前页对象
		BasePager basePager = pagerList.get(position);
		// 让当前页加载数据
		basePager.initData();
	}

	/**
	 * 数据适配器类
	 * @author Administrator
	 */
	class MyAdapter extends PagerAdapter {
		/**
		 * 这个方法必须有,当指针控件与Viewpager绑定的时候 调用此方法获取头信息
		 * 
		 * @param position
		 * @return
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			return titleList.get(position);
		}

		@Override
		public int getCount() {
			return titleList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View rootView = pagerList.get(position).getRootView();
			((ViewPager) container).addView(rootView);
			return rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}
	}

}
