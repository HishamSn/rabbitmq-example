package com.hussam.fproject.hsrw.myapplication.network;


import android.support.annotation.NonNull;

import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.CallAdapter;

public class HttpCallAdapter<R> implements CallAdapter<R, HttpCall<R>> {

    private final Type type;
    private final Executor callbackExecutor;

    HttpCallAdapter(Type type, Executor callbackExecutor) {
        this.type = type;
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public Type responseType() {
        return type;
    }

    @Override
    public HttpCall<R> adapt(@NonNull Call<R> call) {
        return new HttpEnqueueCallAdapter<>(call, callbackExecutor);
    }
}
