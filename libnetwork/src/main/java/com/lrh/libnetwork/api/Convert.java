package com.lrh.libnetwork.api;

import java.lang.reflect.Type;

/**
 * Created by LRH on 2020/12/6 0006
 */
public interface Convert<T> {
    T convert(String content, Type type);

    T convert(String content, Class clazz);
}
