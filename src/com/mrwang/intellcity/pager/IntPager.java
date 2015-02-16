package com.mrwang.intellcity.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mrwang.intellcity.base.BasePager;
import com.mrwang.intellcity.bean.NewCenter.NewCenterItem;
/**
 * 新闻中心侧拉栏对应的 互动页面
 * @author Administrator
 *
 */
public class IntPager extends BasePager {

	public IntPager(Context context, NewCenterItem newCenterItem) {
		super(context);
	}

	@Override
	public View initView() {
		TextView tv=new TextView(context);
		tv.setText("互动页面");
		view=tv;
		return view;
	}

	@Override
	public void initData() {

	}

}
