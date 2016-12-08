package com.preraktrivedi.android.mvvmbaseapp.network;

import com.preraktrivedi.android.mvvmbaseapp.network.apiservice.SplashApiService;
import com.preraktrivedi.android.mvvmbaseapp.network.interceptors.MockResponseInterceptor;
import com.preraktrivedi.android.mvvmbaseapp.requestmanager.RestAdapterManager;

import retrofit2.Retrofit;

/**
 * Created by preraktrivedi on 12/4/16.
 *
 * Network client to manage all the API services
 */

public class NetworkClient {

    public static final String MOCK_BASE_URL = "https://offlineurl.com/";

    private static Retrofit mMockAdapter = getMockApiAdapter(MOCK_BASE_URL);
    public static SplashApiService splashService = mMockAdapter.create(SplashApiService.class);

    static Retrofit getMockApiAdapter(String baseUrl) {
        return RestAdapterManager.getInstance().createRestAdapter(
                baseUrl, new MockResponseInterceptor());
    }
}
