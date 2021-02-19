package com.lrh.libnetwork.api;

/**
 * Created by LRH on 2020/11/29 0029
 */
public abstract class JsonCallBack<T> {
    public void onSuccess(ApiResponse<T> response) {

    }

    public void onError(ApiResponse<T> response) {

    }

    public void onCacheSuccess(ApiResponse<T> response) {

    }
}
