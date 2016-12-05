package com.preraktrivedi.android.instagrampopular;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

/**
 * Created by preraktrivedi on 11/21/16.
 */
public class InstagramApplication extends MultiDexApplication {

    private static final String TAG = InstagramApplication.class.getSimpleName();

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
