package com.preraktrivedi.android.mvvmbaseapp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

/**
 * Created by preraktrivedi on 11/21/16.
 */
public class BaseApplication extends MultiDexApplication {

    private static final String TAG = BaseApplication.class.getSimpleName();

    private static Context mContext;

    @Override
    public void onCreate() {
        Log.d(TAG, ">onCreate");
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Override
    public void onLowMemory() {
        Log.d(TAG, ">onLowMemory");
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, ">onTerminate");
        super.onTerminate();
    }

    public static Context getContext() {
        return mContext;
    }
}
