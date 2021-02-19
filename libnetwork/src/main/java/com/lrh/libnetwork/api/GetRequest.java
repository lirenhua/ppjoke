package com.lrh.libnetwork.api;

/**
 * Created by LRH on 2020/12/6 0006
 */
public class GetRequest<T> extends Request<T, GetRequest> {
    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        okhttp3.Request request = builder.get().url(UrlCreator.createUrlFromParams(url, params)).build();
        return request;
    }
}
