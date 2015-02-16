package com.mrwang.intellcity.fragment;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
/**
 * ListView的公共adapter类 
 * @author Administrator
 *
 */
public abstract class HMAdapter<T> extends BaseAdapter {
	
	public Context context;
	public List<T> list;
	/**
	 * 子类必须传入数据的集合
	 * @param context
	 * @param list
	 */
	public HMAdapter(Context context,List<T> list){
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	/**
	 * 子类必须实现的方法 
	 */
	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

}
