package com.mrwang.intellcity.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mrwang.intellcity.base.BasePager;
/**
 * 设置中心页面
 * @author Administrator
 *
 */
public class SettingPager extends BasePager {

	public SettingPager(Context context) {
		super(context);
	}

	@Override
	public View initView() {
		TextView tv=new TextView(context);
		tv.setText("设置中心页面");
		view=tv;
		return view;
	}

	@Override
	public void initData() {

	}

}
