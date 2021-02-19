package com.lrh.libnetwork.api;

/**
 * Created by LRH on 2020/11/29 0029
 */
public class ApiResponse<T> {
    public String message;
    public int status;
    public boolean success;
    public T body;
}
