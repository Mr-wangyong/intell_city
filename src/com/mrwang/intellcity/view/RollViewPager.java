package com.mrwang.intellcity.view;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.mrwang.intellcity.R;
import com.mrwang.intellcity.utils.HMMApi;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义ViewPager对象,对外界传入的数据和布局进行处理 返回一个设置好的ViewPager对象
 * 
 * @author Administrator
 * 
 */
public class RollViewPager extends ViewPager {
	/**
	 * 放置点的集合
	 */
	private List<View> viewList;
	/**
	 * 轮播图的Url地址集合
	 */
	private List<String> urlImgList;
	/**
	 * 图片说明的布局
	 */
	private TextView top_news_title;
	/**
	 * 图片说明的集合
	 */
	private List<String> titleList;
	private Context context;
	private BitmapUtils bitmapUtils;
	private MyRollAdapter myAdapter;
	private RunnableTask runnableTask;
	private int currentPosition = 0;
	private int pagernum=0;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 设置图片自动轮播
			RollViewPager.this.setCurrentItem(currentPosition);
			// 重新不断循环轮播
			startRoll();
		};
	};
	private int downX;
	private int downY;
	private onClick listener;

	public RollViewPager(Context context, final List<View> viewList) {
		super(context);
		this.context = context;
		this.viewList = viewList;
		bitmapUtils = new BitmapUtils(context);
		runnableTask = new RunnableTask();
		/**
		 * 设置一个页面状态监听器 页面状态改变时,点的样式也跟着改变
		 */
		this.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// 设置轮播标题
				top_news_title.setText(titleList.get(arg0));
				// 循环设置点的样式
				for (int i = 0; i < urlImgList.size(); i++) {
					if (i == arg0) {
						viewList.get(i).setBackgroundResource(
								R.drawable.dot_focus);
					} else {
						viewList.get(i).setBackgroundResource(
								R.drawable.dot_normal);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	/**
	 * 初始化头信息
	 * 
	 * @param top_news_title
	 * @param titleList
	 */
	public void initTitleList(TextView top_news_title, List<String> titleList) {
		if (null != top_news_title && null != titleList && titleList.size() > 0) {
			// 说明两个都不为空 默认显示第一条
			top_news_title.setText(titleList.get(0));
		}
		this.top_news_title = top_news_title;
		this.titleList = titleList;
	}

	public void initImaUrlList(List<String> urlImgList) {
		this.urlImgList = urlImgList;
	}

	/**
	 * 让Viewpager滚动
	 */
	public void startRoll() {
		if (myAdapter == null) {
			myAdapter = new MyRollAdapter();
			this.setAdapter(myAdapter);
		} else {
			myAdapter.notifyDataSetChanged();
		}
		handler.postDelayed(runnableTask, 3000);

	}

	/**
	 * 定时任务类 定时刷新轮播图
	 * 
	 * @author Administrator
	 * 
	 */
	class RunnableTask implements Runnable {
		@Override
		public void run() {
			currentPosition = (currentPosition + 1) % viewList.size();
			handler.sendEmptyMessage(0);
		}
	}

	/**
	 * 数据适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class MyRollAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return urlImgList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			// 加载布局
			View view = View.inflate(context, R.layout.viewpager_item, null);
			ImageView image = (ImageView) view.findViewById(R.id.image);
			// 从网络获取图片
			bitmapUtils.display(image,
					HMMApi.BASE_URL + urlImgList.get(position));
			// TODO 这里还有点击图片停止刷新的没有写
			/**
			 * 给item布局添加滑动事件,插入一个事件监听器
			 */
			view.setOnTouchListener(new OnTouchListener() {
				private int downX;
				private long downTime;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 用户手指一点击,马上停止轮播
						handler.removeCallbacksAndMessages(null);
						downX = (int) event.getX();
						downTime = System.currentTimeMillis();
						break;

					case MotionEvent.ACTION_MOVE:
						int moveX = (int) event.getX();
						// 当用户手指按下时间小于500毫秒 并且没有移动的时候 可以认为是一次点击事件
						if (System.currentTimeMillis() - downTime < 500
								&& downX == moveX) {
							if (listener != null) {
								// 调用接口的方法
								listener.onClick(position);
							}
						}else {
							pagernum=position;
						}
						// 重新开始轮播

						break;

					case MotionEvent.ACTION_CANCEL:
						startRoll();
						break;
					case MotionEvent.ACTION_UP:
						startRoll();
						break;
					}
					return true;
				}
			});

			((RollViewPager) container).addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((RollViewPager) container).removeView((View) object);
		}
	}

	/**
	 * 给ListView条目设置一个点击事件监听器
	 * 
	 * @author Administrator
	 */
	public interface onClick {
		public void onClick(int position);
	}

	public void setOnClickListener(onClick listener) {
		this.listener = listener;
	}

	/**
	 * 下面是修复bug的操作 1.修复第一个bug 当切换到其他页面时 再回来 图片轮播乱套拉 问题在于子线程任务没有结束掉 又开启了一个
	 * 多个子线程抢夺 导致乱套 解决办法是每次切换出去的时候 关闭handler
	 */
	// 从界面移出的时候会调用方法
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		// 移除handler上的所有任务 传入null表示删除所有
		handler.removeCallbacksAndMessages(null);
	}

	/**
	 * 第二个bug 轮播图不能滑动 还有斜着滑动没办法确定是下拉刷新还是滑动轮播图 利用事件分发机制完美解决
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 当用户手指按下的时候 让父控件不要去拦截事件
			/**
			 * 新的API 请求不拦截手指触摸事件 true 不拦截 false 拦截 这个是9
			 */
			getParent().requestDisallowInterceptTouchEvent(true);
			// 记录下起始坐标
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();
			// 做判断,如果用户是
			if (Math.abs(moveY - downY) > Math.abs(moveX - downX)) {
				// 说明用户想要下拉刷新 此时要求父控件拦截触摸事件
				getParent().requestDisallowInterceptTouchEvent(false);
				//System.out.println("走了if");
			} else {
				//System.out.println("走了else");
				// 拦截 用户可以滚动轮播图
				if (pagernum==urlImgList.size()-1 && moveX<downX) {
					//说明时最后一个条目
					//System.out.println("pagernum=="+pagernum);
					getParent().requestDisallowInterceptTouchEvent(false);
				}else if (pagernum==0 && moveX>downX) {
					//System.out.println("moveX=="+moveX+"  downX=="+downX+"  pagernum"+pagernum);
					//System.out.println("pagernum=="+pagernum);
					getParent().requestDisallowInterceptTouchEvent(false);
				}else {
					//System.out.println("不晓得执行冒得");
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				
			}

			break;

		case MotionEvent.ACTION_UP:

			break;
		}

		return super.dispatchTouchEvent(ev);
	}
}
