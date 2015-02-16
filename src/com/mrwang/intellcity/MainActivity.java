package com.mrwang.intellcity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.mrwang.intellcity.fragment.HomeFragment;
import com.mrwang.intellcity.fragment.MenuFragment;
import com.mrwang.intellcity.utils.SharedPreferencesUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.SearchManager.OnCancelListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;
/**
 * 开源 框架实现侧滑侧边栏效果
 * @author Administrator
 */
public class MainActivity extends SlidingFragmentActivity {

	public SlidingMenu slidingMenu;
	private int count = 1;
	private long fristTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//欢迎页
		boolean flag = SharedPreferencesUtil.getBooleanData(this, "is_frist", true);
		if (flag) {
			// 说明是第一次
			Intent intent = new Intent(this, WelcomeActivity.class);
			startActivity(intent);
			finish();
			SharedPreferencesUtil.saveBooleanData(getApplicationContext(), "is_frist", false);
			return;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//设置内容页布局为content,其实就是一个fragment的占位布局而已
		setContentView(R.layout.content);
		//设置侧拉条目的布局
		setBehindContentView(R.layout.menu_frame);
		
		//获取侧拉条目对象
		slidingMenu = getSlidingMenu();
		
		//slidingMenu.setsli;
		//设置触摸滑动的模式为无效 
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		/**
		 * 设置边缘的滑动模式为全屏触摸有效  这个有效解决了侧滑侧拉菜单没响应的问题
		 */
		slidingMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		//设置内容显示页所对应的dip大小
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		//设置左侧侧拉栏的宽度
		//slidingMenu.setBehindWidth(140);
		//设置侧拉栏目所在的位置 有左边 右边 或者左右都有
		slidingMenu.setMode(SlidingMenu.LEFT);
		
		//使用fragment去替换布局中的节点,先把主界面加载上去
		MenuFragment menuFragment=new MenuFragment();
		getSupportFragmentManager().//获取fragment管理器
		beginTransaction().//获取fragment事物管理器
		replace(R.id.menu, menuFragment,"MENU")//替换布局中的fragment
		.commit();//提交事物
		
		//使用fragment去替换布局中的节点,先把主界面加载上去
		HomeFragment homeFragment=new HomeFragment();
		getSupportFragmentManager().//获取fragment管理器
		beginTransaction().//获取fragment事物管理器
		replace(R.id.content_frame, homeFragment,"HOME")//替换布局中的fragment
		.commit();//提交事物		
	}
    /**
     * 获取MenuFragment对象
     * @return
     */
	public MenuFragment getMenuFragment() {
		return (MenuFragment) getSupportFragmentManager().findFragmentByTag("MENU");
	}
	/**
     * 获取HomeFragment对象
     * @return
     */
	public HomeFragment getHomeFragment() {
		return (HomeFragment) getSupportFragmentManager().findFragmentByTag("HOME");
	}
	/**
	 * 用户点击返回键调用的方法
	 */
	@Override
	public void onBackPressed() {
		AlertMethod();
		//ToastAlert();
		
	}
	/**
	 * 土司方式二  拦截返回键点击事件
	 */
	/*private long oldTime=0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis()-oldTime>2000) {
				Toast.makeText(MainActivity.this, "连按两次退出应用", 0).show();
				oldTime=System.currentTimeMillis();
			}else {
				finish();
			}
		}
		return true;
	}*/
	/**
	 * 土司的方式 
	 */
	private void ToastAlert() {
		//System.out.println("用户点击返回键拉,时间:"+System.currentTimeMillis());
		if (count%2==1) {
			fristTime = System.currentTimeMillis();
			Toast.makeText(MainActivity.this, "连按两次退出应用", 0).show();
		}else {
			if (System.currentTimeMillis()-fristTime<1000) {
				Toast.makeText(MainActivity.this, "欢迎您下次再来", 0).show();
				MainActivity.super.onBackPressed();
			}
		}
		count++;
	}
	/**
	 * 弹对话框的方式
	 */
	private void AlertMethod() {
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("大爷.您真的愿意抛弃小女子吗?");
		builder.setPositiveButton("精彩继续", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("残忍抛弃!", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(MainActivity.this, "欢迎您下次再来", 0).show();
				dialog.dismiss();
				MainActivity.super.onBackPressed();
			}
		});
		builder.show();
	}
}
