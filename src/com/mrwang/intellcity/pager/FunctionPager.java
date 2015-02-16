package com.mrwang.intellcity.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mrwang.intellcity.base.BasePager;
/**
 *  首页的页面
 * @author Administrator
 *
 */
public class FunctionPager extends BasePager {

	public FunctionPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView tv=new TextView(context);
		tv.setText("首页");
		view=tv;
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
