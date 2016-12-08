package com.preraktrivedi.android.mvvmbaseapp.model.response;

import java.util.UUID;

/**
 * Created by preraktrivedi on 12/4/16.
 *
 * Sample response class.
 *
 */

public class SplashInfoResponse {

    private String token;
    private String param;

    public SplashInfoResponse() {
        setToken(UUID.randomUUID().toString());
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
