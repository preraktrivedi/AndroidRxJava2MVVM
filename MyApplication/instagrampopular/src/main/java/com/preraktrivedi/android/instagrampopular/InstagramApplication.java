package com.preraktrivedi.android.instagrampopular;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

/**
 * Created by preraktrivedi on 11/21/16.
 */
public class InstagramApplication extends MultiDexApplication {

    private static final String TAG = InstagramApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.d(TAG, ">onCreate");
        super.onCreate();
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
}
