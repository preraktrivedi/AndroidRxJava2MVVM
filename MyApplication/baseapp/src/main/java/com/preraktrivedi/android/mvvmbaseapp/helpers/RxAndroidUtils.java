package com.preraktrivedi.android.mvvmbaseapp.helpers;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by prtrived on 11/26/16.
 *
 * Utility methods for using RxAndroid/RxJava classes.
 */
public class RxAndroidUtils {

    public static void unsubscribeDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public static <T> Flowable<T> convertToFlowable(Observable<T> inObservable) {
        return inObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

}