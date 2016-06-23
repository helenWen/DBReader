package com.helen.file.read.readdb;

import java.util.List;

import android.app.Application;
import android.util.Log;

import com.helen.file.read.service.DBManager;
import com.helen.file.read.service.FileManager;

public class MeApplication extends Application {

	private static final String TAG = "MApplication";
	
	public FileManager fm;
	public DBManager dbm;
	
	public String dbPath;
	
	public List<String> tableNames;
	
	public MeApplication() {
Log.v(TAG,"MeApplication()");

	}
}
