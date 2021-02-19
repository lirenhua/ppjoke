package com.lrh.libnetwork.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by LRH on 2020/12/6 0006
 */
public class UrlCreator {
    public static String createUrlFromParams(String url, Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        if (url.indexOf("?") > 0 || url.indexOf("&") > 0) {
            sb.append("&");
        }else {
            sb.append("?");
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            try {
                String value = URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8");
                sb.append(entry.getKey()).append("=").append(value).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
