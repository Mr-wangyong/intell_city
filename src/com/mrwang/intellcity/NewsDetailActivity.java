package com.mrwang.intellcity;

import com.mrwang.intellcity.utils.HMMApi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 显示单条新闻的详情页 webView
 * 
 * @author Administrator
 * 
 */
public class NewsDetailActivity extends Activity {
	private String url;
	private WebView webView;
	private FrameLayout loading_view;
	private WebSettings settings;
	private TextView txt_title;
	private ImageButton imgbtn_text;
	private ImageButton imgbtn_right;
	private ImageButton btn_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_news_detail);
		//获取传过来的数据
		if (getIntent() != null) {
			url = getIntent().getStringExtra("url");
		}
		initTitle();
		
		webView=(WebView)findViewById(R.id.news_detail_wv);
		//正在加载的页面
		loading_view=(FrameLayout)findViewById(R.id.loading_view);
		//设置一个监听器 当页面加载完毕的时候
		webView.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onPageFinished(WebView view, String url) {
				//将页面隐藏
				loading_view.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
		});
		/**
		 * 获取webView的设置对象
		 */
		settings = webView.getSettings();
		//从网络加载内容
		webView.loadUrl(HMMApi.BASE_URL+url);
	}
	/**
	 * 加载头上的控件
	 */
	private void initTitle() {
		Button btn_left=(Button) findViewById(R.id.btn_left);
		btn_left.setVisibility(View.GONE);
		
		ImageButton imgbtn_left=(ImageButton) findViewById(R.id.imgbtn_left);
		imgbtn_left.setImageResource(R.drawable.back);
		imgbtn_left.setOnClickListener(new OnClickListener() {
			//点击返回上一级
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		txt_title = (TextView) findViewById(R.id.txt_title);
		
		
		imgbtn_text = (ImageButton) findViewById(R.id.imgbtn_text);
		imgbtn_right = (ImageButton) findViewById(R.id.imgbtn_right);
		btn_right = (ImageButton) findViewById(R.id.btn_right);
		
		imgbtn_text.setVisibility(View.GONE);
		imgbtn_right.setImageResource(R.drawable.icon_textsize);
		imgbtn_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				settings.setTextZoom(120);
			}
		});
		btn_right.setImageResource(R.drawable.icon_share);
	}
}
