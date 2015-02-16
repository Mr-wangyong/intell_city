package com.mrwang.intellcity;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {
	private Button button;
	private ViewPager viewPager;
	private List<ImageView> imageList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		button = (Button) findViewById(R.id.button);
		viewPager = (ViewPager) findViewById(R.id.view_pager);

		ImageView image1 = new ImageView(this);
		image1.setBackgroundResource(R.drawable.guide_1);

		ImageView image2 = new ImageView(this);
		image2.setBackgroundResource(R.drawable.guide_2);

		ImageView image3 = new ImageView(this);
		image3.setBackgroundResource(R.drawable.guide_3);

		imageList = new ArrayList<ImageView>();
		imageList.add(image1);
		imageList.add(image2);
		imageList.add(image3);
		
		
		
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * 当页面被选中的时候
			 */
			@Override
			public void onPageSelected(int position) {
				if (position==(imageList.size()-1)) {
					button.setVisibility(View.VISIBLE);
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
							startActivity(intent);
							finish();
						}
					});
				}
			}
			/**
			 * 当页面滑动完成的时候
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			/**
			 * 当页面滑动状态改变的时候
			 */
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});

	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//利用传入进来的container对象添加子View对象
			((ViewPager)container).addView(imageList.get(position));
			return imageList.get(position);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//这种写法 诡异
			((ViewPager)container).removeView((View)object);;
		}
	}
}
