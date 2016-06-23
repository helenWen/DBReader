package com.helen.file.read.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.helen.file.read.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LocalGridViewAdapter extends BaseAdapter {

	private static final String TAG = "GridViewAdapter";
	private Context mContext; 
	
	private ArrayList<HashMap<String, String>> names;
	
	public LocalGridViewAdapter(Context context, ArrayList<HashMap<String , String>> list) {
		this.mContext = context;
		this.names = list;
	}
	
	public void setDatas(ArrayList<HashMap<String , String>> list){
		this.names = list;
	}
	
	@Override
	public int getCount() {
Log.i(TAG,"size()="+names.size());
		return names==null? 0 : names.size();
	}

	@Override
	public Object getItem(int position) {
		return names==null? null : names.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder item = null;
		if(convertView == null){
			item = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);
			item.tv = (TextView) convertView.findViewById(R.id.tv);
			item.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			convertView.setTag(item);
		}else{
			item = (ViewHolder) convertView.getTag();
		}
		
		Map<String, String> map = null;
		map = names.get(position);
		
		for(Entry<String, String> entry :  map.entrySet()){
			item.tv.setText(entry.getKey());
			item.tv2.setText("( "+entry.getValue()+" )");
		}
		
		/*item.tv.setText(map.get("name")+"");
		item.tv2.setText(map2 == null? null : "("+map2.get("type")+")");*/
		
		return convertView;
	}

	
	class ViewHolder{
		TextView tv;
		TextView tv2;
	}
}
