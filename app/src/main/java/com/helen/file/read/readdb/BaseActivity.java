package com.helen.file.read.readdb;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by admin on 2016/6/14.
 */
public abstract class BaseActivity extends Activity {

    public final String TAG = getClass().getSimpleName();

    private boolean isFullScreen = false;

    private boolean isOrientationPortrait = true;

    private boolean isSetStatusBar = true;

    protected View mContextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarScreen();

        View view = bindView();
        if(null == view){
            mContextView = LayoutInflater.from(this)
                    .inflate(bindLayout(), null);
        }else{
            mContextView = view;
        }

        setContentView(mContextView);

        initBaseView(mContextView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * set Window Screen
     * */
    private void setStatusBarScreen(){
//        Log.d(TAG, "setStatusBarScreen " + isFullScreen + " " + isOrientationPortrait);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(isOrientationPortrait){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        if(isFullScreen){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            if(isSetStatusBar){
                steepStatusBar();
            }
        }

    }

    private void initBaseView(View v){

    }

    private void steepStatusBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public void startActivity(Class clz){
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public void startActivity(Class clz, Bundle bundle){
        Intent intent = new Intent(this, clz);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * Go back to Main
     * */
    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void setIsFullScreen(boolean isFullScreen) {
        this.isFullScreen = isFullScreen;
    }

    public void setIsOrientationPortrait(boolean isOrientationPortrait) {
        this.isOrientationPortrait = isOrientationPortrait;
    }

    public void setIsSetStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * */
    public abstract int bindLayout();

    /**
     * */
    public abstract View bindView();

}
