package com.lrh.libnetwork.api;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.lrh.libnetwork.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.IntDef;
import androidx.arch.core.executor.ArchTaskExecutor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by LRH on 2020/11/29 0029
 */
public abstract class Request<T, R extends Request> {
    protected String url;
    protected HashMap<String, String> headers = new HashMap<>();
    protected HashMap<String, Object> params = new HashMap<>();

    public String cacheKey;
    private int mCacheStrategy = NET_ONLY;

    public static final int CACHE_ONLY = 1;
    public static final int CACHE_FIRST = 2;

    public static final int NET_ONLY = 3;
    public static final int NET_CACHE = 4;
    protected Type mType;

    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY})
    public @interface CacheStrategy {

    }

    // T Response的实体类型,R Request的子类类型
    public Request(String url) {
        this.url = url;
    }

    /**
     * 添加请求头
     *
     * @param key
     * @param value
     * @return
     */
    public R addHeaders(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    /**
     * 添加参数
     *
     * @param key
     * @param value
     * @return
     */
    public R addParams(String key, Object value) {
        try {
            Field tpye = value.getClass().getField("TPYE");
            Class clazz = (Class) tpye.get(null);
            if (clazz.isPrimitive()) {
                params.put(key, value);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    public R cacheStrategy(@CacheStrategy int cacheStrategy) {
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }

    public R addCache(String key) {
        this.cacheKey = key;
        return (R) this;
    }

    public R responseType(Type type) {
        mType = type;
        return (R) this;
    }

    private Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.okHttpClient.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 异步请求
     *
     * @param jsonCallBack
     */
    @SuppressLint("RestrictedApi")
    public void execute(final JsonCallBack<T> jsonCallBack) {
        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse<T> response = readCache();
                    if (jsonCallBack != null && response.body != null) {
                        jsonCallBack.onCacheSuccess(response);
                    }
                }
            });
        }

        if (mCacheStrategy != CACHE_ONLY) {
            getCall().enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    ApiResponse<T> apiResponse = new ApiResponse<>();
                    jsonCallBack.onError(apiResponse);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ApiResponse<T> apiResponse = parseResponse(response, jsonCallBack);
                    boolean success = apiResponse.success;
                    if (success) {
                        jsonCallBack.onSuccess(apiResponse);
                    } else {
                        jsonCallBack.onError(apiResponse);
                    }
                }
            });
        }
    }

    private ApiResponse<T> parseResponse(Response response, JsonCallBack<T> jsonCallBack) {
        Convert convert = ApiService.sConvert;
        int status = response.code();
        boolean success = response.isSuccessful();
        String message = response.message();
        ApiResponse<T> result = new ApiResponse<>();
        try {
            String content = response.body().toString();
            if (success) {
                if (jsonCallBack != null) {
                    ParameterizedType parameterizedType = (ParameterizedType) jsonCallBack.getClass().getGenericSuperclass();
                    Type argument = parameterizedType.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                } else if (mType != null) {
                    result.body = (T) convert.convert(content, mType);
                } else {
                    Log.d("request", "parseResponse: 无法解析");
                }
            } else {
                message = content;
                success = false;
            }
        } catch (Exception e) {
            message = e.getMessage();
            success = false;
            status = 0;
        }

        result.status = status;
        result.success = success;
        result.message = message;

        if (mCacheStrategy != NET_ONLY
                && result.success
                && result.body != null) {
            saveCache(result.body);
        }
        return result;
    }

    private ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> result = new ApiResponse<>();
        result.status = 304;
        result.message = "缓存获取成功";
        result.body = (T) cache;
        result.success = true;
        return result;
    }

    private void saveCache(T body) {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        CacheManager.save(key, body);
    }

    private String generateCacheKey() {
        cacheKey = UrlCreator.createUrlFromParams(url, params);
        return cacheKey;
    }

    public ApiResponse<T> execute() {
        if (mType == null) {
            throw new RuntimeException("同步方法,response 返回值 类型必须设置");
        }

        if (mCacheStrategy == CACHE_ONLY) {
            return readCache();
        }

        if (mCacheStrategy != CACHE_ONLY) {
            ApiResponse<T> result = null;
            try {
                Response response = getCall().execute();
                result = parseResponse(response, null);
            } catch (IOException e) {
                e.printStackTrace();
                if (result == null) {
                    result = new ApiResponse<>();
                    result.message = e.getMessage();
                }
            }
            return result;
        }
        return null;
    }
}
