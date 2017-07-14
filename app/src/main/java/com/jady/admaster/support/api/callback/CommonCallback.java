package com.jady.admaster.support.api.callback;


import com.jady.retrofitclient.callback.HttpCallback;

/**
 * @Description: Created by jadyli on 2017/5/4.
 */
public abstract class CommonCallback<T> extends HttpCallback<T> {
    @Override
    public void onResolve(T t) {
        onSuccess(t);
    }

    @Override
    public void onFailed(String error_code, String error_message) {
        onFailure(error_code, error_message);
    }

    public abstract void onSuccess(T v);

    public abstract void onFailure(String error_code, String error_message);

}
