package com.helen.file.read.service;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileManager {

	private static final String TAG = "FM";
	
	public static String rootDir;
	
	@SuppressWarnings("unused")
	private Context mContext;
	
	public FileManager(Context context) {
		this.mContext = context;
		rootDir = Environment.getExternalStorageDirectory() + File.separator;
Log.v(TAG,rootDir);
	}
	
	
	
}
