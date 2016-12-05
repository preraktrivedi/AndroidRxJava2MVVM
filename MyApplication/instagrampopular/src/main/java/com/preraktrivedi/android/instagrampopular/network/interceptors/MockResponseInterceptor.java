package com.preraktrivedi.android.instagrampopular.network.interceptors;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.preraktrivedi.android.instagrampopular.InstagramApplication;
import com.preraktrivedi.android.instagrampopular.helpers.Util;
import com.preraktrivedi.android.instagrampopular.network.NetworkClient;
import com.preraktrivedi.android.instagrampopular.network.mock.MockApiResponseConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by preraktrivedi on 12/4/16.
 */
public class MockResponseInterceptor implements Interceptor {

    private static final String TAG = MockResponseInterceptor.class.getSimpleName();
    public static final String HTTP_500_MSG = "Internal Server Error";
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PATCH = "PATCH";
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String HEADER_KEY_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_VALUE_APPLICATION_JSON = "application/json";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        HttpUrl incomingUrl = request.url();
        String httpVerb = request.method();

        if (!incomingUrl.toString().startsWith(NetworkClient.MOCK_BASE_URL)) {
            Log.d(TAG, "Performing actual request with OKHttp");
            return chain.proceed(request);
        }

        //Induce some wait time
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.e(TAG, "intercept: ", e);
        }

        String url = sanitizeUrlForMockLocation(incomingUrl.toString());
        Log.d(TAG, "retrofit \nHTTP " + httpVerb + "\noriginal request url - "
                + incomingUrl + "\nupdated request url: " + url);

        String uriPath = Uri.parse(url).getPath();
        Log.d(TAG, "retrofit Uri Path: " + uriPath);

        //Catch all - error HTTP 500
        Response response = getDefaultErrorResponse(request);

        MockApiResponseConfig responseConfig = getResponseConfigFromUriPath(uriPath, httpVerb);

        if (responseConfig != null) {
            response = parseResponse(request, responseConfig);
        }

        return response;
    }

    private MockApiResponseConfig getResponseConfigFromUriPath(String uriPath, String httpVerb) {
        MockApiResponseConfig responseConfig = null;

        for (MockApiResponseConfig config : MockApiResponseConfig.values()) {
            String configEndpoint = config.getUrlEndpoint();
            String configVerb = config.getHttpVerb();
            if (uriPath.equalsIgnoreCase(configEndpoint) && httpVerb.equalsIgnoreCase(configVerb)) {
                responseConfig = config;
            }
        }

        return responseConfig;
    }
    private String sanitizeUrlForMockLocation(String url) {
        return Util.replaceGuidsWithPlaceHolders(url)
                .replace("{id}","id")
                .replaceAll("[a-fA-F0-9]{32}", "id");
    }

    private Response getDefaultErrorResponse(Request request) {
        String responseBody = getResponseFromJson("internal_server_error.json");

        if (TextUtils.isEmpty(responseBody)) {
            responseBody = HTTP_500_MSG;
        }

        return getBaseBuilder(request, responseBody, 500).build();
    }

    private String getResponseFromJson(String filePath) {
        String responseBody = "";
        try {
            responseBody = Util.readAssetFile(InstagramApplication.getContext(), filePath);
        } catch (Exception ex) {
            Log.e(TAG, "exception reading response for " + filePath +
                    "\n " + Log.getStackTraceString(ex));
        }
        return responseBody;
    }

    private Response parseResponse(Request request, MockApiResponseConfig responseConfig) {

        String responseBody;

        if (responseConfig == null) {
            return getDefaultErrorResponse(request);
        }

        responseBody = getResponseFromJson(responseConfig.getResponseResourcePath());

        if (TextUtils.isEmpty(responseBody)) {
            return getDefaultErrorResponse(request);
        }

        return getBaseBuilder(request, responseBody, responseConfig.getHttpStatus()).build();
    }


    private Response.Builder getBaseBuilder(Request request, String responseBody, int status) {

        Response.Builder builder = new Response.Builder();

        builder.protocol(Protocol.HTTP_1_1);
        builder.request(request);
        builder.body(ResponseBody.create(MediaType.parse(HEADER_VALUE_APPLICATION_JSON), responseBody));
        builder.code(status);

        return builder;
    }
}
