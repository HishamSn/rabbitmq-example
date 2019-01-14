package com.hussam.fproject.hsrw.myapplication.network;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HttpEnqueueCallAdapter<T> implements HttpCall<T> {

    private final Call<T> call;
    private final Executor callbackExecutor;
    private Handler handler = new Handler(Looper.getMainLooper());

    public HttpEnqueueCallAdapter(Call<T> call, Executor callbackExecutor) {
        this.call = call;
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public void cancel() {
        call.cancel();
    }

    @Override
    public void enqueue(final HttpCallback<T> callback) {
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                int code = response.code();
                HttpResult<T> result = new HttpResult<>();
                result.setCode(code);
                result.setCall(HttpEnqueueCallAdapter.this);
                result.setResult(response.body());
                if (code >= 200 && code < 300) {
                    result.setStatus(HttpStatus.SUCCESS);
                } else if (code == 400 || code == 401) {
                    result.setStatus(HttpStatus.BAD_REQUEST);
                }
                else if (code == 403) {
                    result.setStatus(HttpStatus.FORBIDDEN);
                } else if (code == 404) {
                    result.setStatus(HttpStatus.NOT_FOUND);
                } else if (code >= 500 && code < 600) {
                    result.setStatus(HttpStatus.SERVER_ERROR);
                } else {
                    result.setStatus(HttpStatus.UNEXPECTED_ERROR);
                }
                handler.post(() -> callback.onResult(result));


            }

            @Override
            public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                HttpResult<T> result = new HttpResult<>();
                result.setCall(HttpEnqueueCallAdapter.this);
                result.setThrowable(t);
                if (t instanceof IOException) {
                    result.setStatus(HttpStatus.NETWORK_ERROR);
                } else {
                    result.setStatus(HttpStatus.UNEXPECTED_ERROR);
                }
                handler.post(() -> callback.onResult(result));
            }
        });
    }

    @Override
    public HttpCall<T> cloneCall() {
        return new HttpEnqueueCallAdapter<>(call.clone(), callbackExecutor);
    }
}
