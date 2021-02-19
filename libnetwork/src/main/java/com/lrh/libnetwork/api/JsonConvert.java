package com.lrh.libnetwork.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.lang.reflect.Type;

/**
 * Created by LRH on 2020/12/6 0006
 */
public class JsonConvert implements Convert {
    @Override
    public Object convert(String content, Type type) {
        JSONObject jsonObject = JSON.parseObject(content);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object data1 = data.get("data");
            Object object = JSON.parseObject(data1.toString(), type);
            return object;
        }
        return null;
    }

    @Override
    public Object convert(String content, Class clazz) {
        JSONObject jsonObject = JSON.parseObject(content);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object data1 = data.get("data");
            Object object = JSON.parseObject(data1.toString(), clazz);
            return object;
        }
        return null;
    }
}
