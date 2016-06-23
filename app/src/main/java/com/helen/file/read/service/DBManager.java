package com.helen.file.read.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private static final String TAG = "DBM";
	private static final String DEFAULT_DB_NAME = "demo.db";
	
	public static SQLiteDatabase db;
	
	public DBManager(String dbName) {
		Log.i(TAG,"DBManager() dbName="+dbName);
		
		try{
			if(dbName == null || "".equals(dbName)){
				db = SQLiteDatabase.openDatabase(DEFAULT_DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
			}else{
	Log.i(TAG,"enter");
				db = SQLiteDatabase.openDatabase(dbName, null, SQLiteDatabase.OPEN_READONLY |
						SQLiteDatabase.CREATE_IF_NECESSARY );
			}
		}catch(Exception e){
			e.printStackTrace();
Log.e(TAG,"Open Database Eorror");
		}
		
		/*if(db != null){
			getAllTableNames();
		}*/
				
	}
	
	/**
	 * 关闭数据库
	 * */
	public static void close(){
		try{
			if(db != null){
				db.close();
				db = null;
			}
		}catch(Exception e){
			
		}
	}
	
	/**
	 * 执行SQL语句
	 * */
	public void exeSQL(String sql){
		db.beginTransaction();
		try{
			db.execSQL(sql);
			db.setTransactionSuccessful();
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db.endTransaction();
		}
	}
	
	/**
	 * 获取所有的数据表名,保存在tableNames中
	 * */
	public List<String> getAllTableNames(){
		List<String> tableNames = new ArrayList<String>(); 
		Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);  
		 //遍历出表名  
		  while(cursor.moveToNext()){  
			  tableNames.add(cursor.getString(0));
Log.i(TAG,"table:"+cursor.getString(0));
		  } 
		  cursor.close();
		  
		  return tableNames;
	}
	
	
	/**
	 * 获得所有的字段名,类型
	 * */
	public HashMap<String, String> getFieldNames(String tableName){
		String sql = "PRAGMA table_info(["+tableName+"])";
		Cursor cur = db.rawQuery(sql, null);
		
		HashMap<String, String> map = new LinkedHashMap<String, String>();
		while(cur.moveToNext()){
			map.put(cur.getString(cur.getColumnIndex("name")), cur.getString(cur.getColumnIndex("type")));
Log.i(TAG,"field="+cur.getString(cur.getColumnIndex("name")));
		}
		cur.close();
		
		return map; 
	}
	
	/**
	 * 获得数据表的数据内容(不知道数据表长度)
	 * */
	public ArrayList<ArrayList<Object>> getContents(String tableName){
		String sql = "SELECT * FROM "+ tableName +" ORDER BY 1";
		ArrayList<ArrayList<Object>> rtnList = new ArrayList<ArrayList<Object>>();
		
		Cursor cur = db.rawQuery(sql, null);
		String[] fields = cur.getColumnNames();
		int count = cur.getColumnCount();
		while(cur.moveToNext()){
			ArrayList<Object> list = new ArrayList<Object>();
			for(int i=0;i<count;i++){
				Log.i(TAG,"fieldName="+fields[i]+","+cur.getString(cur.getColumnIndex(fields[i])));
				list.add(cur.getString(cur.getColumnIndex(fields[i])));
			}
			rtnList.add(list);
		}
		
		cur.close();
		
		return rtnList;
	}
	
	public ArrayList<String> getContents2(String tableName){
		String sql = "SELECT * FROM "+ tableName +" ORDER BY 1";
		ArrayList<String> rtnList = new ArrayList<String>();
		
		Cursor cur = db.rawQuery(sql, null);
		String[] fields = cur.getColumnNames();
		int count = cur.getColumnCount();
		while(cur.moveToNext()){
			String str = new String();
			for(int i=0;i<count;i++){
//				Log.i(TAG,"fieldName="+fields[i]+"="+cur.getString(cur.getColumnIndex(fields[i])));
				str += cur.getString(cur.getColumnIndex(fields[i])) +"  ,  ";
			}
//			Log.i(TAG,"list="+str);
			str = "".equals(str)?"":str.substring(0, str.length()-3);
			rtnList.add(str);
		}
		cur.close();
		
		return rtnList;
	}
	
	
}
