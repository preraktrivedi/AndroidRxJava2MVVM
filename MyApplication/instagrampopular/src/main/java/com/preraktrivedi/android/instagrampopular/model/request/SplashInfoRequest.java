package com.preraktrivedi.android.instagrampopular.model.request;

/**
 * Created by preraktrivedi on 12/4/16.
 *
 * Sample request class
 *
 */

public class SplashInfoRequest {

    private String deviceId;

    public SplashInfoRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
