package com.mrwang.intellcity.base;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.mrwang.intellcity.MainActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	public Context context;
	public SlidingMenu slidingMenu;
	public View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//获取context对象 让子类使用
		context=(Context)getActivity();
		//获取slidingMenu侧拉栏对象 让子类使用
		slidingMenu = ((MainActivity)context).getSlidingMenu();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
	}
	/**
	 * 1.初始化界面
	 * @param inflater
	 */
	public abstract View initView(LayoutInflater inflater);
	/**
	 * 2.填充数据
	 * @param savedInstanceState
	 */
	public abstract void initData(Bundle savedInstanceState);
	
}
