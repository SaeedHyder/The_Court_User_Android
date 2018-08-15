package com.app.court.retrofit;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServiceFactory {

    private static WebService webService;

    public static WebService getWebServiceInstanceWithCustomInterceptor(Context context, String endPoint) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OKHttpClientCreator.createCustomInterceptorClient(context))
                .build();

        webService = retrofit.create(WebService.class);

        return webService;

    }

    public static WebService getWebServiceInstanceWithCustomInterceptorWithoutToken(Context context, String endPoint) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OKHttpClientCreator.createCustomInterceptorClientWithoutToken(context))
                .build();

        webService = retrofit.create(WebService.class);

        return webService;

    }

    public static WebService getWebServiceInstanceWithDefaultInterceptor(Context context, String endPoint) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OKHttpClientCreator.createDefaultInterceptorClient(context))
                .build();

        webService = retrofit.create(WebService.class);

        return webService;

    }
}
