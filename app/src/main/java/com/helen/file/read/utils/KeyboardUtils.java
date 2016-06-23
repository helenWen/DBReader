package com.helen.file.read.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

	/**
	 * show keyboard
	 * */
	public static void showKeyboard(View v){
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
	}
	
	/**
	 * hide keyboard
	 * */
	public static boolean hideKeyboard(Context context, IBinder windowToken) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){
			return imm.hideSoftInputFromWindow(windowToken, 0);
		}
		return true;
	}
	
	/** 
	 * hide keyboard
	 * */
	public static void hideKeyboard(Context context, View... views) {
		if (views != null) {
			for (int i = 0; i < views.length; i++) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm.isActive()){
					imm.hideSoftInputFromWindow((views[i]).getWindowToken(), 0);
					views[i].clearFocus();
				}
			}
		}
	}
	
}
