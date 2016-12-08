package com.preraktrivedi.android.mvvmbaseapp.network.mock;

import com.preraktrivedi.android.mvvmbaseapp.network.ApiEndpoints;

import static com.preraktrivedi.android.mvvmbaseapp.network.interceptors.MockResponseInterceptor.HTTP_METHOD_GET;
import static com.preraktrivedi.android.mvvmbaseapp.network.interceptors.MockResponseInterceptor.HTTP_METHOD_POST;


/**
 * Created by prtrived on 12/4/16.
 */
public enum MockApiResponseConfig {

    SIMPLE_REQUEST_1(HTTP_METHOD_GET, ApiEndpoints.SIMPLE_REQUEST_1,
            200,  "mock_empty_response.json"),

    PARALLEL_REQUEST_1(HTTP_METHOD_POST, ApiEndpoints.PARALLEL_REQUEST_1,
            201,  "mock_splash_response.json"),

    PARALLEL_REQUEST_2(HTTP_METHOD_GET, ApiEndpoints.PARALLEL_REQUEST_2,
            200,  "mock_empty_response.json");


    MockApiResponseConfig(String httpVerb, String urlEndpoint,
                          int httpStatus, String jsonResponsePath) {
        setHttpVerb(httpVerb);
        setUrlEndpoint(urlEndpoint);
        setHttpStatus(httpStatus);
        setResponseResourcePath(jsonResponsePath);
    }

    private String httpVerb, urlEndpoint, jsonResponsePath;
    private int httpStatus;

    public String getHttpVerb() {
        return httpVerb;
    }

    private void setHttpVerb(String httpVerb) {
        this.httpVerb = httpVerb;
    }

    public String getUrlEndpoint() {
        return urlEndpoint;
    }

    private void setUrlEndpoint(String urlEndpoint) {
        this.urlEndpoint = urlEndpoint;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    private void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getResponseResourcePath() {
        return jsonResponsePath;
    }

    private void setResponseResourcePath(String responseResource) {
        this.jsonResponsePath = responseResource;
    }

}
