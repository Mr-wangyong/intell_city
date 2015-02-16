package com.mrwang.intellcity.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mrwang.intellcity.base.BasePager;
import com.mrwang.intellcity.bean.NewCenter.NewCenterItem;
/**
 * 新闻中心侧拉栏		对应的组图页面
 * @author Administrator
 *
 */
public class PicPager extends BasePager {

	public PicPager(Context context,NewCenterItem newCenterItem) {
		super(context);
	}

	@Override
	public View initView() {
		TextView tv=new TextView(context);
		tv.setText("组图页面");
		view=tv;
		return view;
	}

	@Override
	public void initData() {

	}

}
