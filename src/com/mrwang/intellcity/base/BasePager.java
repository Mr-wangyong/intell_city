package com.mrwang.intellcity.base;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mrwang.intellcity.MainActivity;
import com.mrwang.intellcity.R;

/**
 * 所有pager的父类 抽取pager的公共部分
 * 
 * @author Administrator
 * 
 */
public abstract class BasePager {
	// 提供context供子类使用
	public Context context;
	// 提供侧拉对象供子类使用
	public SlidingMenu slidingMenu;
	// 提供View供子类使用 并且在initView里面做初始化
	public View view;
	// 提够标题对象并让子类去调用
	public TextView txt_title;
	private ImageButton imgbtn_text;
	private ImageButton imgbtn_right;
	private ImageButton btn_right;

	public BasePager(Context context) {
		this.context = context;
		slidingMenu = ((MainActivity) context).getSlidingMenu();
		view = initView();
	}

	/**
	 * 获取当前页面的View对象
	 * 
	 * @return
	 */
	public View getRootView() {
		return view;
	}

	/**
	 * 加载页面头信息
	 */
	protected void initTitleBar() {
		Button btn_left = (Button) view.findViewById(R.id.btn_left);
		// 设置按钮不可见 并且不占地方 后期会用到
		btn_left.setVisibility(View.GONE);

		ImageButton imgbtn_left = (ImageButton) view
				.findViewById(R.id.imgbtn_left);
		// 给图片设置前景
		imgbtn_left.setImageResource(R.drawable.img_menu);
		// 设置图片点击关闭测拉栏
		imgbtn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				slidingMenu.toggle();
			}
		});
		// 初始化标题控件
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		/*
		 * 给其他的按钮初始化并隐藏
		 */
		imgbtn_text = (ImageButton) view.findViewById(R.id.imgbtn_text);
		imgbtn_right = (ImageButton) view.findViewById(R.id.imgbtn_right);
		btn_right = (ImageButton) view.findViewById(R.id.btn_right);
		// 隐藏各个按钮
		imgbtn_text.setVisibility(View.GONE);
		imgbtn_right.setVisibility(View.GONE);
		btn_right.setVisibility(View.GONE);
	}

	/**
	 * 初始化父类的view成员变量
	 * 
	 * @return
	 */
	public abstract View initView();

	/**
	 * 填充数据
	 */
	public abstract void initData();

	/**
	 * 提供给子类的请求网络的方法
	 * 
	 * @param method
	 *            请求的类型 GET 或者POST
	 * @param url
	 *            请求的地址
	 * @param params
	 *            请求的参数
	 * @param callBack
	 *            回调函数
	 */
	public void getData(HttpMethod method, String url, RequestParams params,
			RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(method, url, callBack);
	}

}
