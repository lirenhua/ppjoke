package com.lrh.ppjoke.util;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by LRH on 2020/11/15 0015
 */
public class AppGlobals {
    private static Application sApplication;

    public static Application getApplication() {
        if (sApplication == null) {
            try {
                Method method = Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication");
                sApplication = (Application) method.invoke(null);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return sApplication;
    }
}
