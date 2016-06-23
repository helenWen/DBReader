package com.helen.file.read.readdb;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.helen.file.read.R;
import com.helen.file.read.adapter.LocalGridViewAdapter;
import com.helen.file.read.fragment.MenuWindow;
import com.helen.file.read.service.DBManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class MainActivity extends BaseActivity implements MenuWindow.MenuWindowListener {
	
	private static final String TAG = "Main";
	
//	private LinearLayout mainLayout;
	
	private MeApplication mApplication;
	private DBManager dbm;
	private HashMap<String, String> fieldMap;
//	private ArrayList<ArrayList<Object>> contentList;
	private ArrayList<String> contentlist ;
	
	private LocalGridViewAdapter titleAdapter;
	private ArrayAdapter<String> adapter;
	
	private Button menuBtn;
	private SlidingMenu menu;
	
	private TextView title;
	private TextView info;
	private ListView listView;
	private GridView gridView;

	private MenuWindow menuWindow;

	@Override
	public int bindLayout() {
		return R.layout.window_main;
	}

	@Override
	public View bindView() {
		return LayoutInflater.from(this).inflate(R.layout.window_main, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.window_main);
Log.i(TAG,"onCreate()");

		initData();
		initView();
		
		initMenu();
	}
	
	private void initData(){
		if(mApplication == null){
			mApplication = (MeApplication) this.getApplication();
		}
		dbm = mApplication.dbm;
		
	}
	
	private void initView(){
		menuWindow = new MenuWindow();

		menuBtn = (Button) findViewById(R.id.menuBtn);
		title = (TextView) findViewById(R.id.title);
		info = (TextView) findViewById(R.id.info);
		gridView = (GridView) findViewById(R.id.gridView);
		listView = (ListView) findViewById(R.id.listView);
		
		menuWindow.setMenuWindowListener(this);
		
		menuBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMenu();
				
			}
		});
		
	}
	
	private void initMenu(){
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		//设置触摸屏幕的模式,改设置为全屏区域都可以滑动
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		//设置阴影图片宽度
		menu.setShadowWidthRes(R.dimen.shadow_width);
		//设置阴影图片
		menu.setShadowDrawable(R.drawable.slidingmenu_shadow);
		
		//SlidingMenu划出主页面显示的剩余宽度
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		//设置渐入渐出效果的值
		menu.setFadeDegree(0.35f);
		//使SlidingMenu附加在Activity上
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		//为侧滑菜单设置布局
		menu.setMenu(R.layout.main_menu);
		replaceFragment(R.id.menu,menuWindow);
	}
	
	private void showMenu(){
		menu.showMenu();
	}
	
	private void showContent(){
		menu.showContent();
	}
	
	@SuppressLint("NewApi")
	private void replaceFragment(int id, Fragment f){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(id, f);
		ft.commit();
	}
	
	//MenuWindowListener
	@Override
	public void listClick(int position, String content) {
		
		if(fieldMap != null){
			fieldMap.clear();
			fieldMap = null;
		}
		fieldMap = dbm.getFieldNames(content);
		
		ArrayList<HashMap<String, String>> cells = new ArrayList<HashMap<String, String>>();
		for(Entry<String, String> entry : fieldMap.entrySet()){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(entry.getKey(), entry.getValue());
			cells.add(map);
		}		
		
		int size = fieldMap.size();
		titleAdapter = new LocalGridViewAdapter(this, cells);
		gridView.setNumColumns(size>8? 8:size);
		gridView.setAdapter(titleAdapter);
		
		if(contentlist != null){
			contentlist.clear();
			contentlist = null;
		}
		contentlist = dbm.getContents2(content);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contentlist);
		listView.setAdapter(adapter);
		listView.setSelector(R.drawable.listview_selector_color);
		
		title.setText(content);
		info.setText(getResources().getString(R.string.row_count) + contentlist.size());
		showContent();
		
	}
	
}
