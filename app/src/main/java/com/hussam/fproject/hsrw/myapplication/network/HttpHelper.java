package com.hussam.fproject.hsrw.myapplication.network;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.hussam.fproject.hsrw.myapplication.constant.AppConstant.BASE_URL_API;


public class HttpHelper {

    private static HttpHelper instance;
    private Retrofit retrofit;

    private HttpHelper() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_API)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(new HttpFactory())
                .client(getClient())
                .build();
    }

    public static HttpHelper getInstance() {
        synchronized (HttpHelper.class) {
            if (instance == null) {
                instance = new HttpHelper();
            }
        }
        return instance;
    }

    public <T> T create(Class<T> clazz) {
        return retrofit.create(clazz);
    }

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("Authorization", "Basic YWRtaW46YWRtaW4=");
                    builder.addHeader("cache-control", "no-cache");
                    return chain.proceed(builder.build());
                }).build();


    }


}
