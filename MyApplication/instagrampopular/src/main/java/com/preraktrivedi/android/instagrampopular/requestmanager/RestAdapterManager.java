package com.preraktrivedi.android.instagrampopular.requestmanager;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestAdapterManager {

    private static final long REQUEST_TIMEOUT_DURATION = 30;

    private static volatile RestAdapterManager instance;

    private RestAdapterManager() {}

    public static synchronized RestAdapterManager getInstance() {
        if (instance == null) {
            instance = new RestAdapterManager();
        }
        return instance;
    }

    public Retrofit createRestAdapter(final String baseUrl, Interceptor requestInterceptor) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(requestInterceptor))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getClient(Interceptor requestInterceptor) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .readTimeout(REQUEST_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .connectTimeout(REQUEST_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT_DURATION, TimeUnit.SECONDS);

        return clientBuilder.build();
    }

}