package com.mrwang.intellcity.fragment;

import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.mrwang.intellcity.MainActivity;
import com.mrwang.intellcity.R;
import com.mrwang.intellcity.base.BaseFragment;


public class MenuFragment extends BaseFragment {
	
	
	private List<String> titleList;
	//默认选中第一个条目
	private int currentPosition=0;
	private MyAdapter adapter;
	@ViewInject(R.id.lv_menu_news_center)
	private ListView lv_menu_news_center;
	
	/**
	 * 初始化UI
	 */
	@Override
	public View initView(LayoutInflater inflater) {
		view=inflater.inflate(R.layout.layout_left_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}
	/**
	 * 填充数据
	 */
	@Override
	public void initData(Bundle savedInstanceState) {
		
	}
	/**
	 * 将新闻中心模块    网络请求获取的数据设置到侧边栏上,其实就是用数据将ListView填充起来
	 * @param titleList
	 */
	public void initMenu(List<String> titleList){
		this.titleList=titleList;
		LogUtils.i("tilteList.size()=="+titleList.size());
		//设置数据适配器
		adapter = new MyAdapter(getActivity(), titleList);
		lv_menu_news_center.setAdapter(adapter);
		
		/*
		 * 当用户点击新闻中心的相关条目时  
		 * 获取当前页面页码,然后获取新闻中心页面对象,调用相关方法设置内容显示页内容
		 */
		lv_menu_news_center.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPosition=position;
				//这里通知适配器刷新,其实就是更改一下条目的颜色样式
				adapter.notifyDataSetChanged();
				/**
				 * 将用户点击的页码传递给新闻中心,然后让此对象将数据传递给内容显示页上
				 */
				((MainActivity)context).getHomeFragment().getNewConterPager().switchPager(position);
				//关闭侧边栏
				slidingMenu.toggle();
			}
		});
		
		
	}
	
	/**
	 * 数据适配器
	 * @author Administrator
	 *
	 */
	class MyAdapter extends HMAdapter<String>{

		

		public MyAdapter(Context context, List<String> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder=null;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView=View.inflate(getActivity(), R.layout.layout_item_menu, null);
				holder.iv_menu_item=(ImageView) convertView.findViewById(R.id.iv_menu_item);
				holder.tv_menu_item=(TextView) convertView.findViewById(R.id.tv_menu_item);
				convertView.setTag(holder);
			}else {
				holder=(ViewHolder) convertView.getTag();
			}
			//设置每个条目的名称
			holder.tv_menu_item.setText(titleList.get(position));
			
			//设置条目选中时的状态
			if (position==currentPosition) {
				//说明是选中状态,将图片背景换成红色 不透明 
				holder.iv_menu_item.setImageResource(R.drawable.menu_arr_select);
				/**
				 * 这种用法 没见过   还是Color.RED好
				 */
				holder.tv_menu_item.setTextColor(context.getResources().getColor(R.color.red));
				convertView.setBackgroundResource(R.drawable.menu_item_bg_select);
			}else {
				/**
				 * 这里一定要有else 否则由于回收机制 会使得之前的item保留状态  就会彻底悲剧
				 */
				//说明是未选中状态,将图片背景换成白色  透明
				holder.iv_menu_item.setImageResource(R.drawable.menu_arr_normal);
				holder.tv_menu_item.setTextColor(context.getResources().getColor(R.color.white));
				convertView.setBackgroundResource(R.drawable.transparent);
			}
			return convertView;
		}
		
	}
	class ViewHolder{
		ImageView iv_menu_item;
		TextView tv_menu_item;
	}

	
	
}
