package com.mrwang.intellcity.view;

import com.mrwang.intellcity.base.BasePager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止viewpager滑动
 * 
 * @author Administrator
 * 
 */
public class MyViewPager extends LazyViewPager {


	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		//返回false 禁止消费滑动事件
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
			return false;
	}

}
