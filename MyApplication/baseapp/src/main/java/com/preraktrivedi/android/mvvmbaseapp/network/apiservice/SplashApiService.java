package com.preraktrivedi.android.mvvmbaseapp.network.apiservice;

import com.preraktrivedi.android.mvvmbaseapp.model.request.SplashInfoRequest;
import com.preraktrivedi.android.mvvmbaseapp.model.response.SplashInfoResponse;
import com.preraktrivedi.android.mvvmbaseapp.network.ApiEndpoints;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by preraktrivedi on 12/4/16.
 *
 * Service for all the Splash calls (mock for now)
 */
public interface SplashApiService {

    @GET(ApiEndpoints.SIMPLE_REQUEST_1)
    Observable<Object> simpleRequest1();

    @POST(ApiEndpoints.PARALLEL_REQUEST_1)
    Observable<SplashInfoResponse> parallelRequest1(@Body SplashInfoRequest splashInfoRequest);

    @GET(ApiEndpoints.PARALLEL_REQUEST_2)
    Observable<Object> parallelRequest2();
}
