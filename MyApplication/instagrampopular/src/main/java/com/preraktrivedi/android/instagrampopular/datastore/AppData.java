package com.preraktrivedi.android.instagrampopular.datastore;

/**
 * Created by preraktrivedi on 11/26/16.
 */
public class AppData {

    private static final String TAG = AppData.class.getSimpleName();

    private static AppData sInstance;

    private AppData() {}

    public static AppData getInstance() {
        synchronized (AppData.class) {
            if (null == sInstance) {
                sInstance = new AppData();
            }
            return sInstance;
        }
    }
}
