package com.preraktrivedi.android.mvvmbaseapp.viewbinder;

/**
 * Created by prtrived on 11/15/16.
 */
public interface SplashBinder {

    interface View extends BaseViewModelBinder.ViewCallback {
        void onAppLaunchSuccess();
        void onAppLaunchFailure(Throwable t);
    }

    interface ViewModel extends BaseViewModelBinder.ViewModelCallback {
        void getAppSettings();
    }
}