package com.helen.file.read.readdb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.helen.file.read.R;
import com.helen.file.read.service.DBManager;
import com.helen.file.read.service.FileManager;
import com.helen.file.read.utils.KeyboardUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends BaseActivity {

	private static final String TAG = "Launch";
	
	private static final String SHARED_REFFERENCE_NAME = "tool";
	private static final String SHARED_ITEM_DB_NAME = "dbIdx";
	
	private RelativeLayout loginLayout;
	private Spinner dbSpn;
	private Button read;
	
	private MeApplication mApplication;
	private DBManager dbm;
	private int dbIdx;
	
	private List<String> lstFile;
	private ArrayAdapter<String> dbAdapter;

	@Override
	public int bindLayout() {
		return R.layout.window_login;
	}

	@Override
	public View bindView() {
		return LayoutInflater.from(this).inflate(R.layout.window_login, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
		initView();
	}
	
	private void initData(){
		if(mApplication == null){
			mApplication = (MeApplication)getApplication(); 
		}

		mApplication.fm = new FileManager(this);
		
		getDBIndex();
		
		lstFile = new ArrayList<String>(); //结果 List
		getFiles(FileManager.rootDir, ".db", true);
		/*for(String st : lstFile){
			Log.i(TAG,"db = "+st);
		}
		*/
		if(dbIdx >= lstFile.size()){
			dbIdx = 0;
		}
	}

	private void initView(){
		read = (Button) findViewById(R.id.read);
		dbSpn = (Spinner) findViewById(R.id.dbSpn);
		loginLayout = (RelativeLayout)findViewById(R.id.loginLayout);
		
		setListener();
	}
	
	private void setListener(){
		dbAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,lstFile);
		dbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dbSpn.setAdapter(dbAdapter);
		dbSpn.setSelection(dbIdx);
		dbSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				dbIdx = position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String tPath = (String)dbSpn.getSelectedItem();				
				
				if("".equals(tPath)){
					Toast.makeText(LaunchActivity.this, "The dbPath should not be empty!", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(!tPath.endsWith(".db")){
					Toast.makeText(LaunchActivity.this, "Could not parse the file, it should be end of '.db' !", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(!(new File(tPath)).exists()){
					Toast.makeText(LaunchActivity.this, "Could not find the file!", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(dbm != null){
					DBManager.close();
					dbm = null;
				}
				dbm = new DBManager(tPath);
				if(DBManager.db == null){
					Toast.makeText(LaunchActivity.this, "Open Database failed!", Toast.LENGTH_LONG).show();
					return;
				}
				
				mApplication.dbPath = tPath;
				mApplication.dbm = dbm;
				saveDBIndex(dbIdx);
				
				mApplication.tableNames = mApplication.dbm.getAllTableNames();
				
				startActivity(new Intent().setClass(LaunchActivity.this, MainActivity.class));
			}
		});
		
		loginLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getCurrentFocus() != null){
					KeyboardUtils.hideKeyboard(LaunchActivity.this, getCurrentFocus().getWindowToken());
				}
				
			}
		});
	}

	private void getDBIndex(){
Log.i(TAG,"getDBPath()");
		SharedPreferences sp = getSharedPreferences(SHARED_REFFERENCE_NAME, Context.MODE_PRIVATE);
		dbIdx = sp.getInt(SHARED_ITEM_DB_NAME, 0);
	}
	
	@SuppressLint("CommitPrefEdits")
	private void saveDBIndex(int dbIdx){
Log.i(TAG,"saveDBPath() "+dbIdx);
		SharedPreferences sp = getSharedPreferences(SHARED_REFFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(SHARED_ITEM_DB_NAME, dbIdx);
		editor.commit();
	}
	
	
	/**
	 * 遍历文件夹，获得指定拓展名的文件 
	 * @param path  搜索目录
	 * @param extension 拓展名
	 * @param isIterative 是否进入子文件夹
	 * */ 
	public void getFiles(String path, String extension,boolean isIterative){
	    File[] files =new File(path).listFiles();

		if(files != null) {
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (f.isFile()) {
					if (f.getPath().substring(f.getPath().length() - extension.length()).equals(extension)) { //判断扩展名
						lstFile.add(f.getPath());
					}

					if (!isIterative) {
						break;
					}
				} else if (f.isDirectory() && f.getPath().indexOf("/.") == -1) { //忽略点文件（隐藏文件/文件夹）
					getFiles(f.getPath(), extension, isIterative);
				}
			}
		}
	}
}
