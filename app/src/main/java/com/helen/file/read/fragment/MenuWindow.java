package com.helen.file.read.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.helen.file.read.R;
import com.helen.file.read.readdb.MeApplication;

public class MenuWindow extends Fragment {
	private static final String TAG = "MW";
	private View mView;
	private EditText dbName;
	private ListView nameList;
	
	private MeApplication mApplication;
	private ArrayAdapter<String> adapter;
	private List<String> tableNames;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(mApplication == null){
			mApplication = (MeApplication) getActivity().getApplication();
		}
		
		tableNames = mApplication.tableNames;
		
	}
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mView == null){
			mView = inflater.inflate(R.layout.menu_content, null);
			initView(mView);
		}
		return mView;
	}
	
	private void initView(View v){
		dbName = (EditText) v.findViewById(R.id.dbName);
		nameList = (ListView) v.findViewById(R.id.nameList);
		
		dbName.setText(mApplication.dbPath);
		
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableNames);
		nameList.setAdapter(adapter);
		nameList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(TAG,"position="+position+","+nameList.getItemAtPosition(position));
				menuListener.listClick(position, (String)nameList.getItemAtPosition(position));
			}
		});
		
	}
	
	//--------------------------------------------------------------
	//给MainActivity的回调函数
	private MenuWindowListener menuListener;
	
	public void setMenuWindowListener(MenuWindowListener menuListener){
		this.menuListener = menuListener;
	}
	
	public interface MenuWindowListener{
		public void listClick(int position, String content); 
	}
	
}
