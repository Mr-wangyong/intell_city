package com.mrwang.intellcity.pager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mrwang.intellcity.MainActivity;
import com.mrwang.intellcity.NewsDetailActivity;
import com.mrwang.intellcity.R;
import com.mrwang.intellcity.base.BasePager;
import com.mrwang.intellcity.bean.newBean;
import com.mrwang.intellcity.bean.newBean.News;
import com.mrwang.intellcity.fragment.HMAdapter;
import com.mrwang.intellcity.utils.CommonUtil;
import com.mrwang.intellcity.utils.GsonUtil;
import com.mrwang.intellcity.utils.HMMApi;
import com.mrwang.intellcity.utils.SharedPreferencesUtil;
import com.mrwang.intellcity.view.RollViewPager;
import com.mrwang.intellcity.view.RollViewPager.onClick;
import com.mrwang.intellcity.view.pullrefreshview.PullToRefreshBase;
import com.mrwang.intellcity.view.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import com.mrwang.intellcity.view.pullrefreshview.PullToRefreshListView;
/**
 * 新闻模块 新闻条目 对应的内容页对象
 * @author Administrator
 *
 */
public class NewItemPager extends BasePager {

	private String url;
	
	private View layout_roll_view;
	//图片标题
	@ViewInject(R.id.top_news_title)
	private TextView top_news_title;
	//图片中间点布局
	@ViewInject(R.id.dots_ll)
	private LinearLayout dots_ll;
	//放置图片轮播 的布局
	@ViewInject(R.id.ll_top_news_viewpager)
	private LinearLayout ll_top_news_viewpager;
	//初始化下拉刷新的控件
	@ViewInject(R.id.lv_item_news)
	private PullToRefreshListView ptrlv_lv_item_news;
	//加载更多
	private String moreUrl;
	
	//需要传递给viewpager去显示的图片关联文字说明
	private List<String> titleList = new ArrayList<String>();
	//传递图片对应的url地址的集合
	private List<String> urlImgList = new ArrayList<String>();	
	
	//放置底部新闻ListView的item条目数据的集合的
	private List<News> newList = new ArrayList<News>();
	
	//放置点的结合
	private List<View> viewList = new ArrayList<View>();

	private MyBaseAdapter myBaseAdapter;
	
	protected static final String IDS = "ids";
	
	private List<String> idList = new ArrayList<String>();
	
	
	public NewItemPager(Context context, String url) {
		super(context);
		this.url=url;
	}

	@Override
	public View initView() {
		//初始化轮播图所在布局
		layout_roll_view = View.inflate(context, R.layout.layout_roll_view, null);
		ViewUtils.inject(this, layout_roll_view);
		view = View.inflate(context, R.layout.frag_item_news, null);
		//TODO 这个真的行?????  s事实证明是可以的
		ViewUtils.inject(this, view);
		
		
		//屏蔽下拉加载
		ptrlv_lv_item_news.setPullLoadEnabled(false);
		//启用下拉刷新,上拉加载
		ptrlv_lv_item_news.setScrollLoadEnabled(true);
		/**
		 * 设置下拉刷新的监听器
		 */
		ptrlv_lv_item_news.setOnRefreshListener(new OnRefreshListener<ListView>() {

			
			/**
			 * 当用户下拉刷新时
			 * @param refreshView
			 */
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getNewItemPager(url,true);
			}
			/**
			 * 当用户上拉加载时
			 * @param refreshView
			 */
			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				getNewItemPager(moreUrl, false);
			}
		});
		/**
		 * 设置点击事件监听器 若用户已点击条目 则更改状态
		 */
		ptrlv_lv_item_news.getRefreshableView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 * 这里注意 因为加了个头 所有都是position-1
				 */
				if (!newList.get(position-1).isRead) {
					//如果该新闻没有被阅读过 点击了之后更改状态为已读 
					//position-1是因为加了一个头  所以要减下来
					newList.get(position-1).isRead=true;
					//获取当前的ID 如果第一次没有返回为空
					String ids = SharedPreferencesUtil.getStringData(context, IDS, "");
					//在当前ＩＤ上加一个#
					SharedPreferencesUtil.saveStringData(context, IDS, ids+"#"+newList.get(position-1).id);
				}
				//刷新一下 让用户及时看到状态
				myBaseAdapter.notifyDataSetChanged();
				
				//TODO 跳转到相关页上去 webView
				Intent intent=new Intent(context,NewsDetailActivity.class);
				intent.putExtra("url", newList.get(position-1).url);
				context.startActivity(intent);
				
			}
		});
		
		return view;
	}
	
	/**
	 * 填充数据
	 */
	@Override
	public void initData() {
		//现将存储在本地的id分割成一个数组，
		//并且放置到结合中，然后跟请求服务端的数据进行匹配(id)，如果id匹配上了，
		//则说明已读，将服务端返回数据News对象中isRead字段设置成true,否则false
		idList.clear();
		//获取当前的ID 如果第一次没有返回为空
		String ids = SharedPreferencesUtil.getStringData(context, IDS, "");
		//将已读的条目ID加入到集合中
		String[] split = ids.split("#");
		for (String string : split) {
			idList.add(string);
		}
		
		
		
		
		
		String result = SharedPreferencesUtil.getStringData(context, HMMApi.BASE_URL+url, "");
		if (!TextUtils.isEmpty(result)) {
			//说明有缓存,先解析 再访问网络,提高用户体验
			processData(result,true);
		}
		//调用获取新条目的方法 去访问网络获取新的页面
		getNewItemPager(url, true);
	}

	/**
	 * 访问网络 获取新的条目页面
	 * @param url 网络地址
	 * @param isRefresh 是否刷新页面 <br/> 
	 * 上拉加载页面false  下拉刷新页面true
	 */
	protected void getNewItemPager(final String url, final boolean isRefresh) {
		if (!TextUtils.isEmpty(url)) {
			//说明有URl地址
			getData(HttpMethod.GET, HMMApi.BASE_URL+url, null, new RequestCallBack<String>() {
				
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					SharedPreferencesUtil.saveStringData(context, HMMApi.BASE_URL+url, responseInfo.result);
					processData(responseInfo.result, isRefresh);
				}
				
				@Override
				public void onFailure(HttpException error, String msg) {
					
				}
			});
			
		}else {
			Toast.makeText(context, "没有更多数据", 0).show();
			
			//隐藏顶部底部的刷新加载条目
			ptrlv_lv_item_news.onPullDownRefreshComplete();
			ptrlv_lv_item_news.onPullUpRefreshComplete();
		}
	}
	/**
	 * 解析从网络获取的Json数据
	 * @param result 解析的Json数据
	 * @param isRefresh 是否刷新操作
	 */
	private void processData(String result, boolean isRefresh) {
		newBean bean = GsonUtil.jsonToBean(result, newBean.class);
		if (bean.retcode.equals("200")) {
			//说明服务器响应码为200
			moreUrl=bean.data.more;
			
			//轮播图的解析
			if (bean.data.topnews.size()>0) {
				//说明有轮播图
				if (isRefresh) {
					//说明是刷新界面的操作  
					//清除它是为了更新操作,防止重复添加数据
					/**
					 * 这里面的代码是解析json并刷新数据
					 */
					titleList.clear();
					urlImgList.clear();
					for (int i = 0; i <bean.data.topnews.size(); i++) {
						titleList.add(bean.data.topnews.get(i).title);
						urlImgList.add(bean.data.topnews.get(i).topimage);
					}
					//将获取到的数据初始化点数据
					initDot();
					
					//组装数据,将数据拆分开来,让一个对象直接去处理  其实就是viewpager单独处理数据
					RollViewPager rollViewPager=new RollViewPager(context,viewList);
					//将布局对象和数据传递给该对象让其处理
					rollViewPager.initTitleList(top_news_title,titleList);
					rollViewPager.initImaUrlList(urlImgList);
					//开启滚动事件
					rollViewPager.startRoll();
					
					/**
					 * 给轮播图设置一个点击事件监听器 
					 * 点击是调用
					 */
					rollViewPager.setOnClickListener(new onClick() {
						
						@Override
						public void onClick(int position) {
							Toast.makeText(context,titleList.get(position) , Toast.LENGTH_SHORT).show();
						}
					});
					
					//将自定义的轮播图ViewPager设置到界面上去
					ll_top_news_viewpager.removeAllViews();
					ll_top_news_viewpager.addView(rollViewPager);
					
					//将轮播图所在的布局layout_roll_view添加到 ListView的头上
					//ptrlv_lv_item_news.getRefreshableView()获取的是自定义的ListView
					if (ptrlv_lv_item_news.getRefreshableView().getHeaderViewsCount()<=0) {
						//把整个布局加载到下拉刷新开源控件的头上
						ptrlv_lv_item_news.getRefreshableView().addHeaderView(layout_roll_view);
					}
				}
			}
			/**
			 * ListView的数据填充
			 */
			if (bean.data.news.size()>0) {
				if (isRefresh) {
					//如果是刷新的操作 将原有数据清空
					newList.clear();
				}
				//在重新加载数据 将整个集合添加进去
				newList.addAll(bean.data.news);
				
				//TODO  遍历数据  设置条目是否已读
				for (int i = 0; i < newList.size(); i++) {
					if (idList.contains(newList.get(i).id)) {
						//说明该条目已读
						newList.get(i).isRead=true;
					}else {
						newList.get(i).isRead=false;
					}
				}
				
				
				
				
				//填充ListView
				if (myBaseAdapter==null) {
					myBaseAdapter=new MyBaseAdapter(context, newList);
					ptrlv_lv_item_news.getRefreshableView().setAdapter(myBaseAdapter);
				}else {
					myBaseAdapter.notifyDataSetChanged();
				}
				
			}
			//隐藏底部下拉条目 结束下 拉刷新 和上拉加载
			ptrlv_lv_item_news.onPullDownRefreshComplete();
			ptrlv_lv_item_news.onPullUpRefreshComplete();
			//将正在加载的布局隐藏起来
			((MainActivity)context).getHomeFragment().getNewConterPager().ll_loading.setVisibility(View.INVISIBLE);
		}
	}
	/**
	 * 数据适配器类  将Json数据设置到界面上
	 * @author Administrator
	 *
	 */
	class MyBaseAdapter extends HMAdapter<News>{

		public MyBaseAdapter(Context context, List<News> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView==null) {
				convertView=View.inflate(context, R.layout.layout_news_item, null);
			}
			//利用xutils的工具去从网络上下载图片
			BitmapUtils bitmapUtils=new BitmapUtils(context);
			
			//查找子控件
			ImageView iv_img=(ImageView) convertView.findViewById(R.id.iv_img);
			TextView tv_title=(TextView) convertView.findViewById(R.id.tv_title);
			TextView tv_pub_date=(TextView) convertView.findViewById(R.id.tv_pub_date);
			/**
			 * 利用xutils从网络上加载图片
			 */
			bitmapUtils.display(iv_img,  HMMApi.BASE_URL+list.get(position).listimage);
			tv_title.setText(list.get(position).title);
			tv_pub_date.setText(list.get(position).pubdate);
			
			//TODO 这里设置已读条目变为灰色
			if (list.get(position).isRead) {
				tv_title.setTextColor(context.getResources().getColor(R.color.news_item_has_read_textcolor));
			}else {
				tv_title.setTextColor(context.getResources().getColor(R.color.news_item_no_read_textcolor));
			}
			return convertView;
		}
		
	}
	
	/**
	 * 初始化点View对象
	 */
	private void initDot() {
		//先清空点的容器
		dots_ll.removeAllViews();
		viewList.clear();
		
		//循环遍历轮播图集合 有多少张图片就有多少个点
		for (int i = 0; i < urlImgList.size(); i++) {
			View view=new View(context);
			if (i==0) {
				view.setBackgroundResource(R.drawable.dot_focus);
			}else {
				view.setBackgroundResource(R.drawable.dot_normal);
			}
			//设置宽高为6dip
			LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
					CommonUtil.dip2px(context, 6),
					CommonUtil.dip2px(context, 6)
					);
			//将布局设置到点身上
			view.setLayoutParams(layoutParams);
			//设置点之间的间距
			if (i!=0) {
				layoutParams.leftMargin=5;
			}
			//加载到容器上
			dots_ll.addView(view);
			//加载到集合中
			viewList.add(view);
		}
		
		
		
	}
}
