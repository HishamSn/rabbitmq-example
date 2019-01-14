package com.hussam.fproject.hsrw.myapplication.network;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;


public class HttpFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations,
                                 @NonNull Retrofit retrofit) {
        if (getRawType(returnType) != HttpCall.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException(
                    "MyCall must have generic type (e.g., MyCall<ResponseBody>)");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
        Executor callbackExecutor = retrofit.callbackExecutor();
        return new HttpCallAdapter<>(responseType, callbackExecutor);
    }
}
