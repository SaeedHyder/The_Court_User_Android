package com.app.court.retrofit;


import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CustomInterceptor implements Interceptor {

    private ProgressResponseBody.ProgressListener progressListener;

    public CustomInterceptor(ProgressResponseBody.ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();

        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

        return newResponse.newBuilder()
                .body(new ProgressResponseBody(newResponse.body(), progressListener))
                .build();
    }
}
