package com.mrwang.intellcity.pager;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mrwang.intellcity.base.BasePager;
/***
 * 政务信息
 * @author Administrator
 *
 */
public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		TextView tv=new TextView(context);
		tv.setText("政务信息");
		view=tv;
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
