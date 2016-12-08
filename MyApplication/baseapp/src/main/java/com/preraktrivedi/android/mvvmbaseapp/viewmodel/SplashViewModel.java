package com.preraktrivedi.android.mvvmbaseapp.viewmodel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.preraktrivedi.android.mvvmbaseapp.helpers.RxAndroidUtils;
import com.preraktrivedi.android.mvvmbaseapp.requestmanager.SplashRequestManager;
import com.preraktrivedi.android.mvvmbaseapp.viewbinder.BaseViewModelBinder;
import com.preraktrivedi.android.mvvmbaseapp.viewbinder.SplashBinder;

import io.reactivex.disposables.Disposable;
import io.reactivex.processors.AsyncProcessor;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by preraktrivedi on 11/26/16.
 *
 * Needs to implement BaseViewModelBinder.ViewModelCallback so that fragments can call appropriate
 * methods.
 *
 * Contains the business logic, checks if required data is available in cache, if not then requests
 * from the RequestManager.
 *
 * Based on the response, notify the View callback to process the data.
 *
 */
public class SplashViewModel implements SplashBinder.ViewModel {

    private static final String TAG = SplashViewModel.class.getSimpleName();
    private AsyncProcessor<Object> mSplashProcessor;
    private SplashBinder.View mViewCallback;
    private Disposable mSplashDisposable;
    private String deviceId, username;

    private SplashViewModel(String deviceId, String username) {
        this.deviceId = deviceId;
        this.username = username;
    }

    @Override
    public void getAppSettings() {
        mSplashProcessor = AsyncProcessor.create();
        mSplashDisposable = mSplashProcessor.subscribeWith(new SplashSubscriber());
        new SplashRequestManager(deviceId).
                getAppConfig().
                subscribe(mSplashProcessor);
    }

    @Override
    public void onViewAttached(@NonNull BaseViewModelBinder.ViewCallback viewCallback) {
        this.mViewCallback = (SplashBinder.View) viewCallback;
    }

    @Override
    public void onViewCreated() {
        if (mSplashDisposable != null) {
            mSplashProcessor.subscribe(new SplashSubscriber());
        }
    }

    @Override
    public void onViewDestroyed() {
        this.mViewCallback = null;
        if (mSplashDisposable != null) {
            RxAndroidUtils.unsubscribeDisposable(mSplashDisposable);
        }
    }

    private class SplashSubscriber extends DisposableSubscriber<Object> {

        @Override
        public void onNext(Object appDeviceResponse) {
            Log.d(TAG, "SplashSubscriber.onSuccess");
            if (mViewCallback != null) {
                mViewCallback.onAppLaunchSuccess();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "SplashSubscriber.onError");
            if (mViewCallback != null) {
                mViewCallback.onAppLaunchFailure(e);
            }
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "SplashSubscriber.onComplete");
        }
    }

    public static class Builder {

        private String deviceId;
        private String username;

        public Builder(String deviceId) {
            this.deviceId = deviceId;
        }

        public Builder with(String username) {
            this.username = username;
            return this;
        }

        public SplashViewModel build() {
            return new SplashViewModel(deviceId, username);
        }
    }
}