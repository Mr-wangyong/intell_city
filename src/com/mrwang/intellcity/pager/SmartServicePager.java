package com.mrwang.intellcity.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mrwang.intellcity.base.BasePager;
/**
 *  智慧服务
 * @author Administrator
 *
 */
public class SmartServicePager extends BasePager {

	public SmartServicePager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView tv=new TextView(context);
		tv.setText("智慧服务");
		view=tv;
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
