package com.app.court.retrofit;


import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.app.court.helpers.BasePreferenceHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OKHttpClientCreator {

    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;

    @NonNull
    public static OkHttpClient createCustomInterceptorClient(Context context) {
        final BasePreferenceHelper preferenceHelper = new BasePreferenceHelper(context);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addNetworkInterceptor(new CustomInterceptor(progressListener));
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (preferenceHelper != null &&
                        preferenceHelper.getSignUpUser() != null &&
                        preferenceHelper.getSignUpUser().getToken() != null) {

                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("token", preferenceHelper.getSignUpUser().getToken())
                            .addHeader("Accept-Encoding", "identity")
                            .build();
                    return chain.proceed(request);
                } else {
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept-Encoding", "identity")
                            .build();
                    return chain.proceed(request);
                }
            }
        });
        return httpClient.build();
    }

    @NonNull
    public static OkHttpClient createCustomInterceptorClientWithoutToken(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new CustomInterceptor(progressListener));
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request().newBuilder()
                        .addHeader("Accept-Encoding", "identity")
                        .build();
                return chain.proceed(request);
            }
        });
        return httpClient.build();
    }

    public static OkHttpClient createDefaultInterceptorClient(Context context) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()

                .addNetworkInterceptor(new Interceptor() {
                    @Override

                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .addInterceptor(logging)
                .build();
        return client;
    }

    final static ProgressResponseBody.ProgressListener progressListener = new ProgressResponseBody.ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            int percent = (int) ((100 * bytesRead) / contentLength);
        }
    };

}
