package com.preraktrivedi.android.mvvmbaseapp.requestmanager;

import android.text.TextUtils;
import android.util.Log;

import com.preraktrivedi.android.mvvmbaseapp.helpers.RxAndroidUtils;
import com.preraktrivedi.android.mvvmbaseapp.model.request.SplashInfoRequest;
import com.preraktrivedi.android.mvvmbaseapp.model.response.SplashInfoResponse;
import com.preraktrivedi.android.mvvmbaseapp.network.NetworkClient;

import java.util.UUID;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by preraktrivedi on 11/27/16.
 *
 * On request of the ViewModel, the manager will request the data from the network using the
 * API service layer
 *
 */
public class SplashRequestManager {

    private static final String TAG = SplashRequestManager.class.getSimpleName();
    private SplashInfoRequest mSplashRequest;

    public SplashRequestManager(String deviceId) {
        this.mSplashRequest = new SplashInfoRequest(deviceId);
    }

    public Flowable<SplashInfoResponse> getAppConfig() {
        //If DDT Device register is required, do that first, else just proceed with DAS calls
        if (mSplashRequest != null) {
            return simpleRequest1()
                    .flatMap(new Function<Object, Flowable<SplashInfoResponse>>() {
                        @Override
                        public Flowable<SplashInfoResponse> apply(Object o) throws Exception {
                            String token = processSimpleCallResponse(o);
                            if (TextUtils.isEmpty(token)) {
                                throw new IllegalArgumentException("Token cannot be empty");
                            }
                            return initiateParallelCalls();
                        }
                    });
        }
        return initiateParallelCalls();
    }

    private String processSimpleCallResponse(Object o) {
        //process Object o if needed
        return UUID.randomUUID().toString();
    }

    private Flowable<SplashInfoResponse> initiateParallelCalls() {
        return Flowable.zip(
                parallelRequest1(),
                parallelRequest2(),
                this::processParallelCallResponse);
    }

    private SplashInfoResponse processParallelCallResponse(SplashInfoResponse splashInfoResponse,
                                                           Object otherResponse) {
        //Process both input objects as needed
        if (splashInfoResponse != null) {
            Log.d(TAG, "processParallelCallResponse: " + splashInfoResponse.getParam());
        }

        return splashInfoResponse;
    }

    private Flowable<Object> simpleRequest1() {
        return RxAndroidUtils.convertToFlowable(
                NetworkClient.splashService.simpleRequest1());
    }

    private Flowable<SplashInfoResponse> parallelRequest1() {
        return RxAndroidUtils.convertToFlowable(
                NetworkClient.splashService.parallelRequest1(mSplashRequest));
    }

    private Flowable<Object> parallelRequest2() {
        return RxAndroidUtils.convertToFlowable(
                NetworkClient.splashService.parallelRequest2());
    }
}